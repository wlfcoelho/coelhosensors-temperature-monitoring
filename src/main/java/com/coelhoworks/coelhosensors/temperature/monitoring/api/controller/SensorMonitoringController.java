package com.coelhoworks.coelhosensors.temperature.monitoring.api.controller;

import com.coelhoworks.coelhosensors.temperature.monitoring.ConstantApp;
import com.coelhoworks.coelhosensors.temperature.monitoring.api.model.SensorMonitoringOuput;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorId;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {

  private final SensorMonitoringRepository sensorMonitoringRepository;

  @GetMapping
  public SensorMonitoringOuput detail(@PathVariable TSID sensorId) {
    SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);

    return SensorMonitoringOuput.builder()
            .id(sensorMonitoring.getId().getValue())
            .enabled(sensorMonitoring.getEnabled())
            .lastTemperature(sensorMonitoring.getLastTemperature())
            .updatedAt(sensorMonitoring.getUpdatedAt())
            .build();
  }

  private SensorMonitoring findByIdOrDefault(TSID sensorId) {
    return sensorMonitoringRepository.findById(new SensorId(sensorId))
            .orElse(SensorMonitoring.builder()
                    .id(new SensorId(sensorId))
                    .enabled(false)
                    .lastTemperature(null)
                    .updatedAt(null)
                    .build());
  }

  @PutMapping("/enable")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void enable(@PathVariable TSID sensorId) {
    SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);
    if (ConstantApp.TRUE.equals(sensorMonitoring.getEnabled())) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    sensorMonitoring.setEnabled(true);
    sensorMonitoringRepository.saveAndFlush(sensorMonitoring);
  }

  @DeleteMapping("/enable")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @SneakyThrows
  public void disable(@PathVariable TSID sensorId) {
    SensorMonitoring sensorMonitoring = findByIdOrDefault(sensorId);
    if (ConstantApp.FALSE.equals(sensorMonitoring.getEnabled())) {
      Thread.sleep(Duration.ofSeconds(10));
    }
    sensorMonitoring.setEnabled(false);
    sensorMonitoringRepository.saveAndFlush(sensorMonitoring);
  }
}
