pipeline {
  agent {label 'slave01'}

  tools {
    maven 'Maven 3.6.3'
    jdk 'jdk8'
  }
  stages{
    stage ('Initialize') {
      steps{
        sh '''
          echo "PATH = ${PATH}"
          echo "M2_HOME = ${M2_HOME}"
        '''
      }
    }

    stage ('Build') {
      steps {
        echo 'This is a test pipeline.'
      }
    }
  }
}
