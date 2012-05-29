package com.example.amqp;

import org.codehaus.jackson.annotate.JsonProperty;

public class JerseyConfiguration {

  @JsonProperty
  private boolean logTraffic = false;


  public boolean isLogTraffic() {
    return logTraffic;
  }
}
