package ru.vershinin.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.vershinin.utils.Convert;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.vershinin.dto.DeviceCommand;
import ru.vershinin.model.Device;
import ru.vershinin.repository.DeviceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {


    private final DeviceRepository deviceRepository;
    private final Convert convert;



    private final KafkaTemplate<String, String> kafkaTemplate;

    @SneakyThrows
    public Device registerDevice(Device device) {
        device.setLastUpdate(LocalDateTime.now());

        var savedDevice= deviceRepository.save(device);
        log.info("новое устройство добавлено {}",device);
        // Публикация информации о новом устройстве в Kafka
        kafkaTemplate.send("device-topic",convert.getJsonDevice(savedDevice));
        return device;
    }

    @SneakyThrows
    public Device updateDeviceStatus(Long id, String status)  {
        var deviceOpt = deviceRepository.findById(id);
        if (deviceOpt.isEmpty()) {
            throw new Exception("Device not found with id " + id);
        }

        Device device = deviceOpt.get();

        // Обновление состояния устройства
        device.setStatus(status);
        Device updatedDevice = deviceRepository.save(device);
        log.info("обновление статуса устройства {}",updatedDevice);

        // Публикация обновленного состояния устройства в Kafka
        kafkaTemplate.send("device-status-topic", convert.getJsonDevice(updatedDevice));

        return updatedDevice;
    }

    @SneakyThrows
    public void sendCommand(DeviceCommand command) {


         // Сериализация объекта в JSON
        String jsonString = convert.getJsonDeviceCommand(command);

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("device_command_topic",jsonString);

        future.thenAccept(result -> {
            log.info("Сообщение отправлено успешно. Тема: " + result.getRecordMetadata().topic() +
                    ", Раздел: " + result.getRecordMetadata().partition() +
                    ", Смещение: " + result.getRecordMetadata().offset());
        }).exceptionally(ex -> {
            log.info("Ошибка при отправке сообщения: " + ex.getMessage());
            // Обработка ошибки отправки
            return null;
        });

    }

    @KafkaListener(topics = "rollback-topic", groupId = "rollback-group")
    private void rollback(String message){
        log.info("коррекционное воздействие {}",message);
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}

