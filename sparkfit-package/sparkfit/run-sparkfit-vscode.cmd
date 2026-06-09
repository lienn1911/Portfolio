@echo off
setlocal

set "JAVA_HOME=C:\Program Files\Java\jdk-22"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Using JAVA_HOME=%JAVA_HOME%
echo Starting SparkFit with Maven Wrapper...

call "%~dp0mvnw.cmd" clean javafx:run

pause
