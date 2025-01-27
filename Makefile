current: test

build:
	find * -name '*.java' > sources.txt
	javac -d build @sources.txt

run: build
	DEBUG=1 java -cp build Main < debug_input.txt

test: build
	java -cp build Main test

i: interact
interact: build
	java -cp build Main

fmt:
	find * -name '*.java' | xargs clang-format -i

.PHONY: build
