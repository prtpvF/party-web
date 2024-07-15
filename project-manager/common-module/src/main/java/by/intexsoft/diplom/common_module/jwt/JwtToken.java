package by.intexsoft.diplom.common_module.jwt;

import by.intexsoft.diplom.auth.dto.JwtResponseDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RefreshScope
public class JwtToken {
    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        return JWT.create().withSubject("User details").withClaim("username", username)
                .withIssuedAt(new Date()).withIssuer("free-party").withExpiresAt(expirationDate).sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("free-party")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}