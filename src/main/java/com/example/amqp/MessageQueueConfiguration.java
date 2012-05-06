package com.example.amqp;

import com.yammer.dropwizard.config.Configuration;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

public class MessageQueueConfiguration extends Configuration {
  @NotNull
  @JsonProperty("amqp")
  private AMQPConfiguration amqp = new AMQPConfiguration();

  @JsonProperty("jersey")
  private JerseyConfiguration jersey = new JerseyConfiguration();

  public AMQPConfiguration getAMQPConfiguration() {
    return amqp;
  }

  public JerseyConfiguration getJerseyConfiguration() {
    return jersey;
  }
}