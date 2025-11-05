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
                        git branch: 'main', url: 'https://github.com/your-repo/rate-and-review-service.git'
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
                        echo "🧹 Stopping any old running instance of the service..."
                        bat """
                        for /f "tokens=5" %%a in ('netstat -ano ^| findstr :${PORT}') do (
                            echo Killing PID %%a using port ${PORT}...
                            taskkill /F /PID %%a
                        )
                        timeout /t 5 >nul
                        """
                    } catch (err) {
                        echo "⚠️ Warning: Stop Stage encountered an error or no process found. ${err.getMessage()}"
                    }
                }
            }
        }

        stage('🚚 Deploy New JAR to Target Directory') {
            steps {
                script {
                    try {
                        echo "📦 Deploying the newly built JAR to ${DEPLOY_DIR}..."
                        bat """
                        if not exist "${DEPLOY_DIR}" mkdir "${DEPLOY_DIR}"
                        copy target\\${APP_JAR} "${DEPLOY_DIR}\\${APP_JAR}" /Y
                        """
                    } catch (err) {
                        error("❌ Deployment Failed: Unable to copy JAR file. ${err.getMessage()}")
                    }
                }
            }
        }

        stage('🚀 Start Spring Boot Application') {
            steps {
                script {
                    try {
                        echo "🔥 Starting Spring Boot service on port ${PORT}..."
                        bat """
                        cd "${DEPLOY_DIR}"
                        start /min cmd /c "javaw -jar ${APP_JAR} >> service.log 2>&1"
                        echo Application start command executed successfully.
                        timeout /t 5 >nul
                        """
                    } catch (err) {
                        error("❌ Start Failed: Unable to launch the application. ${err.getMessage()}")
                    }
                }
            }
        }

        stage('🩺 Verify Application Health Status') {
            steps {
                script {
                    def retries = 10
                    def success = false
                    try {
                        echo "🔍 Checking if the application is healthy via Actuator endpoint..."
                        for (int i = 0; i < retries; i++) {
                            def result = bat(script: "curl -s ${HEALTH_URL}", returnStatus: true)
                            if (result == 0) {
                                echo "✅ Application is UP and running!"
                                success = true
                                break
                            } else {
                                echo "⏳ Waiting for service to become healthy... (${i + 1}/${retries})"
                                sleep 5
                            }
                        }
                        if (!success) {
                            error("❌ Health Check Failed: Application did not respond successfully after ${retries * 5} seconds.")
                        }
                    } catch (err) {
                        error("❌ Health Check Failed: ${err.getMessage()}")
                    }
                }
            }
        }

        stage('📜 Display Recent Application Logs') {
            steps {
                script {
                    try {
                        echo "📖 Displaying last 20 lines from service.log for verification..."
                        bat """
                        cd "${DEPLOY_DIR}"
                        powershell -Command "Get-Content service.log -Tail 20"
                        """
                    } catch (err) {
                        echo "⚠️ Warning: Could not read or display service.log file. ${err.getMessage()}"
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
