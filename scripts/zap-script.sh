#!/bin/bash

docker pull owasp/zap2docker-stable
docker run -i owasp/zap2docker-stable zap-baseline.py -t "https://github.com/NUS-ISS-Projects/flight-backend/" -l PASS -J "-Xmx512m" -r zap_baseline_report.html

echo $? > /dev/null
