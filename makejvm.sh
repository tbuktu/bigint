#!/bin/bash

# Makes a JVM with a modified BigInteger class. Tested with Java 11.

rm -rf bigIntJRE
mkdir bigIntJRE
$JAVA_HOME/bin/jmod extract --dir bigIntJRE $JAVA_HOME/jmods/java.base.jmod
$JAVA_HOME/bin/javac -XDignore.symbol.file=true --patch-module java.base=. -d bigIntJRE/classes/ src/main/java/java/math/BigInteger.java
mkdir -p bigIntJmod
rm -f bigIntJmod/java.base.jmod
cd bigIntJRE/
$JAVA_HOME/bin/jmod create --class-path classes/ --header-files include/ --libs lib/ --config conf/ --cmds bin/ --legal-notices legal/ ../bigIntJmod/java.base.jmod
cd ..
rm -rf bigIntJRE
$JAVA_HOME/bin/jlink --output bigIntJRE --module-path bigIntJmod/java.base.jmod --add-modules java.base
rm bigIntJmod/java.base.jmod
rmdir bigIntJmod/
