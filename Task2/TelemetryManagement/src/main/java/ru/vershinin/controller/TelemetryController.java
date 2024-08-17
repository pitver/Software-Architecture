package ru.vershinin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vershinin.model.TelemetryData;
import ru.vershinin.service.TelemetryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @Operation(summary = "Прием телеметрических данных", description = "Получает и сохраняет телеметрические данные от устройства.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Телеметрические данные успешно сохранены",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TelemetryData.class)) }),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TelemetryData> receiveTelemetry(@RequestBody TelemetryData telemetryData) {
        TelemetryData savedData = telemetryService.saveTelemetryData(telemetryData);
        return ResponseEntity.ok(savedData);
    }

    @Operation(summary = "Получение телеметрических данных по ID устройства", description = "Возвращает список телеметрических данных для указанного устройства.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные успешно получены",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TelemetryData.class)) }),
            @ApiResponse(responseCode = "404", description = "Устройство не найдено", content = @Content)
    })
    @GetMapping("/{deviceId}")
    public ResponseEntity<List<TelemetryData>> getTelemetryByDeviceId(@PathVariable String deviceId) {
        List<TelemetryData> telemetryDataList = telemetryService.getTelemetryDataByDeviceId(deviceId);
        return ResponseEntity.ok(telemetryDataList);
    }
}


