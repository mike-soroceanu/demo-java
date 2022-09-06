pipeline {
  agent {label 'slave01'}

  tools {
    maven 'Maven 3.6'
    jdk "JDK 1.8"
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

    stage ('Build maven application') {
      steps {
        sh '''
          mvn package
        '''
      }
    }

    stage ('Store the artifact') {
      steps{
        sh '''
          mkdir -p pkg
          mv target/demo.war pkg/demo.war
        '''
      }
    }

    stage ('Add jenkins user in docker group'){
      steps {
        script {
          sh "sudo usermod -a -G docker jenkins"
        }
      }
    }

    stage ('Build Docker image') {
      steps { 
        script { 
         sh "docker build -t mikesoroceanu/simplejavaapp-mvn-docker:${BUILD_ID} ."
        }
      }
    }

  }
}
