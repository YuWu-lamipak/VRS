@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-1.8"
set "PATH=%JAVA_HOME%\bin;%PATH%"
cd VRS\lemei
D:\maven\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run -pl lemei-api
