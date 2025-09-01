#!/bin/bash
set -e
echo 'Make sure you have Playwright browsers installed: mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"'
mvn -q -DskipTests package
java -jar target/market-intel-java-0.1.0.jar
