.PHONY: build run

build:
	javac src/*.java src/**/*.java -d build

run: build
	java -cp build Main
