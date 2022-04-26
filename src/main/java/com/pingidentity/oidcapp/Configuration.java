package com.pingidentity.oidcapp;

class Configuration {

    public static final String CONFIG_URL = "https://tcs-pf-engine.ping-eng.com/.well-known/openid-configuration";
    public static final String CALLBACK_URL = "https://tcs-sample-app.ping-eng.com/openid-connect-example-java/callback";
    
    public static final String CLIENT_ID = "<CHANGE_ME>";
    public static final String CLIENT_SECRET = "<CHANGE_ME>";
    
    public static final String SCOPE = "openid profile"; 
    public static final String LOGIN_HINT = "";
    public static final String ACR_VALUES = ""; 

}
