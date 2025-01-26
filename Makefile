run:
	find * -name '*.java' > sources.txt
	javac -d build @sources.txt
	DEBUG=1 java -cp build Main < debug_input.txt

i: interact
interact:
	javac src/main/java/*.java
	java -cp src/main/java Main
