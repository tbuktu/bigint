<html>
<head>
<style TYPE="text/css">
<!--
  table {
    border-collapse:collapse;
  }
  table td, th {
    border-style: solid;
    border-width: 1px;
    text-align: right;
  }
-->
</style>
</head>

<body>
# Efficient BigInteger Implementation

This is an improved version of `java.math.BigInteger` that uses fast algorithms for multiplication. It is based on [Alan Eliasen's BigInteger patch](http://futureboy.us/temp/BigInteger.java).

Depending on the input size, numbers are multiplied using [Long Multiplication](http://en.wikipedia.org/wiki/Multiplication_algorithm#Long_multiplication), [Karatsuba](http://en.wikipedia.org/wiki/Karatsuba_algorithm), [Toom-Cook](http://en.wikipedia.org/wiki/Toom%E2%80%93Cook_multiplication), or [Schönhage-Strassen](http://en.wikipedia.org/wiki/Sch%C3%B6nhage%E2%80%93Strassen_algorithm).

Sample benchmark results:
<table>
  <tr>
    <th>#Digits</th><th>OpenJDK 6 BigInteger</th><th>Improved BigInteger</th><th>Algorithm</th>
  </tr>
  <tr>
    <td>10</td><td>0.00005ms</td><td>0.00006ms</td><td>Long</td>
  </tr>
  <tr>
    <td>25</td><td>0.00018ms</td><td>0.00018ms</td><td>Long</td>
  </tr>
  <tr>
    <td>50</td><td>0.00047ms</td><td>0.00046ms</td><td>Long</td>
  </tr>
  <tr>
    <td>75</td><td>0.00067ms</td><td>0.00069ms</td><td>Long</td>
  </tr>
  <tr>
    <td>100</td><td>0.00128ms</td><td>0.00132ms</td><td>Long</td>
  </tr>
  <tr>
    <td>250</td><td>0.00635ms</td><td>0.00647ms</td><td>Long</td>
  </tr>
  <tr>
    <td>500</td><td>0.02517ms</td><td>0.02194ms</td><td>Kara</td>
  </tr>
  <tr>
    <td>750</td><td>0.05679ms</td><td>0.04603ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>1000</td><td>0.10030ms</td><td>0.07294ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>2500</td><td>0.62099ms</td><td>0.29881ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>5000</td><td>2.48014ms</td><td>0.80627ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>7500</td><td>5.61238ms</td><td>1.53449ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>10000</td><td>10.02289ms</td><td>2.38305ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>25000</td><td>62.33890ms</td><td>9.44067ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>50000</td><td>251.21408ms</td><td>26.00191ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>75000</td><td>563.95811ms</td><td>46.20780ms</td><td>SS</td>
  </tr>
  <tr>
    <td>100000</td><td>1003.29212ms</td><td>75.03685ms</td><td>Toom</td>
  </tr>
  <tr>
    <td>250000</td><td>6.28s</td><td>.22s</td><td>SS</td>
  </tr>
  <tr>
    <td>500000</td><td>25.14s</td><td>.50s</td><td>SS</td>
  </tr>
  <tr>
    <td>750000</td><td>56.82s</td><td>1.08s</td><td>SS</td>
  </tr>
  <tr>
    <td>1000000</td><td>101.56s</td><td>1.08s</td><td>SS</td>
  </tr>
  <tr>
    <td>2500000</td><td>645.63s</td><td>2.57s</td><td>SS</td>
  </tr>
  <tr>
    <td>5000000</td><td>2576.74s</td><td>5.51s</td><td>SS</td>
  </tr>
  <tr>
    <td>7500000</td><td>5807.67s</td><td>13.55s</td><td>SS</td>
  </tr>
  <tr>
    <td>10000000</td><td>8584.36s</td><td>13.49s</td><td>SS</td>
  </tr>
</table>
</body>
</html>
