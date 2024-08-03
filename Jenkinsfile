pipeline {
    agent any

    environment {
        GITHUB_CREDENTIALS_ID = 'bf69d9ff-a08d-4138-979a-2f7f32bc860b'
        GITHUB_ACCOUNT = 'anbu-xyz'
        GITHUB_REPO = 'spring-petclinic'
    }
    
    stages {
        stage('Build') {
            steps {
                script {
                    def commitSHA = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                    githubNotify account: env.GITHUB_ACCOUNT, context: 'Jenkins', credentialsId: env.GITHUB_CREDENTIALS_ID, description: 'Building...', repo: env.GITHUB_REPO, sha: commitSHA, status: 'PENDING', targetUrl: ''
                    
                    // Your build steps here
                    sh 'mvn clean test'
                    
                    githubNotify account: env.GITHUB_ACCOUNT, context: 'Jenkins', credentialsId: env.GITHUB_CREDENTIALS_ID, description: 'Success', repo: env.GITHUB_REPO, sha: commitSHA, status: 'SUCCESS', targetUrl: ''
                }
            }
        }
    }

    post {
        failure {
            script {
                def commitSHA = sh(script: 'git rev-parse HEAD', returnStdout: true).trim()
                githubNotify account: env.GITHUB_ACCOUNT, context: 'Jenkins', credentialsId: env.GITHUB_CREDENTIALS_ID, description: 'Build failed', repo: env.GITHUB_REPO, sha: commitSHA, status: 'FAILED', targetUrl: ''
            }
        }
    }
}
