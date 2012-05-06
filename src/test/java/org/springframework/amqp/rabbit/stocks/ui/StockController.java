package org.springframework.amqp.rabbit.stocks.ui;

import org.springframework.amqp.rabbit.stocks.gateway.StockServiceGateway;
import com.example.amqp.core.TradeRequest;
import com.example.amqp.core.TradeResponse;

import java.util.concurrent.Future;

/**
 * Basic controller for the UI.
 * TODO:  Fix that the UI can receive events before it's panel has been initialized.
 *
 * @author Mark Pollack
 * @author Mark Fisher
 */
public class StockController {

  private StockPanel stockPanel;

  private StockServiceGateway stockServiceGateway;


  public StockPanel getStockPanel() {
    return stockPanel;
  }

  public void setStockPanel(StockPanel stockPanel) {
    this.stockPanel = stockPanel;
  }

  public StockServiceGateway getStockServiceGateway() {
    return stockServiceGateway;
  }

  public void setStockServiceGateway(StockServiceGateway stockServiceGateway) {
    this.stockServiceGateway = stockServiceGateway;
  }

  // "Actions"

  public Future<TradeResponse> sendTradeRequest(String text) {
    String[] tokens = text.split("\\s");
    String quantityString = tokens[0];
    String ticker = tokens[1];
    int quantity = Integer.parseInt(quantityString);
    TradeRequest tr = new TradeRequest();
    tr.setAccountName("ACCT-123");
    tr.setBuyRequest(true);
    tr.setOrderType("MARKET");
    tr.setTicker(ticker);
    tr.setQuantity(quantity);
    tr.setRequestId("REQ-1");
    tr.setUserName("Joe Trader");
    tr.setUserName("Joe");
    return stockServiceGateway.send(tr);
  }

}