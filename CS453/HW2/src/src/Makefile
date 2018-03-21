JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) *.java

default: .java.class

clean:
	$(RM) *.class

package:
	-rm smithza.zip
	zip smithza ContextObject.java SymTableVisitor.java TypeCheckException.java TypeCheckHelper.java TypeCheckVisitor.java TypeHelper.java Typecheck.java
