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

                        echo Deploying new JAR to "%DEPLOY_DIR%"...
                        if not exist "%DEPLOY_DIR%" mkdir "%DEPLOY_DIR%"

                        if exist "target\\%APP_JAR%" (
                            copy "target\\%APP_JAR%" "%DEPLOY_DIR%\\%APP_JAR%" /Y
                            echo ✅ New JAR copied successfully.
                        ) else (
                            echo ❌ ERROR: JAR not found in target folder!
                            exit /b 1
                        )

                        cd "%DEPLOY_DIR%"
                        echo 🟢 Starting Spring Boot service...
                        cmd /c "start /B javaw -jar %APP_JAR% >> service.log 2>&1"
                        echo ✅ Application launch command executed.

                        ping -n 6 127.0.0.1 >nul
                        endlocal
                        """
                    } catch (err) {
                        error("❌ Deployment or Startup Failed: ${err.getMessage()}")
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
