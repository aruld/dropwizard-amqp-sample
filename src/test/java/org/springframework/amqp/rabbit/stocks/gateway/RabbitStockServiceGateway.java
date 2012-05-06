package org.springframework.amqp.rabbit.stocks.gateway;

import com.example.amqp.core.TradeRequest;
import com.example.amqp.core.TradeResponse;
import com.sun.cloud.api.storage.client.SSLUtilities;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.core.MediaType;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Future;

/**
 * Rabbit implementation of {@link StockServiceGateway} to send trade requests to an external process.
 *
 * @author Mark Pollack
 */
public class RabbitStockServiceGateway implements StockServiceGateway {

  final String TRADER_ENDPOINT = "https://localhost:8080/trader";
  final Client client;
  final AsyncWebResource resource;

  public RabbitStockServiceGateway() {
    SSLUtilities.trustAllHttpsCertificates();
    TrustManager mytm[] = new TrustManager[]{new SSLUtilities.FakeX509TrustManager()};
    KeyManager mykm[] = null;

    SSLContext context = null;

    try {
      context = SSLContext.getInstance("SSL");
      context.init(mykm, mytm, null);
    } catch (NoSuchAlgorithmException nae) {

    } catch (KeyManagementException kme) {

    }

    HTTPSProperties prop = new HTTPSProperties(new SSLUtilities.FakeHostnameVerifier(), context);
    DefaultClientConfig dcc = new DefaultClientConfig();
    dcc.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, prop);
    client = Client.create(dcc);
    client.addFilter(new LoggingFilter());
    resource = client.asyncResource(TRADER_ENDPOINT);
  }

  @Override
  public Future<TradeResponse> send(TradeRequest tradeRequest) {
    Future<TradeResponse> response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(TradeResponse.class, tradeRequest);
    return response;
  }

}
