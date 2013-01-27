package com.example.amqp;

import com.example.amqp.core.User;
import com.example.amqp.health.AMQPHealthCheck;
import com.example.amqp.resources.StockQuoteResource;
import com.rabbitmq.client.ConnectionFactory;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TradingService extends Service<MessageQueueConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(TradingService.class);

    public static void main(String... args) throws Exception {
        new TradingService().run(new String[]{"server", "src/main/resources/amqp.yaml"});
    }

    @Override
    public void initialize(Bootstrap<MessageQueueConfiguration> bootstrap) {
        bootstrap.setName("amqp");
    }

    @Override
    public void run(MessageQueueConfiguration configuration, Environment environment) throws Exception {
        final AMQPConfiguration amqpConfiguration = configuration.getAMQPConfiguration();
        final String host = amqpConfiguration.getHost();
        final int port = amqpConfiguration.getPort();
        final String username = amqpConfiguration.getUsername();
        final String password = amqpConfiguration.getPassword();

        try {
            ConnectionFactory connectionFactory = createConnectionFactory(username, password, host, port);
            environment.addHealthCheck(new AMQPHealthCheck(connectionFactory));
            environment.addResource(new StockQuoteResource(connectionFactory, amqpConfiguration));
        } catch (IOException e) {
            LOG.error("Error connecting to AMQP server. Reason : " + e.getMessage(), e);
        }
        environment.addProvider(new BasicAuthProvider<User>(new DumbAuthenticator(), "SUPER SECRET STUFF"));
        if (configuration.getJerseyConfiguration().isLogTraffic()) {
            environment.setJerseyProperty("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
            environment.setJerseyProperty("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
        }
    }

    private ConnectionFactory createConnectionFactory(String userName, String password, String hostName, int portNumber) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setConnectionTimeout(1000);//times out in 1s.
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setHost(hostName);
        factory.setPort(portNumber);
        return factory;
    }

}
