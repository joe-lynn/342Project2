


all:
	javac -d bin\ -cp 'lib\*;src\;lib\akka\*' src\*.java -Xlint

.PHONY: run
	@java -cp 'lib/*:lib/akka/*:bin/*' Driver

