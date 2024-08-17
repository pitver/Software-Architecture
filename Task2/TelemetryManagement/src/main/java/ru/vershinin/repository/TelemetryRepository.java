package ru.vershinin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vershinin.model.TelemetryData;

import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<TelemetryData, Long> {
    List<TelemetryData> findByDeviceId(String deviceId);
}
