package com.example.amqp;


import com.fasterxml.jackson.annotation.JsonProperty;

public class JerseyConfiguration {

    @JsonProperty
    private boolean logTraffic = false;


    public boolean isLogTraffic() {
        return logTraffic;
    }
}
