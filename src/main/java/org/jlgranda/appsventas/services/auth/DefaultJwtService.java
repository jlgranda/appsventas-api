package org.jlgranda.appsventas.services.auth;

import org.jlgranda.appsventas.exception.NoAuthorizationException;
import org.jlgranda.appsventas.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.Charset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;
import org.apache.commons.codec.binary.Base64;
import org.jlgranda.appsventas.domain.Subject;
import org.springframework.http.HttpHeaders;

@Component
public class DefaultJwtService implements JwtService {
    private String secret;
    private int sessionTime;
    
    @Autowired
    public DefaultJwtService() {
        this.secret = "SECRET";
        this.sessionTime = 30;
    }

    @Override
    public String toToken(Subject user, String password) {
        
        return Jwts.builder()
            .setSubject("" + user.getId())
            .setExpiration(expireTimeFromNow())
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    @Override
    public Optional<String> getSubFromToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return Optional.ofNullable(claimsJws.getBody().getSubject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Date expireTimeFromNow() {
        return new Date(System.currentTimeMillis() + sessionTime * 1000);
    }
    
    @Override
    public String toToken(Subject user) {
        return Jwts.builder()
            .setSubject("" + user.getId())
            .setExpiration(expireTimeFromNow())
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
    
    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    @Override
    public String toToken(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toToken(String username, String password, String serviceUsername, String servicePassword) throws NoAuthorizationException {
          throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
