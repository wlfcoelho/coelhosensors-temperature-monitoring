package com.coelhoworks.coelhosensors.temperature.monitoring.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorAlertInput {
  private Double maxTemperature;
  private Double minTemperature;

}
