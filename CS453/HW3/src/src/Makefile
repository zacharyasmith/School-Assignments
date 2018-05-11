JC = javac
PWD := $(shell pwd)
default: hw5

hw3:
	$(JC) J2V.java

hw4:
	$(JC) -cp ".:vapor-parser.jar" V2VM.java

hw5:
	$(JC) -cp ".:vapor-parser.jar" VM2M.java

clean:
	-rm smithza.zip *.class
	-rm J2V/*.class J2V/context/*.class J2V/elements/*.class
	-rm V2VM/*.class V2VM/elements/*.class V2VM/CFG/*.class
	-rm VM2M/*.class VM2M/elements/*.class

packhw3:
	-rm smithza.zip
	zip smithza -r J2V J2V.java

packhw4:
	-rm smithza.zip
	zip smithza -r V2VM V2VM.java

packhw5:
	-rm smithza.zip
	zip smithza -r VM2M VM2M.java
