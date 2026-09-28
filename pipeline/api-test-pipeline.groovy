pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')          // опрос SCM каждые 5 минут
        cron('H 0 * * *')                // каждый день в полночь
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run API tests') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Publish Allure report') {
            steps {
                allure([
                        includeProperties: false,
                        results: [[path: 'target/allure-results']]
                ])
            }
        }
    }

    post {
        always {
            echo "API pipeline finished"
        }
    }
}