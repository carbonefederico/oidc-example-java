package com.pingidentity.oidcapp;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.UUID;

import static com.pingidentity.oidcapp.Configuration.*;
import static java.net.URLEncoder.encode;

public class OIDCClient {

	private final String authorizationEndpoint;
	private final String token_endpoint;
	private final JWTHandler JWTHandler;

	private static OIDCClient oidcClient;

	public static OIDCClient getInstance() {
		if (oidcClient == null) {
			oidcClient = new OIDCClient();
		}
		return oidcClient;
	}

	private OIDCClient() {
		JSONObject configuration = get (CONFIG_URL, null);
		this.authorizationEndpoint = configuration.getString("authorization_endpoint");
		this.token_endpoint = configuration.getString("token_endpoint");
		JWTHandler = new JWTHandler(configuration);
	}

	public String createAuthenticationUrl() {

		String state = UUID.randomUUID().toString();

		return String.format(
				"%s?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s&login_hint=%s&acr_values=%s",
				authorizationEndpoint, CLIENT_ID, encoded(CALLBACK_URL), "code", encoded(SCOPE), encoded(state), encoded(LOGIN_HINT), encoded(ACR_VALUES));
	}

	public User endAuthentication(String code) {
		System.out.println("Callback with code " + code);
		HttpURLConnection connection = null;
		
		try {
		Map<String, String> formData = new HashMap<String, String>();
		formData.put("grant_type", "authorization_code");
		formData.put("code", code);
		formData.put("redirect_uri", CALLBACK_URL);
		
		StringJoiner sj = new StringJoiner("&");
		for(Map.Entry<String,String> entry : formData.entrySet())
		    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" 
		         + URLEncoder.encode(entry.getValue(), "UTF-8"));
		byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
		
		String basicAuthHeader = "Basic " + java.util.Base64.getEncoder().encodeToString((CLIENT_ID + ":" + CLIENT_SECRET).getBytes());
		
		
		URL url = new URL(token_endpoint);
	    connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("POST");
	    connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
	    connection.setRequestProperty("Authorization",basicAuthHeader);
	    
	    connection.setUseCaches(false);
	    connection.setDoOutput(true);

	    //Send request
	    connection.connect();
	    try(OutputStream os = connection.getOutputStream()) {
	        os.write(out);
	    }

	    //Get Response  
	    String result = "";
	    Scanner scanner = new Scanner(connection.getInputStream());
		while (scanner.hasNext()) {
			result += scanner.nextLine();
		}
		
		System.out.println("Exchanged code for tokens " + result);
		
		scanner.close();
		JSONObject json = new JSONObject(result);
		String access_token = json.getString("access_token");
		String id_token = json.getString("id_token");

		return new User(access_token, id_token, JWTHandler.getPayload(id_token));
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	
	private static String encoded(String s) {
		try {
			return encode(s, Charset.forName("UTF-8").name());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public JSONObject get(String urlString, String accessToken) {
		System.out.println("Getting url " + urlString);
		HttpURLConnection conn = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (accessToken != null) {
				System.out.println("Setting token " + accessToken);
				conn.setRequestProperty("Authorization","Bearer " + accessToken);
			}
			
			conn.connect();
			String result = "";
			Scanner scanner = new Scanner(url.openStream());
			while (scanner.hasNext()) {
				result += scanner.nextLine();
			}
			
			System.out.println("Data received " + result);
			scanner.close();
			return new JSONObject(result);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

}
