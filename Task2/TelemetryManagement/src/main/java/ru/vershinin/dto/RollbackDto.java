package ru.vershinin.dto;

import lombok.Data;

@Data
public class RollbackDto {
    private String idDevice;
    private String description;
    private String typeEvent;
}
