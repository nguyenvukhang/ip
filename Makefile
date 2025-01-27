current: test

run:
	find * -name '*.java' > sources.txt
	javac -d build @sources.txt
	DEBUG=1 java -cp build Main < debug_input.txt

test:
	find * -name '*.java' > sources.txt
	javac -d build @sources.txt
	java -cp build Main test

i: interact
interact:
	javac src/main/java/*.java
	java -cp src/main/java Main
