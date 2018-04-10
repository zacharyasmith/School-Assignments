JC = javac
PWD := $(shell pwd)
default: hw4

hw3:
	$(JC) J2V.java

hw4:
	$(JC) -cp ".:vapor-parser.jar" V2VM.java

clean:
	-rm smithza.zip *.class
	-rm J2V/*.class J2V/context/*.class J2V/elements/*.class
	-rm V2VM/*.class V2VM/elements/*.class V2VM/CFG/*.class

packhw3:
	-rm smithza.zip
	zip smithza -r J2V J2V.java

packhw4:
	-rm smithza.zip
	zip smithza -r V2VM V2VM.java
