JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) *.java

default: .java.class

clean:
	$(RM) *.class J2V/*.class J2V/context/*.class J2V/elements/*.class

package:
	-rm smithza.zip
	zip smithza -r context elements J2V.java VaporVisitor.java
