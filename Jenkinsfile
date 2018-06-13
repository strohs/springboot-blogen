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
            }
            post {
                always {
                    junit 'target/*-reports/*.xml'
                }
            }
        }
//        stage('Deliver') {
//            steps {
//                sh 'cp ./target/spring-boot-blogen-0.0.1-SNAPSHOT.jar ~'
//            }
//        }
    }
}