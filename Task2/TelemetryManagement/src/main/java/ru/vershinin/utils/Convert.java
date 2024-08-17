package ru.vershinin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vershinin.dto.DeviceCommand;
import ru.vershinin.dto.RollbackDto;
import ru.vershinin.model.Device;


@Slf4j
@RequiredArgsConstructor
@Service
public class Convert {
    private final ObjectMapper objectMapper;

    public DeviceCommand JsonToDeviceCommand(String message) throws JsonProcessingException {
        // Десериализация JSON в объект
        DeviceCommand command = objectMapper.readValue(message, DeviceCommand.class);
        // Обработка команды
        log.info("Received command for device {}: {}", command.getDeviceId(), command.getCommand());

        return command;

    }

    public String getJsonDevice(RollbackDto rollbackDto) {

        try {
            return objectMapper.writeValueAsString(rollbackDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Device JsonToDevice(String message) {
        // Десериализация JSON в объект
        Device command = null;
        try {
            command = objectMapper.readValue(message, Device.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Обработка команды
        assert command != null;
        log.info("Received update for device {}: {}", command.getDeviceId(), command);

        return command;

    }
}
