package com.BudgetTracking.UserService.Service;

import com.BudgetTracking.UserService.Model.User;
import com.BudgetTracking.UserService.repository.TokenRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECERT_KEY ="4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";
    private final TokenRepository tokenRepository;

    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }


    public boolean isValid(String token, UserDetails user) {
        String email = extractUsername(token);

        boolean isValidToken = tokenRepository.findByToken(token)
                .map(t-> !t.isLoggedOut()).orElse(false);



        return (email.equals(user.getUsername())) && !isTokenExpired(token) && !isValidToken ;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String generateToken (User user){
        String token = Jwts
                .builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+24*60*60*1000)  )
                .signWith(getSignKey())
                .compact();
        return token;
    }
    private SecretKey getSignKey(){
        byte[] keyBytes =Decoders.BASE64URL.decode(SECERT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
