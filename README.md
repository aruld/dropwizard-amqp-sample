DropWizard AMQP Stock Quote Sample
==================================

git clone https://github.com/aruld/dropwizard-amqp-sample.git
mvn package
java -jar target/dropwizard-amqp-sample-1.0.jar

Uses SSL and Basic Auth.

Using CURL:
POST JSON data and negotiate XML response
curl -k -X POST -d @src/test/resources/tradeRequest.json --user joe:secret https://localhost:8080/trader -H "Content-Type:application/json" -H "Accept: application/xml"

POST XML data and negotiate JSON response
curl -k -X POST -d @src/test/resources/tradeRequest.xml --user joe:secret https://localhost:8080/trader -H "Content-Type:application/xml" -H "Accept: application/json"

Using Spring AMQP Stock sample client UI
java org.springframework.amqp.rabbit.stocks.ui.StockPanel