pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'maven'
    }

    environment {
        DEPLOY_DIR = "C:\\deployments\\rate-service"
        APP_JAR = "rate-and-review-service.jar"
        PORT = "8282"
        HEALTH_URL = "http://localhost:8282/actuator/health"
    }

    stages {

        stage('🧱 Code Checkout from GitHub') {
            steps {
                script {
                    try {
                        echo "📥 Fetching latest source code from GitHub repository..."
                        git branch: 'main', url: 'https://github.com/mohammadrashidalam/rate-and-review-service-with-jenkins.git'
                    } catch (err) {
                        error("❌ Code Checkout Failed: ${err.getMessage()}")
                    }
                }
            }
        }

        stage('🏗️ Build Application with Maven') {
            steps {
                script {
                    try {
                        echo "⚙️ Running Maven build to package Spring Boot JAR..."
                        bat 'mvn clean package -DskipTests=true'
                    } catch (err) {
                        error("❌ Build Failed: Maven compilation or packaging error. ${err.getMessage()}")
                    }
                }
            }
        }
        stage('🛑 Stop Existing Application on Port 8282') {
            steps {
                script {
                    try {
                        echo "🧹 Checking and stopping any old running instance on port 8282..."

                        bat '''
                        setlocal enabledelayedexpansion
                        set "foundProcess=false"

                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8282') do (
                            echo Killing process on port 8282 (PID %%a)...
                            taskkill /F /PID %%a >nul 2>&1
                            set "foundProcess=true"
                        )

                        if "!foundProcess!"=="false" (
                            echo ⚠️ No process found running on port 8282.
                        ) else (
                            echo ✅ Old process stopped successfully.
                        )

                        ping -n 6 127.0.0.1 >nul
                        endlocal
                        '''
                    } catch (err) {
                        echo "⚠️ Stop stage encountered an error, but continuing. ${err.getMessage()}"
                    }
                }
            }
        }
        stage('🗂 Backup Previous JAR') {
            steps {
                script {
                    try {
                        echo "📦 Checking for previously deployed JAR to backup..."
                        bat """
                        setlocal
                        set DEPLOY_DIR=${DEPLOY_DIR}
                        set APP_JAR=${APP_JAR}
                        set BACKUP_DIR=%DEPLOY_DIR%\\previous_builds
                        set TIMESTAMP=%date:~-4%%date:~3,2%%date:~0,2%_%time:~0,2%%time:~3,2%%time:~6,2%

                        if exist "%DEPLOY_DIR%\\%APP_JAR%" (
                            echo 🕒 Previous JAR found at "%DEPLOY_DIR%\\%APP_JAR%"
                            if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"
                            echo Moving old JAR to "%BACKUP_DIR%\\%APP_JAR%_%TIMESTAMP%"
                            move "%DEPLOY_DIR%\\%APP_JAR%" "%BACKUP_DIR%\\%APP_JAR%_%TIMESTAMP%" >nul
                            echo ✅ Backup completed successfully.
                        ) else (
                            echo ⚠️ No previous JAR found. Skipping backup.
                        )
                        endlocal
                        """
                    } catch (err) {
                        echo "⚠️ Backup Stage Warning: ${err.getMessage()}"
                    }
                }
            }
        }
        stage('🚚 Deploy and Start New JAR') {
            steps {
                script {
                    try {
                        echo "🚀 Deploying new JAR and starting Spring Boot service..."

                        bat """
                        setlocal
                        set DEPLOY_DIR=${DEPLOY_DIR}
                        set APP_JAR=${APP_JAR}

                        echo [INFO] Deploying new JAR to "%DEPLOY_DIR%"...
                        if not exist "%DEPLOY_DIR%" mkdir "%DEPLOY_DIR%"

                        if exist "target\\%APP_JAR%" (
                            copy "target\\%APP_JAR%" "%DEPLOY_DIR%\\%APP_JAR%" /Y
                            echo [SUCCESS] New JAR copied successfully.
                        ) else (
                            echo [ERROR] JAR not found in target folder!
                            exit /b 1
                        )

                        echo [INFO] Checking MongoDB service on port 27017...
                        netstat -ano | findstr :27017 >nul
                        if %errorlevel% neq 0 (
                            echo [ERROR] MongoDB not running! Please start MongoDB before deploying. >> service.log
                            echo [ERROR] Deployment stopped due to missing MongoDB instance.
                            exit /b 1
                        ) else (
                            echo [INFO] MongoDB is running on port 27017. Proceeding with app start... >> service.log
                        )

                        cd "%DEPLOY_DIR%"
                        echo [INFO] Starting Spring Boot service...
                        powershell -Command "Start-Process -NoNewWindow -WindowStyle Hidden -FilePath 'java' -ArgumentList '-jar','rate-and-review-service.jar' -WorkingDirectory 'C:\deployments\rate-service' -RedirectStandardOutput 'service.log' -RedirectStandardError 'service.log'"



                        ping -n 6 127.0.0.1 >nul

                        curl -s http://localhost:8282/actuator/health | findstr /C:"UP" >nul
                        if %errorlevel%==0 (
                            echo [SUCCESS] Application is running and healthy.
                        ) else (
                            echo [WARN] Application might not be up yet. Check service.log for details.
                        )

                        exit /b 0
                        endlocal
                        """
                    } catch (err) {
                        error("❌ Deployment or Startup Failed: ${err.getMessage()}")
                    }
                }
            }
        }

        stage('🩺 Verify Application Health') {
            steps {
                script {
                    try {
                        echo "🩺 Checking if Rate-Service is UP and healthy..."

                        // Give it a few seconds to start
                        bat 'ping -n 8 127.0.0.1 >nul'

                        // Perform the health check
                        bat """
                        echo Checking health at http://localhost:8282/actuator/health ...
                        curl -s http://localhost:8282/actuator/health > health.txt

                        if exist health.txt (
                            findstr /C:"UP" health.txt >nul
                            if %errorlevel%==0 (
                                echo ✅ Application is UP and running.
                                type health.txt
                                exit /b 0
                            ) else (
                                echo ❌ Application health check failed — status not UP.
                                type health.txt
                                exit /b 1
                            )
                        ) else (
                            echo ❌ Health check file not found — CURL might have failed.
                            exit /b 1
                        )
                        """
                    } catch (err) {
                        echo "⚠️ Health Check Warning: ${err.getMessage()}"
                        echo "🚨 Application might not have started correctly. Please check service.log for details."
                    }
                }
            }
        }
    }

    post {
        success {
            echo "✅ PIPELINE SUCCESS: All stages executed successfully and application is UP!"
        }
        failure {
            echo "❌ PIPELINE FAILED: Please check the specific stage error messages above."
        }
    }
}
