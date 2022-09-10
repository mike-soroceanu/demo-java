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
		stage('Login') {

			steps {
				sh "echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin"
		}


		stage('Push') {

			steps {
				sh "docker pull mikesoroceanu/simplejavaapp-mvn-docker:latest"
			}
		}

        stage('Run container') {

            steps{
                sh "docker run --name MySimpleJavaApp-${BUILD_ID} --rm -d -p 5000:8080 mikesoroceanu/simplejavaapp-mvn-docker:latest"
            }
        }
	
  }
}
