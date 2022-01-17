package com.maxzap.bserv.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultTokenService implements TokenService {

    @Value("${auth.jwt.secret}")
    private String secretKey;

    /*Проверка валидности токена*/
    @Override
    public boolean checkToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);

            if (!decodedJWT.getIssuer().equals("auth-service")) {
                log.error("issuer is incorrect");
                return false;
            }

            if (!decodedJWT.getAudience().contains("bookStore")) {
                log.error("Audience is incorrect");
                return false;
            }

        } catch (JWTVerificationException exception) {
            log.error("token is invalid");
            return false;
        }
        return true;
    }
}
