pipeline {
  agent any

  environment {
    REGISTRY = "your-registry.com"
    BACKEND_IMAGE = "${REGISTRY}/flinksight-backend:${env.BUILD_NUMBER}"
    FRONTEND_IMAGE = "${REGISTRY}/flinksight-frontend:${env.BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    stage('Backend Build & Test') {
      steps {
        dir('flinksight-backend') {
          sh 'mvn clean package -DskipTests=false'
          sh 'mvn flyway:migrate -Dflyway.configFiles=../deploy/flyway.conf'
        }
      }
    }
    stage('Frontend Build & Test') {
      steps {
        dir('flinksight-frontend') {
          sh 'npm install'
          sh 'npm run build'
          sh 'npm run test'
        }
      }
    }
    stage('API Docs Sync & Mock Gen') {
      steps {
        dir('flinksight-backend') {
          sh 'mvn springdoc-openapi-maven-plugin:generate'
        }
        dir('flinksight-frontend') {
          sh '''
            npx openapi-generator-cli generate \
              -i ../flinksight-backend/target/openapi.json \
              -g typescript-axios \
              -o src/api
            npm run mock:generate # mock生成脚本，如有
          '''
        }
      }
    }
    stage('Build Docker Images') {
      steps {
        sh 'docker build -t $BACKEND_IMAGE ./flinksight-backend'
        sh 'docker build -t $FRONTEND_IMAGE ./flinksight-frontend'
      }
    }
    stage('Push Docker Images') {
      steps {
        sh 'docker push $BACKEND_IMAGE'
        sh 'docker push $FRONTEND_IMAGE'
      }
    }
    stage('Deploy to K8S') {
      steps {
        sh '''
        kubectl set image deployment/flinksight-backend flinksight-backend=$BACKEND_IMAGE --record
        kubectl set image deployment/flinksight-frontend flinksight-frontend=$FRONTEND_IMAGE --record
        kubectl rollout status deployment/flinksight-backend
        kubectl rollout status deployment/flinksight-frontend
        '''
      }
    }
    stage('Post-Deploy E2E & Healthcheck') {
      steps {
        sh 'bash test/scripts/e2e_check.sh'
      }
    }
  }
  post {
    success {
      mail to: 'devops@yourorg.com',
           subject: "[上线成功] Flinksight v${env.BUILD_NUMBER} 部署完成",
           body: "详见CI日志。"
    }
    failure {
      mail to: 'devops@yourorg.com',
           subject: "[上线失败] Flinksight v${env.BUILD_NUMBER} 构建或部署异常",
           body: "详见CI日志。"
    }
  }
}
