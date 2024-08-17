package ru.vershinin.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vershinin.dto.DeviceCommand;
import ru.vershinin.model.Device;

@Service
@Slf4j
@RequiredArgsConstructor
public class Convert {
    private final ObjectMapper objectMapper;

    public String getJsonDevice(Device device) throws JsonProcessingException {

            return objectMapper.writeValueAsString(device);

    } public String getJsonDeviceCommand(DeviceCommand device) throws JsonProcessingException {

        return objectMapper.writeValueAsString(device);

    }


}
