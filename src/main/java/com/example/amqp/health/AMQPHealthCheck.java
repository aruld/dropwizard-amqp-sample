package com.example.amqp.health;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yammer.dropwizard.logging.Log;
import com.yammer.metrics.core.HealthCheck;

import java.io.IOException;

public class AMQPHealthCheck extends HealthCheck {

  private static final Log LOG = Log.forClass(AMQPHealthCheck.class);
  private final ConnectionFactory connectionFactory;

  public AMQPHealthCheck(ConnectionFactory connectionFactory) {
    super("amqp");
    this.connectionFactory = connectionFactory;
  }

  @Override
  protected Result check() throws Exception {
    Connection conn = null;
    try {
      conn = connectionFactory.newConnection();
      if (conn.isOpen()) {
        return Result.healthy();
      } else {
        return Result.unhealthy("AMQP connection is closed.");
      }
    } catch (IOException e) {
      return Result.unhealthy("Cannot open AMQP connection. " + e.getMessage());
    } finally {
      if (conn != null)
        try {
          LOG.info("Closing AMQP connection.");
          conn.close();
        } catch (IOException e) {
          conn = null;
          LOG.info("Error closing AMQP connection.");
        }
    }
  }
}
