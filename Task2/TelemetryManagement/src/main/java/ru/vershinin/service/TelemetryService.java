package ru.vershinin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.vershinin.dto.DeviceCommand;
import ru.vershinin.dto.RollbackDto;
import ru.vershinin.model.Device;
import ru.vershinin.model.TelemetryData;
import ru.vershinin.repository.TelemetryRepository;
import ru.vershinin.utils.Convert;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelemetryService {


    private final TelemetryRepository telemetryDataRepository;
    private final Convert convert;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TelemetryData saveTelemetryData(TelemetryData telemetryData) {
        telemetryData.setTimestamp(LocalDateTime.now());
        return telemetryDataRepository.save(telemetryData);
    }

    public List<TelemetryData> getTelemetryDataByDeviceId(String deviceId) {
        return telemetryDataRepository.findByDeviceId(deviceId);
    }

    @KafkaListener(topics = "telemetry_topic", groupId = "telemetry-management-group")
    public void consumeTelemetryData(TelemetryData telemetryData) {
        saveTelemetryData(telemetryData);
    }

    @KafkaListener(topics = "device-topic", groupId = "telemetry-group")
    public void handleNewDevice(String message) {
        var device = convert.JsonToDevice(message);// Обработка нового устройства (например, подготовка для приема телеметрии)

        try {
            log.info("New device registered: {}", message);

        } catch (Exception e) {
            //saga rollback
            log.error("ошибка обработки нового устройства {}", e.getMessage());
            RollbackDto rollbackDto = new RollbackDto();
            rollbackDto.setIdDevice(device.getDeviceId());
            rollbackDto.setDescription(e.getMessage());
            rollbackDto.setTypeEvent("device");
            var mes = convert.getJsonDevice(rollbackDto);
            kafkaTemplate.send("rollback-topic", mes);
        }


    }

    @KafkaListener(topics = "device-status-topic", groupId = "telemetry-group")
    public void handleDeviceStatusUpdate(String message) {
        // Обработка обновленного состояния устройства
        log.info("Device status updated: {}", message);
        // Если необходимо, можно предпринять какие-то действия на основе изменения состояния устройства
    }

    @SneakyThrows
    @KafkaListener(topics = "device_command_topic", groupId = "telemetry-group")
    public void handleDeviceCommand(String message) {
        var command = convert.JsonToDeviceCommand(message);
        // Обработка команды
        log.info("Received command for device {}: {}", command.getDeviceId(), command.getCommand());

        // Пример действия: если команда связана с началом или завершением сбора телеметрии
        if ("START_TELEMETRY".equals(command.getCommand())) {
            startTelemetryCollection(command.getDeviceId());
        } else if ("STOP_TELEMETRY".equals(command.getCommand())) {
            stopTelemetryCollection(command.getDeviceId());
        }
    }

    private void startTelemetryCollection(String deviceId) {
        // Логика для начала сбора телеметрии
        log.info("Starting telemetry collection for device " + deviceId);
    }

    private void stopTelemetryCollection(String deviceId) {
        // Логика для завершения сбора телеметрии
        log.info("Stopping telemetry collection for device {}", deviceId);
    }

}

