task_branch = "${TEST_BRANCH_NAME}"
task_tags = "${TEST_TAGS_NAME}"

def branch_cutted = task_branch.contains("origin") ? task_branch.split('/')[1] : task_branch.trim()
def tags_cutted = task_tags.replace(' ', '').split(',').collect{it as String}
currentBuild.displayName = "$task_tags $branch_cutted"
base_git_url = "https://github.com/Moby812/RestAssuredTest.git"

node {
    withEnv(["branch=${branch_cutted}", "base_url=${base_git_url}"]) {
        stage("Checkout Branch") {
            if (!"$branch_cutted".contains("allSteps")) {
                try {
                    getProject("$base_git_url", "$branch_cutted")
                } catch (err) {
                    echo "Failed get branch $branch_cutted"
                    throw ("${err}")
                }
            } else {
                echo "Current branch is allSteps"
            }
        }

        try {
            parallel getTestStages(tags_cutted)
        } finally {
            stage ("Allure") {
                generateAllure()
            }
        }
    }
}


def getTestStages(testTags) {
    def stages = [:]
    testTags.each { tag ->
        stages["${tag}"] = {
            runTestWithTag(tag)
        }
    }
    return stages
}


def runTestWithTag(String tag) {
    try {
        labelledShell(label: '''Run tests''', script: '''
                  mvn clean test -Dgroups='''+tag
        )
    } finally {
        echo "some failed tests"
    }
}

def getProject(String repo, String branch) {
    cleanWs()
    checkout scm: [
            $class           : 'GitSCM', branches: [[name: branch]],
            userRemoteConfigs: [[
                                        url: repo
                                ]]
    ]
}

def generateAllure() {
    allure([
            includeProperties: true,
            jdk              : '',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'target/allure-results']]
    ])
}
