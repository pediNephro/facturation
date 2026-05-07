pipeline {
    agent any
    environment {
        IMAGE_NAME = 'brahimbk/facturation'
        IMAGE_TAG = 'latest'
    }
    stages {
        stage('1 — Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/pediNephro/facturation.git'
            }
        }
        stage('2 — Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('3 - Tests') {
            steps {
                sh 'mvn test || true'
            }
            post {
                always {
                    script {
                        if (fileExists('target/surefire-reports')) {
                            junit 'target/surefire-reports/*.xml'
                        } else {
                            echo 'No test reports found - skipping junit publish'
                        }
                    }
                }
            }
        }
        stage('4 — Docker Build') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }
        stage('5 — Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'docker-hub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    sh "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }
    }
    post {
        success {
            echo "Pipeline done — image ${IMAGE_NAME}:${IMAGE_TAG} is on Docker Hub"
            build job: 'cd-pipeline', wait: false
        }
        failure {
            echo "Pipeline failed — check logs above"
        }
        always {
            sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG} || true"
        }
    }
}