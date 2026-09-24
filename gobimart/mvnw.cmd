@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------
@IF "%DEBUG%" == "" @ECHO OFF
@SETLOCAL
@SET ERROR_CODE=0

@SET MAVEN_CMD_LINE_ARGS=%*

@SET "DIRNAME=%~dp0"
@IF "%DIRNAME%" == "" SET "DIRNAME=."
@SET "APP_BASEDIR=%DIRNAME%"

@SET "WRAPPER_JAR=%APP_BASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

@REM Look for installed mvn if wrapper jar is missing, or run via mvn
where mvn >nul 2>nul
@IF %ERRORLEVEL% EQU 0 (
  mvn %MAVEN_CMD_LINE_ARGS%
  goto end
)

@IF EXIST "%WRAPPER_JAR%" (
    goto runWrapper
)

@echo Maven is not found in PATH and wrapper jar is missing.
@echo Please install Maven 3.8+ or run with an existing maven installation.
@set ERROR_CODE=1
@goto end

:runWrapper
java -jar "%WRAPPER_JAR%" %MAVEN_CMD_LINE_ARGS%

:end
@exit /B %ERROR_CODE%
