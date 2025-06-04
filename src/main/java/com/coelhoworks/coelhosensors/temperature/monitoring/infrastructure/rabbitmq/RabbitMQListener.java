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

import static com.coelhoworks.coelhosensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUEURL;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

  private final TemperatureMonitoringService temperatureMonitoringService;

  @RabbitListener(queues = QUEUEURL, concurrency = "2-3")
  @SneakyThrows
  public void handle(@Payload TemperatureLogData temperatureLogData) {
    temperatureMonitoringService.processTemperatureLog(temperatureLogData);

    Thread.sleep(Duration.ofSeconds(5));
  }
}
