# Base image
FROM openjdk:19-jdk-alpine

# Maintainer
LABEL maintainer="Mineur Detenu CGPR"

# Set environment variables
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

# Create tomcat user
#RUN addgroup -S tomcat && adduser -S tomcat -G tomcat

# Download and install Tomcat
RUN set -eux; \
  mkdir -p "$CATALINA_HOME"; \
  cd /tmp; \
  wget -q "https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.71/bin/apache-tomcat-9.0.71.tar.gz"; \
  tar -xzf apache-tomcat-9.0.71.tar.gz -C "$CATALINA_HOME" --strip-components=1; 
#  rm apache-tomcat-9.0.71.tar.gz; 
#  chmod +x -R "$CATALINA_HOME/bin"; \
#  chown -R tomcat:tomcat "$CATALINA_HOME"

# Set the environment variables for JMX and maximum heap size
# ENV CATALINA_OPTS -Xmx3g -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9090 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false

# Copy the .war file to the Tomcat webapps directory
COPY /target/mineur-0.0.1-SNAPSHOT.war $CATALINA_HOME/webapps/mineur.war

# Set the working directory
WORKDIR $CATALINA_HOME

# Expose tomcat port
EXPOSE 8080

# Start tomcat
CMD ["catalina.sh", "run"]