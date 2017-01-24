node{
    // Mark the code checkout 'stage'....
    stage 'Checkout'
git url: 'git@github.com:lausdahl/astcreator-tryout-pt.git'
    // Get some code from a GitHub repository
 //   git url: 'https://github.com/alvarolobato/maven_test.git'

    // Mark the code build
    stage 'Clean'

//    withMaven(
//            maven: 'M3',
//            mavenSettingsConfig: 'maven-settings-for-gameoflife',
//            mavenLocalRepo: '.repository') {

withMaven(mavenLocalRepo: '.repository', mavenSettingsFilePath: '/var/lib/jenkins/internal-resources/settings.xml') {

        // Run the maven build
        sh "mvn clean"
    }

stage 'package'

//  withMaven(
  //          maven: 'M3',
    //        mavenSettingsConfig: 'maven-settings-for-gameoflife',
      //      mavenLocalRepo: '.repository') {
withMaven(mavenLocalRepo: '.repository', mavenSettingsFilePath: '/var/lib/jenkins/internal-resources/settings.xml') {

        // Run the maven build
        sh "mvn package"
    }


}
