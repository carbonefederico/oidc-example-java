package com.pingidentity.oidcapp;

import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.json.JSONObject;

class JWTHandler {

	private final HttpsJwks httpsJkws;
	private final String issuer; 
	
	public JWTHandler(JSONObject configuration) {
		httpsJkws = new HttpsJwks(configuration.getString("jwks_uri"));
		issuer = configuration.getString("issuer");
	}

	public JwtClaims getPayload(String jwt) {

		HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);
		JwtConsumer jwtConsumer = new JwtConsumerBuilder().
				setVerificationKeyResolver(httpsJwksKeyResolver).
				setExpectedAudience(Configuration.CLIENT_ID).
				setExpectedIssuer(issuer).
				build();
		try {
			JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
			return jwtClaims;
		} catch (InvalidJwtException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
