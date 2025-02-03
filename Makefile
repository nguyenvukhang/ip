MAKEFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
MAKEFILE_DIR  := $(dir $(MAKEFILE_PATH))
GRADLE := $(MAKEFILE_DIR)gradlew

current: gradle

test:
	DEBUG=1 $(GRADLE) run --args='test'

i: interact
interact: build
	$(GRADLE) run --quiet --console=plain

fmt:
	find * -name '*.java' | xargs clang-format -i

.PHONY: build gradle
