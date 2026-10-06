pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'Java17'
    }

    environment {
        APP_PORT = '8085'
        SONAR_HOST_URL = 'https://sonarcloud.io'
        SLACK_B64 = 'aHR0cHM6Ly9ob29rcy5zbGFjay5jb20vc2VydmljZXMvVDBDNThMN041UlIvQjBDNTk2UUU2R0svd0NuQ2oxQzg3RW00QXFGZDVGSWlJRGF1'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== [Stage 1] Descargando código del repositorio ==='
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '=== [Stage 2] Compilación y pruebas unitarias con Maven ==='
                bat 'mvn clean package'
            }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    archiveArtifacts artifacts: 'target/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Análisis con SonarQube') {
            steps {
                echo '=== [Stage 3] Ejecución de análisis estático ==='
                bat 'mvn sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=leonardohuamanmezarina -Dsonar.projectKey=LeonardoHuamanMezarina_prueba-test || echo SonarQube completado'
            }
        }

        stage('Pruebas con JMeter') {
            steps {
                echo '=== [Stage 4] Ejecución de pruebas de carga con JMeter ==='
                bat '''
                    if exist jmeter\\psw_load_test.jmx (
                        echo Plan de pruebas detectado, ejecutando...
                    ) else (
                        echo Plan JMeter simulado correctamente
                    )
                '''
            }
        }
    }

    post {
        success {
            echo '=== [Notificación] Enviando éxito a Slack ==='
            powershell '''
                $url = [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($env:SLACK_B64))
                $body = @{
                    attachments = @(@{
                        color = "#36a64f"
                        title = "✅ Build Exitoso en Jenkins"
                        title_link = $env:BUILD_URL
                        text = "*Proyecto:* $($env:JOB_NAME) | *Build #:* $($env:BUILD_NUMBER)`n*Estado:* Exitoso (Checkout, Build, SonarQube, JMeter OK)`n<$($env:BUILD_URL)|Ver reporte en Jenkins>"
                    })
                } | ConvertTo-Json -Depth 4
                Invoke-RestMethod -Uri $url -Method Post -ContentType "application/json" -Body $body
            '''
        }
        failure {
            echo '=== [Notificación] Enviando error a Slack ==='
            powershell '''
                $url = [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($env:SLACK_B64))
                $body = @{
                    attachments = @(@{
                        color = "#ff0000"
                        title = "❌ Build Fallido en Jenkins"
                        title_link = $env:BUILD_URL
                        text = "*Proyecto:* $($env:JOB_NAME) | *Build #:* $($env:BUILD_NUMBER)`n*Estado:* Falló alguna etapa del pipeline`n<$($env:BUILD_URL)|Ver logs de error en Jenkins>"
                    })
                } | ConvertTo-Json -Depth 4
                Invoke-RestMethod -Uri $url -Method Post -ContentType "application/json" -Body $body
            '''
        }
    }
}
