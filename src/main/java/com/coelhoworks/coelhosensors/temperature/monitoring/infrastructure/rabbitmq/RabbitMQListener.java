package com.coelhoworks.coelhosensors.temperature.monitoring.infrastructure.rabbitmq;

import com.coelhoworks.coelhosensors.temperature.monitoring.api.model.TemperatureLogData;
import com.coelhoworks.coelhosensors.temperature.monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.coelhoworks.coelhosensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_ALERTING;
import static com.coelhoworks.coelhosensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE_PROCESS_TEMPERATURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

  private final TemperatureMonitoringService temperatureMonitoringService;

  @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
  @SneakyThrows
  public void handleProcessTemperature(@Payload TemperatureLogData temperatureLogData) {
    temperatureMonitoringService.processTemperatureLog(temperatureLogData);

    Thread.sleep(Duration.ofSeconds(5));
  }

  @RabbitListener(queues = QUEUE_ALERTING, concurrency = "2-3")
  @SneakyThrows
  public void handleAlerting(@Payload TemperatureLogData temperatureLogData) {
    temperatureMonitoringService.processTemperatureLog(temperatureLogData);

    Thread.sleep(Duration.ofSeconds(5));
  }
}
