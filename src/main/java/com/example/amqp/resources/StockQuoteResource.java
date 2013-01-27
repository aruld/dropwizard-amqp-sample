package com.example.amqp.resources;

import com.example.amqp.AMQPConfiguration;
import com.example.amqp.core.TradeRequest;
import com.example.amqp.core.TradeResponse;
import com.example.amqp.core.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yammer.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Path("/trader")
public class StockQuoteResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockQuoteResource.class);

    public static final String DEFAULT_CHARSET = "UTF-8";

    private volatile String defaultCharset = DEFAULT_CHARSET;

    private static final String DEFAULT_ENCODING = "UTF-8";

    private String encoding = DEFAULT_ENCODING;

    public static final String CONTENT_TYPE_JSON = "application/json";

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    private final ConnectionFactory connectionFactory;
    private final AMQPConfiguration amqpConfiguration;
    private Random random = new Random();

    public StockQuoteResource(final ConnectionFactory connectionFactory, final AMQPConfiguration amqpConfiguration) throws IOException {
        this.connectionFactory = connectionFactory;
        this.amqpConfiguration = amqpConfiguration;
    }

    @POST
    public Response processTrade(@Auth User user, TradeRequest request) {
        try {
            submitRequest(request);
        } catch (Exception e) {
            LOG.error("Failed to publish to RabbitMQ.", e);
            return Response.serverError().build();
        }

        // Mocked up response
        TradeResponse response = new TradeResponse();
        response.setAccountName(request.getAccountName());
        response.setUserName(user.getUserName());
        response.setOrderType(request.getOrderType());
        response.setPrice(calculatePrice(request.getTicker(), request.getQuantity(), request.getOrderType(), request.getPrice(), request.getUserName()));
        response.setQuantity(request.getQuantity());
        response.setTicker(request.getTicker());
        response.setRequestId(request.getId());
        response.setConfirmationNumber(UUID.randomUUID().toString());

        try {
            LOG.info("Sleeping 2 seconds to simulate processing..");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOG.error("Didn't finish sleeping", e);
        }

        return Response.ok(response).build();
    }

    private BigDecimal calculatePrice(String ticker, long quantity,
                                      String orderType, BigDecimal price, String userName) {
        // provide as sophisticated implementation...for now all the same price.
        if (orderType.compareTo("LIMIT") == 0) {
            return price;
        } else {
            //in line with market data implementation
            return new BigDecimal(22 + Math.abs(gaussian()));
        }
    }

    private double gaussian() {
        return random.nextGaussian();
    }

    private void submitRequest(TradeRequest request) throws Exception {
        byte[] bytes;
        String jsonString = jsonObjectMapper.writeValueAsString(request);
        bytes = jsonString.getBytes(this.defaultCharset);
        AMQP.BasicProperties.Builder target = new AMQP.BasicProperties.Builder();
        target.messageId(UUID.randomUUID().toString());
        target.contentType(CONTENT_TYPE_JSON);
        target.contentEncoding(defaultCharset);
        byte[] correlationId = UUID.randomUUID().toString().getBytes("UTF-8");
        if (correlationId != null && correlationId.length > 0) {
            try {
                target.correlationId(new String(correlationId, encoding));
            } catch (UnsupportedEncodingException ex) {
            }
        }


        Channel channel = null;
        Connection conn = null;
        try {
            conn = connectionFactory.newConnection();
            channel = conn.createChannel();
            channel.basicPublish(amqpConfiguration.getExchange(), amqpConfiguration.getRoutingKey(), false, false, target.build(), bytes);
        } finally {
            if (channel != null) {
                channel.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        LOG.info("Publishing message on exchange [" + amqpConfiguration.getExchange() + "], routingKey = [" + amqpConfiguration.getRoutingKey() + "]");
    }
}
