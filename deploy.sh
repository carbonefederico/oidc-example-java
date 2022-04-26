mvn clean package
scp -i /Volumes/GoogleDrive/My\ Drive/keys/TCS-P1AS-POC.pem target/openid-connect-example-java-1.0.war ec2-user@tcs-sample-app.ping-eng.com:/home/ec2-user/apache-tomcat-9.0.48/webapps/openid-connect-example-java.war


