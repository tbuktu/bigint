#!/bin/sh

# path to JDK sources from HG
# hg clone http://hg.openjdk.java.net/jdk8/jdk8
JDK_SRC=../jdk8

if [ ! -f $JDK_SRC/jdk/src/share/classes/java/math/BigInteger.java ]; then
  echo BigInteger.java not found, check JDK_SRC in this script
  exit 1
fi

diff -u0 $JDK_SRC/jdk/src/share/classes/java/math/BigInteger.java > src/main/java/java/math/BigInteger.java.diff src/main/java/java/math/BigInteger.java
diff -u0 $JDK_SRC/jdk/src/share/classes/java/math/MutableBigInteger.java > src/main/java/java/math/MutableBigInteger.java.diff src/main/java/java/math/MutableBigInteger.java
