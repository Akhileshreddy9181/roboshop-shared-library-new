def call () {
    if(env.sonar_extra_opts == "") {
        env.sonar_extra_opts=""
    }
    node('workstation') {

        stage('Check Out Code') {
            cleanWs()
            git branch: 'main', url: "https://github.com/akhileshreddy9181/${component}"
        }

        sh 'env'

        if(env.BRANCH_NAME != "main") {
            stage('Compile/Build') {
                common.compile()
            }
        }

        if(!env.TAG_NAME && env.BRANCH_NAME != "main" ) {
            stage('Test Cases') {
                common.testcases()
            }
        }

        if(env.BRANCH_NAME ==~ "PR-.*")
        {
            stage('Code Quality') {
                common.codequality()
            }
        }

        if(env.TAG_NAME) {
            stage('Package') {
                common.prepareArtifacts()
            }
            stage('Artifact Upload') {
                common.artifactUpload()
            }
        }


    }

}