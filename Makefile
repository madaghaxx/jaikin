build:
	javac src/*.java -d build

run: build
	java -cp build Main
