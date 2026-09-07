pipeline {
    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: 'main')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser for UI tests')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run in headless mode')
        string(name: 'BASE_URL', defaultValue: 'https://otus.ru', description: 'Base URL for tests')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run UI tests') {
            steps {
                sh """
                    mvn clean test \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS} \
                        -Dbase.url=${params.BASE_URL}
                """
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
            echo "UI tests finished"
        }
    }
}