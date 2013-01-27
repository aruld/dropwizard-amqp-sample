package com.example.amqp;

import com.example.amqp.core.User;
import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

public class DumbAuthenticator implements Authenticator<BasicCredentials, User> {
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword())) {
            User user = new User();
            user.setUserName(credentials.getUsername());
            return Optional.of(user);
        }
        return Optional.absent();
    }
}
