MAKEFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
MAKEFILE_DIR  := $(dir $(MAKEFILE_PATH))
GRADLE := $(MAKEFILE_DIR)gradlew

current:
	$(GRADLE) run --args='gui'

test:
	$(GRADLE) test

jar:
	$(GRADLE) shadowJar

t:
	DEBUG=1 $(GRADLE) run --args='test'

i: interact
interact: build
	$(GRADLE) run --quiet --console=plain

fmt:
	find * -name '*.java' | xargs clang-format -i

c: checkstyleMain
checkstyleMain:
	$(GRADLE) checkstyleMain

checkstyle:
	$(GRADLE) check

.PHONY: build gradle test
