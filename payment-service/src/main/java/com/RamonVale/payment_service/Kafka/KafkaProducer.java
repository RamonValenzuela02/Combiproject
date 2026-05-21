package com.RamonVale.payment_service.Kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

  private KafkaTemplate<String, Object> kafkaTemplates;

  public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplates) {
    this.kafkaTemplates = kafkaTemplates;
  }

  public void publicar(String topic, Object payload) {
    kafkaTemplates.send(topic, payload);
  }
}
