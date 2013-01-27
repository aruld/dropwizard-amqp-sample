package com.example.amqp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rabbitmq.client.ConnectionFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class AMQPConfiguration {

    /**
     * Shared topic exchange used for publishing any market data (e.g. stock quotes)
     */
    @JsonProperty
    private String exchange = "app.stock.marketdata";

    /**
     * The server-side consumer's queue that provides point-to-point semantics for stock requests.
     */
    @JsonProperty
    private String queue = "app.stock.request";

    /**
     * Key that clients will use to send to the stock request queue via the default direct exchange.
     */
    @JsonProperty
    private String routingKey = queue;


    @NotEmpty
    @JsonProperty
    private String host;

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int port = ConnectionFactory.DEFAULT_AMQP_PORT;

    @NotEmpty
    @JsonProperty
    private String username = ConnectionFactory.DEFAULT_USER;

    @NotEmpty
    @JsonProperty
    private String password = ConnectionFactory.DEFAULT_PASS;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExchange() {
        return exchange;
    }

    public String getQueue() {
        return queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }
}