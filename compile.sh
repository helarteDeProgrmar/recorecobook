#! /bin/bash
mkdir -p out
javac -cp "lib/*" -d out $(find src -name "*.java")
if [ ! -f out/Media/Cabecera.png ]; then
    echo "Cabecera.png no encontrado en out/Media/, copiando desde src/Media/..."
    mkdir -p out/Media
    cp src/Media/Cabecera.png out/Media/
fi
java -cp "out:lib/*" Main
