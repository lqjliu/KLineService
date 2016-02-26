set MAVEN_HOME=C:\apache-maven-3.1.1\
%MAVEN_HOME%\bin\mvn -Dmaven.test.skip=true clean package
cd .\target
ren ROOT-1.0-SNAPSHOT.war ROOT.war