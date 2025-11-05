pipeline {
    agent any

    tools {
        maven 'maven'      // Must match the Maven name in Global Tool Configuration
        jdk 'JDK17'        // Must match the JDK name in Global Tool Configuration
    }

    environment {
        APP_NAME = "rate-service"
        DEPLOY_DIR = "C:\\deployments\\rate-service"
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "📥 Pulling latest code from Git..."
                git branch: 'main', url: 'https://github.com/mohammadrashidalam/rate-and-review-service-with-jenkins.git'
            }
        }

        stage('Build') {
            steps {
                echo "🏗️ Building the application..."
                bat 'mvn clean package -DskipTests=true'
                 echo "🧪 Finish building the application..."
            }
        }

        stage('Test') {
            steps {
                echo "🧪 Running unit tests script..."
                bat 'mvn test'
                 echo "🧪 Finish unit tests script..."
            }
        }

        stage('Deploy') {
            steps {
                echo "🚀 Deploying Rate-Service..."

                        // Stop old app safely
                        bat '"%DEPLOY_DIR%\\stop.bat"'

                        // Copy new JAR
                        bat """
                        echo Copying new JAR file...
                        if not exist "%DEPLOY_DIR%" mkdir "%DEPLOY_DIR%"
                        copy target\\rate-service.jar "%DEPLOY_DIR%" /Y
                        """

                        // Start new version
                        bat """
                        echo Starting new Rate-Service application...
                        cd "%DEPLOY_DIR%"
                        start java -jar rate-service.jar
                        """
            }
        }
    }

    post {
        success {
            echo "✅ Build and Deployment successful!"
        }
        failure {
            echo "❌ Build or Deployment failed!"
        }
    }
}