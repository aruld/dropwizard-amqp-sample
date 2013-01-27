package com.example.amqp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MessageQueueConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty("amqp")
    private AMQPConfiguration amqp = new AMQPConfiguration();

    @Valid
    @JsonProperty("jersey")
    private JerseyConfiguration jersey = new JerseyConfiguration();

    public AMQPConfiguration getAMQPConfiguration() {
        return amqp;
    }

    public void setAmqp(AMQPConfiguration amqp) {
        this.amqp = amqp;
    }

    public void setJersey(JerseyConfiguration jersey) {
        this.jersey = jersey;
    }

    public JerseyConfiguration getJerseyConfiguration() {
        return jersey;
    }
}