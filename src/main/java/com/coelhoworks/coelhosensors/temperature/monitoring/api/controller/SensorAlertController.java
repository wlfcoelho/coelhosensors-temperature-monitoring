package com.coelhoworks.coelhosensors.temperature.monitoring.api.controller;

import com.coelhoworks.coelhosensors.temperature.monitoring.api.model.SensorAlertInput;
import com.coelhoworks.coelhosensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorAlert;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.model.SensorId;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

  private final SensorAlertRepository sensorAlertRepository;

  @GetMapping
  public SensorAlertOutput getDetail(@PathVariable TSID sensorId) {
    SensorAlert sensorAlert = findById(sensorId);

    return getAlertOutput(sensorAlert);

  }

  private SensorAlert findById(TSID sensorId) {
    return sensorAlertRepository.findById(new SensorId(sensorId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  }

  @PutMapping
  public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId,
                                     @RequestBody SensorAlertInput input) {

    SensorAlert sensorAlert = findByIdOrDefault(sensorId);
    sensorAlert.setMaxTemperature(input.getMaxTemperature());
    sensorAlert.setMinTemperature(input.getMinTemperature());
    sensorAlert = sensorAlertRepository.saveAndFlush(sensorAlert);

    return getAlertOutput(sensorAlert);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAlert(@PathVariable TSID sensorId){

    SensorAlert sensorAlert = findById(sensorId);

    sensorAlertRepository.delete(sensorAlert);
  }

  private static SensorAlertOutput getAlertOutput(SensorAlert sensorAlert) {
    return SensorAlertOutput.builder()
            .id(sensorAlert.getId().getValue())
            .maxTemperature(sensorAlert.getMaxTemperature())
            .minTemperature(sensorAlert.getMinTemperature())
            .build();
  }

  private SensorAlert findByIdOrDefault(TSID sensorId) {
    return sensorAlertRepository.findById(new SensorId(sensorId))
            .orElse(
                    SensorAlert.builder()
                            .id(new SensorId(sensorId))
                            .maxTemperature(null)
                            .minTemperature(null)
                            .build()
            );
  }
}
