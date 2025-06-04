package com.coelhoworks.coelhosensors.temperature.monitoring.domain.service;

import com.coelhoworks.coelhosensors.temperature.monitoring.api.model.TemperatureLogData;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorId;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.TemperatureLog;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class TemperatureMonitoringService {

  private final SensorMonitoringRepository sensorMonitoringRepository;
  private final TemperatureLogRepository temperatureLogRepository;

  @Transactional
  public void processTemperatureLog(TemperatureLogData temperatureLogData){

    sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
            .ifPresentOrElse(
                    sensor -> handleMonitoring(temperatureLogData, sensor),
                    () -> logIgnoreTemperature(temperatureLogData));

  }

  private void handleMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
    if (sensor.isEnabled()) {
      sensor.setLastTemperature(temperatureLogData.getValue());
      sensor.setUpdatedAt(OffsetDateTime.now());
      sensorMonitoringRepository.save(sensor);

      TemperatureLog temperatureLog = TemperatureLog.builder()
              .id(new TemperatureLogId(temperatureLogData.getId()))
              .registeredAt(temperatureLogData.getRegisteredAt())
              .value(temperatureLogData.getValue())
              .sensorId(new SensorId(temperatureLogData.getSensorId()))
              .build();

      temperatureLogRepository.save(temperatureLog);
      log.info("Temperature Updated: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
    } else {
      logIgnoreTemperature(temperatureLogData);
    }
  }

  private void logIgnoreTemperature(TemperatureLogData temperatureLogData) {
    log.info("Temperature Ignored: SensorId {} Temp {}", temperatureLogData.getSensorId(), temperatureLogData.getValue());
  }
}
