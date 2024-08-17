package ru.vershinin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import ru.vershinin.dto.DeviceCommand;
import ru.vershinin.model.Device;
import ru.vershinin.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Регистрация нового устройства", description = "Регистрирует новое устройство в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Устройство успешно зарегистрировано",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Device> registerDevice(@RequestBody Device device) {
        Device registeredDevice = deviceService.registerDevice(device);
        return ResponseEntity.ok(registeredDevice);
    }

    @Operation(summary = "Обновление состояния устройства", description = "Обновляет состояние устройства (например, включить/выключить).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Состояние устройства успешно обновлено",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class)) }),
            @ApiResponse(responseCode = "404", description = "Устройство не найдено", content = @Content)
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<Device> updateDeviceStatus(@PathVariable Long id, @RequestParam String status) throws Exception {
        Device updatedDevice = deviceService.updateDeviceStatus(id, status);
        return ResponseEntity.ok(updatedDevice);
    }

    @Operation(summary = "Отправка команды устройству", description = "Отправляет команду указанному устройству.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Команда успешно отправлена"),
            @ApiResponse(responseCode = "404", description = "Устройство не найдено", content = @Content)
    })
    @PostMapping("/commands")
    public ResponseEntity<Void> sendCommand(@RequestBody DeviceCommand command) {
        deviceService.sendCommand(command);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получение списка всех устройств", description = "Возвращает список всех зарегистрированных устройств.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список устройств успешно получен",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Device.class)) })
    })
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }
}


