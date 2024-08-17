package ru.vershinin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vershinin.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
}
