pipeline {
  agent any
  stages {
    stage('Compile') {
      steps {
        sh 'mvn -B clean compile'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn -B verify'
      }
    }
    stage('Report') {
      steps {
        sh 'junit \'target/*-reports/*.xml\''
      }
    }
  }
}