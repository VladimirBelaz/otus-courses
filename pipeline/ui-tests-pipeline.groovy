pipeline {
    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: 'main')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser for UI tests')
        booleanParam(name: 'HEADLESS', defaultValue: false, description: 'Run in headless mode')
        string(name: 'BASE_URL', defaultValue: 'https://otus.ru', description: 'Base URL for tests')
        string(name: 'SELENOID_URL', defaultValue: 'http://host.docker.internal:4444', description: 'Selenoid URL')
    }

    triggers {
        pollSCM('H/5 * * * *')
        cron('H 0 * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Selenoid is available') {
            steps {
                sh """
                    curl -sf ${params.SELENOID_URL}/status || (echo 'Selenoid недоступен. Запусти его на хосте: cd /opt/selenoid && docker compose up -d' && exit 1)
                """
            }
        }

        stage('Run UI tests') {
            steps {
                sh """
            mvn clean test \\
                -Dbrowser=${params.BROWSER} \\
                -Dheadless=${params.HEADLESS} \\
                -Dbase.url=${params.BASE_URL} \\
                -Dselenoid.url=http://host.docker.internal:4444 \\
                -Dselenoid.ui.url=http://host.docker.internal:8081
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