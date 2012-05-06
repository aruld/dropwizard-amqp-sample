package org.springframework.amqp.rabbit.stocks.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.util.concurrent.Future;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.springframework.amqp.rabbit.stocks.gateway.RabbitStockServiceGateway;
import org.springframework.amqp.rabbit.stocks.gateway.StockServiceGateway;
import com.example.amqp.core.TradeResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A typical poor mans UI to drive the application.
 *
 * @author Mark Pollack
 * @author Mark Fisher
 */
@SuppressWarnings("serial")
public class StockPanel extends JPanel {

  private static Log log = LogFactory.getLog(StockPanel.class);

  private JTextField tradeRequestTextField;
  private JButton tradeRequestButton;
  private JTextArea marketDataTextArea;
  private StockController stockController;

  private DecimalFormat frmt = new DecimalFormat("$0.00");

  public StockPanel(StockController controller) {
    this.stockController = controller;
    controller.setStockPanel(this);
    this.setBorder(BorderFactory.createTitledBorder("Stock Form"));

    FormLayout formLayout = new FormLayout("pref, 150dlu", // columns
        "pref, fill:100dlu:grow"); // rows
    setLayout(formLayout);
    CellConstraints c = new CellConstraints();

    tradeRequestButton = new JButton("Send Trade Request");
    add(tradeRequestButton, c.xy(1, 1));

    tradeRequestTextField = new JTextField("");
    add(tradeRequestTextField, c.xy(2, 1));

    add(new JLabel("Trade Response"), c.xy(1, 2));

    marketDataTextArea = new JTextArea();
    JScrollPane sp = new JScrollPane(marketDataTextArea);
    sp.setSize(200, 300);

    add(sp, c.xy(2, 2));

    tradeRequestTextField.addFocusListener(new FocusListener() {
      public void focusLost(FocusEvent e) {
      }

      public void focusGained(FocusEvent e) {
        tradeRequestTextField.setText("");
        tradeRequestTextField.setForeground(Color.BLACK);
      }
    });

    tradeRequestButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        sendTradeRequest();
      }
    });
  }

  private void sendTradeRequest() {
    try {
      Future<TradeResponse> future = stockController.sendTradeRequest(tradeRequestTextField.getText());
      tradeRequestTextField.setForeground(Color.GRAY);
      log.info("Sent trade request.");
      while (!future.isDone()) {
        Thread.sleep(2000);
      }
      log.info("Received trade response.");
      update(future.get());
    } catch (Exception ex) {
      tradeRequestTextField.setForeground(Color.RED);
      tradeRequestTextField.setText("Required Format: 100 TCKR");
    }
  }

  public static void main(String[] a) {
    StockServiceGateway stockServiceGateway = new RabbitStockServiceGateway();
    StockController controller = new StockController();
    controller.setStockServiceGateway(stockServiceGateway);
    JFrame f = new JFrame("DropWizard AMQP Stock Demo");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.add(new StockPanel(controller));
    f.pack();
    f.setVisible(true);
  }

  public void update(final TradeResponse tradeResponse) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        marketDataTextArea.setForeground(Color.GREEN);
        marketDataTextArea.append("Confirmed. "
            + tradeResponse.getTicker() + " "
            + frmt.format(tradeResponse.getPrice().doubleValue()));
      }
    });
  }

}