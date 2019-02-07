1. Install Library
	Java 8
	Maven 3: Use build source code running with Java
		- Create a Tools folder and copy Maven.
	Z3 4.4.1: https://github.com/Z3Prover/z3/releases
		- Copy Z3 to the Tools folder similar to Maven
	Maven Library: used to install the jar file into the maven Repository rename the file jar into z3-4.4.1.jargo to the folder of Z3
mvn install: install-file -Dfile = z3-4.4.1.jar -DgroupId = com.microsoft -DartifactId = z3 -Dversion = 4.4.1 -Dpackaging = jar
2. Create file to open the system workspace
	Create a research-group-workspace folder: use Eclipse's information store workspace
	Update path in start.bat
3. Run start.bat
	Type build = mvn clean install -DskipTests
	Type updateEclipse =mvn eclipse:eclipse
	Type ecl:run eclipse
4. Run the program
	Run QueryOnPolicy -> testcase