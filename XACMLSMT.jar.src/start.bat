set M2_HOME=E:\tools\apache-maven-3.5.4
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_151
set PATH=%M2_HOME%\bin;%JAVA_HOME%\bin;%PATH%
set ECLIPSE_FOLDER=E:\File_zip\Eclipse\eclipse-committers-photon-R-win32-x86_64\eclipse
set PROJECT_FOLDER=E:\Source\source\XACMLSMT.jar.src

doskey build=mvn clean install -DskipTests
doskey updateEclipse=mvn eclipse:eclipse
doskey ecl=%ECLIPSE_FOLDER%\eclipse.exe -clean -data "E:\EclipseWorkSpace\ResearchGroup"

cd %PROJECT_FOLDER%

cls
cmd