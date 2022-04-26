# Simple OpenID Connect Java Application

This is a Java based test application for authenticating a user with the OpenID Connect Authorization Code Flow

## Process flow

The authentication goes through the following steps


1. the application fetches the OIDC configuration from the well known endpoint .well-known/openid-configuration
2. the application prepares an Authentication Request containing the desired request parameters and redirects the user to the Authorization Server
3. the Authorization Server authenticates the user and redirects the user back to the application callback URL with an Authorization Code
4. the application exchanges the Authorization Code for an Access Token and ID Token
5. the application validates the ID Token and creates a session


## Build

To be able to run the application you will need to edit the parameters in `Configuration.java`, compile with [Maven](https://maven.apache.org) to obtain a war file and deploy ot an application container

```
mvn clean install

```
