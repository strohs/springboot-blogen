pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }
    
  }
  stages {
    stage('Compile') {
      parallel {
        stage('Compile') {
          steps {
            sh 'mvn -B clean verify'
          }
        }
        stage('exStage') {
          steps {
            echo 'this is a message'
          }
        }
      }
    }
  }
}