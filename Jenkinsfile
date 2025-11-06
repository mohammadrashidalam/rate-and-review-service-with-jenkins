pipeline {
    agent any

    tools {
        jdk 'JDK17'
        maven 'maven'
    }

    environment {
        DEPLOY_DIR = "E:\\Practice\\Jenkins\\deployments\\rate-service"
        APP_JAR = "rate-and-review-service.jar"
        PORT = "8282"
        HEALTH_URL = "http://localhost:8282/actuator/health"
    }

    stages {

        stage(' Code Checkout from GitHub') {
            steps {
                script {
                    try {
                        echo " Fetching latest source code from GitHub repository..."
                        git branch: 'main', url: 'https://github.com/mohammadrashidalam/rate-and-review-service-with-jenkins.git'
                    } catch (err) {
                        error(" Code Checkout Failed: ${err.getMessage()}")
                    }
                }
            }
        }

        stage(' Build Application with Maven') {
            steps {
                script {
                    try {
                        echo " Running Maven build to package Spring Boot JAR..."
                        bat 'mvn clean package -DskipTests=true'
                    } catch (err) {
                        error(" Build Failed: Maven compilation or packaging error. ${err.getMessage()}")
                    }
                }
            }
        }
         stage('Test') {
            steps {
                echo " Running unit tests script..."
                bat 'mvn test'
                 echo " Finish unit tests script..."
            }
        }
       stage('Stop Existing Application on Port 8282') {
           steps {
               script {
                   try {
                       echo "Checking and stopping any old running instance on port 8282..."

                       powershell '''
                       $processes = netstat -ano | Select-String ":8282"
                       if ($processes) {
                           foreach ($line in $processes) {
                               $procId = ($line.ToString().Split()[-1])
                               Write-Output "Killing process on port 8282 (PID $procId)..."
                               Stop-Process -Id $procId -Force
                           }
                           Write-Output "Old process stopped successfully on port 8282."
                       } else {
                           Write-Output "No process found running on port 8282."
                       }
                       '''
                   } catch (err) {
                       echo "Stop stage encountered an error, but continuing. ${err.getMessage()}"
                   }
               }
           }
       }

       stage('Backup Previous JAR') {
           steps {
               script {
                   try {
                       echo "Checking for previously deployed JAR to backup..."

                       bat """
                       @echo off
                       setlocal

                       rem Deployment directory and JAR name come from pipeline environment variables
                       set "DEPLOY_DIR=${DEPLOY_DIR}"
                       set "APP_JAR=${APP_JAR}"
                       set "BACKUP_DIR=%DEPLOY_DIR%\\previous_builds"

                       rem Build timestamp (YYYYMMDD_HHMMSS)
                       set "TIMESTAMP=%date:~-4%%date:~3,2%%date:~0,2%_%time:~0,2%%time:~3,2%%time:~6,2%"

                       if exist "%DEPLOY_DIR%\\%APP_JAR%" (
                           echo Previous JAR found at "%DEPLOY_DIR%\\%APP_JAR%"
                           if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"
                           echo Moving old JAR to "%BACKUP_DIR%\\%APP_JAR%_%TIMESTAMP%"
                           move "%DEPLOY_DIR%\\%APP_JAR%" "%BACKUP_DIR%\\%APP_JAR%_%TIMESTAMP%" >nul
                           echo Backup completed successfully.
                       ) else (
                           echo No previous JAR found. Skipping backup.
                       )

                       endlocal
                       """
                   } catch (err) {
                       echo "Backup Stage Warning: ${err.getMessage()}"
                   }
               }
           }
       }

    }

    post {
        success {
            echo " PIPELINE SUCCESS: All stages executed successfully and application is UP!"
        }
        failure {
            echo " PIPELINE FAILED: Please check the specific stage error messages above."
        }
    }
}
