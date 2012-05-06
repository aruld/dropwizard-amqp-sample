package org.springframework.amqp.rabbit.stocks.gateway;

import com.example.amqp.core.TradeRequest;
import com.example.amqp.core.TradeResponse;

import java.util.concurrent.Future;

/**
 * Gateway interface that sends trades to an external process.
 * 
 * @author Mark Pollack
 */
public interface StockServiceGateway {

	Future<TradeResponse> send(TradeRequest tradeRequest);

}
