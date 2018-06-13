pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B clean install'
                sh 'mvn dockerfile:build'
            }
            post {
                always {
                    junit 'target/*-reports/*.xml'
                }
            }
        }
//        stage('Deliver') {
//            steps {
//                sh 'mvn dockerfile:push'
//            }
//        }
    }
}