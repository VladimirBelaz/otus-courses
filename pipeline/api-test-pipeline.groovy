pipeline {
    agent any

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