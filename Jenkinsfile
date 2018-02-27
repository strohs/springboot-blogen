pipeline {
  agent {
    docker {
      image 'maven:3-alpine'
      args '-v /root/.m2:/root/.m2'
    }
    
  }
  stages {
    stage('Compile') {
      steps {
        sh 'mvn -B clean compile'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn -B test'
      }
    }
  }
}