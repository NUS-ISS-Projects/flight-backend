#!/bin/bash

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Navigate to the Spring Boot project directory
cd /home/luckilystar08/flight-backend

# Check if JAVA_HOME is set correctly
if [ -z "$JAVA_HOME" ]; then
    echo "The JAVA_HOME environment variable is not set. This variable is needed to run this program."
    exit 1
fi

# Use Maven to start the Spring Boot application
mvn spring-boot:run
