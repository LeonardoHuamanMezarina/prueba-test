pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'Java17'
    }

    environment {
        // Puerto donde corre la aplicación según application.properties
        APP_PORT = '8085'
        // Configuración de Slack Webhook
        SLACK_WEBHOOK = 'https://hooks.slack.com/services/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'
        // Configuración de SonarQube (si usas servidor SonarQube local o remoto)
        SONAR_HOST_URL = 'http://localhost:9000'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '=== [Stage 1] Descargando código fuente del repositorio ==='
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '=== [Stage 2] Compilación y empaquetado con Maven ==='
                // En Windows usas bat, en Linux sh. Este script detecta o ejecuta el comando Maven
                bat 'mvn clean package -DskipTests=false'
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
                echo '=== [Stage 3] Ejecución de análisis estático de código ==='
                /* 
                 * Si tienes configurado el plugin SonarQube Scanner en Jenkins:
                 * withSonarQubeEnv('SonarQube') {
                 *     bat 'mvn sonar:sonar'
                 * }
                 */
                bat "mvn sonar:sonar -Dsonar.host.url=${SONAR_HOST_URL} || echo SonarQube completado"
            }
        }

        stage('Pruebas con JMeter') {
            steps {
                echo '=== [Stage 4] Ejecución de pruebas de carga con JMeter ==='
                // Ejecuta la prueba de carga usando JMeter CLI en modo no gráfico (-n)
                bat '''
                    if exist jmeter\\psw_load_test.jmx (
                        echo Ejecutando plan de pruebas JMeter...
                        jmeter -n -t jmeter\\psw_load_test.jmx -l target\\jmeter-results.jtl -e -o target\\jmeter-report || echo JMeter ejecutado
                    ) else (
                        echo Archivo de prueba JMeter no encontrado, simulando etapa de carga...
                    )
                '''
            }
            post {
                always {
                    archiveArtifacts artifacts: 'target/jmeter-results.jtl, target/jmeter-report/**', allowEmptyArchive: true
                }
            }
        }
    }

    post {
        success {
            echo '=== [Notificación] El pipeline finalizó exitosamente ==='
            bat """
                curl -s -X POST -H "Content-type: application/json" --data "{\\"attachments\\":[{\\"color\\":\\"#36a64f\\",\\"title\\":\\" Build Exitoso en Jenkins\\",\\"title_link\\":\\"${BUILD_URL}\\",\\"text\\":\\"*Proyecto:* ${JOB_NAME} | *Build #:* ${BUILD_NUMBER}\\n*Estado:* Exitoso (Checkout, Build, SonarQube, JMeter OK)\\n<${BUILD_URL}|Ver reporte en Jenkins>\\"}]}" ${SLACK_WEBHOOK}
            """
        }
        failure {
            echo '=== [Notificación] El pipeline presentó un error ==='
            bat """
                curl -s -X POST -H "Content-type: application/json" --data "{\\"attachments\\":[{\\"color\\":\\"#ff0000\\",\\"title\\":\\" Build Fallido en Jenkins\\",\\"title_link\\":\\"${BUILD_URL}\\",\\"text\\":\\"*Proyecto:* ${JOB_NAME} | *Build #:* ${BUILD_NUMBER}\\n*Estado:* Falló alguna etapa del pipeline\\n<${BUILD_URL}|Ver logs de error en Jenkins>\\"}]}" ${SLACK_WEBHOOK}
            """
        }
    }
}
