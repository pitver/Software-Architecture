package ru.vershinin.dto;

import lombok.Data;

@Data
public class DeviceCommand {

    private String deviceId;
    private String command;
    private String value;

}
