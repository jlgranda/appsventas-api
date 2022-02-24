package org.jlgranda.appsventas.services;

import org.springframework.stereotype.Service;

import java.util.Optional;
import org.jlgranda.appsventas.domain.Subject;
import org.springframework.web.client.HttpClientErrorException;

@Service
public interface JwtService {
    
    String toToken(Subject user);
    
    String toToken(Subject user, String password);
    
    String toToken(String username, String password);
    
    String toToken(String username, String password, String serviceUsername, String servicePassword) throws  HttpClientErrorException;

    Optional<String> getSubFromToken(String token);
}
