package ee.silves.veebipood.model;

import lombok.Data;

@Data
public class AuthToken {
    private String token;
    private long expiration;
}
