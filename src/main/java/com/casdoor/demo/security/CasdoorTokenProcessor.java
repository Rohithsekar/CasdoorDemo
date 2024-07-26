//package com.casdoor.demo.security;
//
//import com.casdoor.demo.constants.Constants;
//import com.nimbusds.jose.*;
//import com.nimbusds.jose.crypto.RSASSASigner;
//import com.nimbusds.jwt.JWTClaimsSet;
//import com.nimbusds.jwt.SignedJWT;
//import io.jsonwebtoken.JwtException;
//import lombok.extern.slf4j.Slf4j;
//import org.bouncycastle.asn1.ASN1Primitive;
//import org.bouncycastle.util.io.pem.PemObject;
//import org.bouncycastle.util.io.pem.PemReader;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.Resource;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StreamUtils;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.nio.charset.StandardCharsets;
//import java.security.KeyFactory;
//import java.security.NoSuchAlgorithmException;
//import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
//
//import java.security.PrivateKey;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.RSAPrivateKeySpec;
//import java.util.Base64;
//import java.util.Date;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class CasdoorTokenProcessor {
//
//    private final NimbusJwtDecoder jwtDecoder;
//
//    private final RSAPrivateKey rsaPrivateKey;
//
//    @Value("classpath:certs/private_key.key")
//    private Resource privateKeyResource;
//
//    public CasdoorTokenProcessor(NimbusJwtDecoder nimbusJwtDecoder,
//                                 RSAPrivateKey rsaPrivateKey) {
//        this.jwtDecoder = nimbusJwtDecoder;
//        this.rsaPrivateKey = rsaPrivateKey;
//    }
//
//    public Map<String, Object> extractPayloadInfo(String jwtToken) throws JwtException {
//
//        try {
//            Jwt decodedJwt = jwtDecoder.decode(jwtToken);
//
//            log.info(String.format(" decoded Jwt token breakdown: "));
//            log.info(String.format("ExpiresAt: %ld", decodedJwt.getExpiresAt()));
//            log.info(String.format("IssuedAt: %ld", decodedJwt.getIssuedAt()));
//            log.info(String.format("Issuer: %s", decodedJwt.getIssuer()));
//            log.info(String.format("Audience : %s", decodedJwt.getAudience()));
//            log.info(String.format("Headers is %s", decodedJwt.getHeaders()));
//            log.info(String.format("Id is %s", decodedJwt.getId()));
//
//            return decodedJwt.getClaims();
//        } catch (org.springframework.security.oauth2.jwt.JwtException e) {
//            String methodName = new Object() {
//            }.getClass().getEnclosingMethod().getName();
//            log.error(Constants.returnErrorTemplate(e, methodName, e.getMessage()));
//            throw new RuntimeException();
//        }
//    }
//
//    public String generateToken(Map<String, Object> claims, String subject, String issuer, String audience, Date expiration) {
//
//        try {
////
////            String key = StreamUtils.copyToString(privateKeyResource.getInputStream(), StandardCharsets.UTF_8);
////            key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
////                    .replace("-----END RSA PRIVATE KEY-----", "")
////                    .replaceAll("\\s", "");
////            byte[] keyBytes = Base64.getDecoder().decode(key);
////            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
////            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
////            RSAPrivateKey rsaPrivateKey =  (RSAPrivateKey) keyFactory.generatePrivate(spec);
//
//            String key = StreamUtils.copyToString(privateKeyResource.getInputStream(), StandardCharsets.UTF_8);
//            key = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
//                    .replace("-----END RSA PRIVATE KEY-----", "")
//                    .replaceAll("\\s", "");
//            byte[] keyBytes = Base64.getDecoder().decode(key);
//
//            try (PemReader pemReader = new PemReader(new StringReader(key))) {
//                PemObject pemObject = pemReader.readPemObject();
//                byte[] content = pemObject.getContent();
//                org.bouncycastle.asn1.pkcs.RSAPrivateKey asn1Key = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(ASN1Primitive.fromByteArray(content));
//
//                RSAPrivateKeySpec spec = new RSAPrivateKeySpec(
//                        asn1Key.getModulus(),
//                        asn1Key.getPrivateExponent()
//                );
//                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                PrivateKey rsaPrivateKey =  keyFactory.generatePrivate(spec);
//
//                JWSSigner signer = new RSASSASigner(rsaPrivateKey);
//
//                // Prepare JWT with claims set
//                JWTClaimsSet.Builder claimsSet = new JWTClaimsSet.Builder()
//                        .subject(subject)
//                        .issuer(issuer)
//                        .audience(audience)
//                        .expirationTime(expiration)
//                        .issueTime(new Date())
//                        .jwtID(claims.get("jti").toString());
//
//                // Add additional claims
//                claims.forEach(claimsSet::claim);
//
//                SignedJWT signedJWT = new SignedJWT(
//                        new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(),
//                        claimsSet.build()
//                );
//
//                // Compute the RSA signature
//                signedJWT.sign(signer);
//
//                // Serialize to compact form, produces something like
//                // eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJodHRwczovL2Rldi1hdXRoLnlhYW50cmFjLmNvbSIsIm5hbWUiOiJKb2huIERvZSIsImV4cCI6MTg2MDAxNjM2MiwianRpIjoiZWIxZmE1YmItZTc1MC00ZTlhLWE0OWYtZDIwNmE4MmZmOWM0Iiwic3ViIjoiMTIzNDU2Nzg5MCJ9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ
//                return signedJWT.serialize();
//            } catch (JOSEException e) {
//                String methodName = new Object() {
//                }.getClass().getEnclosingMethod().getName();
//                log.error(Constants.returnErrorTemplate(e, methodName, e.getMessage()));
//                throw new RuntimeException();
//            } catch (NoSuchAlgorithmException e) {
//                String methodName = new Object() {
//                }.getClass().getEnclosingMethod().getName();
//                log.error(Constants.returnErrorTemplate(e, methodName, e.getMessage()));
//                throw new RuntimeException();
//            } catch (IOException e) {
//                String methodName = new Object() {
//                }.getClass().getEnclosingMethod().getName();
//                log.error(Constants.returnErrorTemplate(e, methodName, e.getMessage()));
//                throw new RuntimeException();
//            } catch (InvalidKeySpecException e) {
//                String methodName = new Object() {
//                }.getClass().getEnclosingMethod().getName();
//                log.error(Constants.returnErrorTemplate(e, methodName, e.getMessage()));
//                throw new RuntimeException();
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException();
//        }
//    }
//}
