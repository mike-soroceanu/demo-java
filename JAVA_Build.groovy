pipeline {
  agent {label 'slave01'}

  tools {
    maven 'Maven 3.6'
    jdk "JDK 1.8"
  }


	environment {
		DOCKERHUB_CREDENTIALS=credentials('DockerHub')
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

    stage ('Build Docker image') {
      steps { 
        script { 
         sh "docker build -t mikesoroceanu/simplejavaapp-mvn-docker:${BUILD_ID} ."
         sh "docker build -t mikesoroceanu/simplejavaapp-mvn-docker:latest ."
        }
      }
    }


		stage('Login to Docker Hub') {

			steps {
				sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
			}
		}


		stage('Push image to Docker Hub') {

			steps {
				sh "docker push mikesoroceanu/simplejavaapp-mvn-docker:${BUILD_ID}"
        sh "docker push mikesoroceanu/simplejavaapp-mvn-docker:latest"
			}
		}
	
  }
}
