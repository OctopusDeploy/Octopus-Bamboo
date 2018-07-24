pipeline {
    agent {
        kubernetes {
            label 'declarative-pod'
            containerTemplate {
                name 'jnlp'
                image 'jenkinsci/jnlp-slave'
                workingDirectory '/home/jenkins'
                ttyEnabled true
                args '${computer.jnlpmac} ${computer.name}'
            }
        }
    }
    tools {
        maven 'Maven 3.3.9'
        jdk 'jdk8'
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.skip=true compile'
            }
        }
    }
}