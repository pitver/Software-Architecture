package ru.vershinin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/reports")
public class ReportController {


    @GetMapping
    public Map<String, Object> getReport() {
        Map<String, Object> report = new HashMap<>();

        // Генерация случайных данных для отчёта
        report.put("reportId", new Random().nextInt(1000));
        report.put("date", LocalDate.now());
        report.put("totalSales", new Random().nextInt(5000));
        report.put("totalExpenses", new Random().nextInt(3000));
        report.put("profit", new Random().nextInt(2000));
        report.put("description", "Сгенерированные данные для отчёта");

        return report;
    }
}
