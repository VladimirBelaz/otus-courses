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

        stage('Start Selenoid') {
            steps {
                sh '''
                    docker compose -f selenoid/docker-compose.yml up -d
                    echo "Waiting for Selenoid to be ready..."
                    for i in $(seq 1 30); do
                        if curl -sf http://host.docker.internal:4444/status > /dev/null 2>&1; then
                            echo "Selenoid is ready"
                            curl -s http://host.docker.internal:4444/status
                            exit 0
                        fi
                        echo "Attempt $i: not ready yet"
                        sleep 2
                    done
                    echo "Selenoid did not start in time"
                    exit 1
                '''
            }
        }

        stage('Run UI tests') {
            steps {
                sh """
                    mvn clean test \\
                        -Dbrowser=${params.BROWSER} \\
                        -Dheadless=${params.HEADLESS} \\
                        -Dbase.url=${params.BASE_URL} \\
                        -Dselenoid.url=${params.SELENOID_URL}
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