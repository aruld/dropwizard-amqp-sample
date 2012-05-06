package com.example.amqp;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Copyright 2000-2012 Flux Corporation. All rights reserved.
 */
public class JerseyConfiguration {

  @JsonProperty
  private boolean logTraffic = false;


  public boolean isLogTraffic() {
    return logTraffic;
  }
}
