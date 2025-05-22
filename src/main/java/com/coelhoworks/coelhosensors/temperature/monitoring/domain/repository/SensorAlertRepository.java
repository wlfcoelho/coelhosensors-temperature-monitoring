package com.coelhoworks.coelhosensors.temperature.monitoring.domain.repository;

import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorAlert;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorAlertRepository extends JpaRepository<SensorAlert, SensorId> {
}
