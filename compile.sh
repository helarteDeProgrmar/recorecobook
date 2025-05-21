#! /bin/bash
mkdir -p out
javac -cp "lib/*" -d out $(find src -name "*.java")
java -cp "out:lib/*" Main
