pipeline {
    agent any

    tools {
        maven 'maven'     // Configure Maven in Jenkins (Manage Jenkins → Global Tool Configuration)
        jdk 'JDK17'            // Configure JDK in Jenkins with this name
    }

    environment {
        APP_NAME = "rate-service"
        DEPLOY_DIR = "C:\\deployments\\rate-service"  // Change this to your deploy folder
    }

    stages {

        stage('Checkout Code') {
            steps {
                echo "Pulling latest code from Git..."
                git branch: 'main', url: 'https://github.com/mohammadrashidalam/rate-and-review-service-with-jenkins.git'
            }
        }

        stage('Build') {
            steps {
                echo "Building the application..."
                bat 'mvn clean package -DskipTests=true'
            }
        }

        stage('Test') {
            steps {
                echo "Running unit tests..."
                bat 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying application..."
                 // Stop only the old Rate-Service running on port 8282
                bat '''
                echo Stopping old service if running...
                 for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8282') do (
                            echo Killing process on port 8282 (PID %%a)...
                            taskkill /F /PID %%a
                        ) || echo No old process found
                '''

                // Copy the new jar file to deploy directory
                bat '''
                echo Copying new jar file...
                if not exist "%DEPLOY_DIR%" mkdir "%DEPLOY_DIR%"
                copy target\\rate-service.jar "%DEPLOY_DIR%"
                '''

                // Start the new version
                bat '''
                echo Starting new service...
                start java -jar "%DEPLOY_DIR%\\rate-service.jar"
                '''
            }
        }
    }

    post {
        success {
            echo "✅ Deployment successful!"
        }
        failure {
            echo "❌ Build or Deployment failed!"
        }
    }
}
