MAKEFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
MAKEFILE_DIR  := $(dir $(MAKEFILE_PATH))
GRADLE := $(MAKEFILE_DIR)gradlew

current: run

test:
	$(GRADLE) test

jar:
	$(GRADLE) clean shadowJar
	rm -f ~/Downloads/pascal.jar
	mv build/libs/pascal.jar ~/Downloads

run:
	$(GRADLE) run --quiet --console=plain

fmt:
	$(GRADLE) spotlessApply

check:
	$(GRADLE) check
