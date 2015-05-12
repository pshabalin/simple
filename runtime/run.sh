#!/bin/bash
java -Dspring.profiles.active="production" -Dlogback.configurationFile=config/logback.xml -cp lib/*:config Main