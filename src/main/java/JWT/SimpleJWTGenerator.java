package JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleJWTGenerator {
    private static final String SECRET_KEY = "2c0b5348ad203f431af4e23e0c91066c95943b2047ab2bcb615d0b25ef68f23b"; // **Replace with a STRONGER, randomly generated key in production!**
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour in milliseconds

    public static String generateToken(String username) throws IOException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username); // Add any other claims you need

        ObjectMapper objectMapper = new ObjectMapper();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    public static void main(String[] args) throws IOException {
        String token = generateToken("john.doe");
        System.out.println("Generated JWT Token: " + token);
    }
}
