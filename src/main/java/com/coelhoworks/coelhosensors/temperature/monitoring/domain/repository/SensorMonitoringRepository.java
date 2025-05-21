package com.coelhoworks.coelhosensors.temperature.monitoring.domain.repository;

import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorId;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorMonitoringRepository extends JpaRepository<SensorMonitoring, SensorId> {
}
