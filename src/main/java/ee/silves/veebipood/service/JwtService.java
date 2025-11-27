package ee.silves.veebipood.service;

import ee.silves.veebipood.entity.Person;
import ee.silves.veebipood.model.AuthToken;
import ee.silves.veebipood.model.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JwtService {

    Key superSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("j7WASmk4DbVREs4ZSW4sjyJzpeq5B9gmTrkx1pofC6U"));

    public AuthToken generateToken(Person person) {
    //                                                              h    m    s    ms
        Date expirationDate = new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000);

        String token = Jwts
                .builder()
                .signWith(superSecretKey)
                .setId(person.getId().toString())
                .setSubject(person.getEmail())
                .setAudience(person.getRole().toString())
                .setExpiration(expirationDate) // päriselt tokenis --> automaatika, viskab errori kui aegunud
                .compact();

        AuthToken authToken = new AuthToken();
        authToken.setToken(token);
        authToken.setExpiration(expirationDate.getTime()); // frontendi jaoks üleliigsete päringute vältimiseks
        return authToken;
    }

    public TokenData parseToken(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(superSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        TokenData tokenData = new TokenData();
        tokenData.setId(Long.parseLong(claims.getId()));
        tokenData.setEmail(claims.getSubject());
        tokenData.setRole(claims.getAudience());

        return tokenData;
    }
}
