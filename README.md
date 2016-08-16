# 1. Простой алгоритм:
Все ноды кроме нулевой отправляют свое число нулевой ноде. На нулевой ноде все полученные числа складываются вместе с секретным числом нулевой ноды и полученная сумма отправляется обратно каждой ноде.

Примерный вывод в консоль для 5 нод:

```
sbt run
Node 0. Number - 1567135050
Node 1. Number - 405049488
Node 2. Number - 1337413465
Node 3. Number - 619308782
Node 4. Number - -1268040045
Node 1. Sending 405049488 to Node 0
Node 0. Recieved 405049488
Node 2. Sending 1337413465 to Node 0
Node 0. Recieved 1337413465
Node 3. Sending 619308782 to Node 0
Node 0. Recieved 619308782
Node 4. Sending -1268040045 to Node 0
Node 0. Recieved -1268040045
Node 0. Sending -1634100556 to Node 1
Node 0. Sending -1634100556 to Node 2
Node 0. Sending -1634100556 to Node 3
Node 0. Sending -1634100556 to Node 4
Node 1. Recieved -1634100556
Node 0. Calculated sum - -1634100556
Node 1. Calculated sum - -1634100556
Node 4. Recieved -1634100556
Node 4. Calculated sum - -1634100556
Node 3. Recieved -1634100556
Node 3. Calculated sum - -1634100556
Node 2. Recieved -1634100556
Node 2. Calculated sum - -1634100556
```
# 2. Алгоритм, в котором сумма - затратная операция.
Нулевая нода отправляет свое секретное число первой ноде, первая нода складывает его со своим секретным числом и отправляет дальше. Так до последней ноды. На последней ноде находится сумма всех чисел и она эту сумму отправляет нулевой ноде, нулевая первой и так до предпоследней.

Приблизительный вывод на консоль:
```
sbt run
Node 0. Number - -106105964
Node 1. Number - -1622096388
Node 2. Number - 1878689098
Node 3. Number - -1044270737
Node 4. Number - -1392781058
Node 0. Sending -106105964 to Node 1
Node 1. Recieved -106105964
Node 1. Sending -1728202352 to Node 2
Node 2. Recieved -1728202352
Node 2. Sending 150486746 to Node 3
Node 3. Recieved 150486746
Node 3. Sending -893783991 to Node 4
Node 4. Recieved -893783991
Node 4. Sending 2008402247 to Node 0
Node 4. Calculated sum - 2008402247
Node 0. Recieved 2008402247
Node 0. Calculated sum - 2008402247
Node 0. Sending 2008402247 to Node 1
Node 1. Recieved 2008402247
Node 1. Calculated sum - 2008402247
Node 1. Sending 2008402247 to Node 2
Node 2. Recieved 2008402247
Node 2. Calculated sum - 2008402247
Node 2. Sending 2008402247 to Node 3
Node 3. Recieved 2008402247
Node 3. Calculated sum - 2008402247
```

# 3. Алгоритм, основанный на двоичном дереве.
Ноды, под которыми нет дочерних нод, отправляют свое число родителю и ждут сумму. Ноды, под которыми есть дочерние ноды, ждут чисел от дочерних нод, после получения чисел от дочерних нод посылают сумму чисел дочерних нод и своего числа - родителю; ждут сумму; после получения суммы отправляют ее своим дочерним нодам. Корневая нода ждет чисел от дочерних нод, после получения, складывает их со своим числом и отсылает сумму своим дочерним нодам.

Приблизительный вывод на консоль:
```
sbt run
Node 0. Parent - None. Left - Some(1). Right - Some(2). Number - 1327446636
Node 1. Parent - Some(0). Left - Some(3). Right - Some(4). Number - 973813061
Node 2. Parent - Some(0). Left - Some(5). Right - Some(6). Number - 2127258231
Node 3. Parent - Some(1). Left - None. Right - None. Number - 153782927
Node 4. Parent - Some(1). Left - None. Right - None. Number - 96345053
Node 5. Parent - Some(2). Left - None. Right - None. Number - -1016266174
Node 6. Parent - Some(2). Left - None. Right - None. Number - 1703230443
Node 0. Starting
Node 0. Receiving rightNumber.
Node 2. Starting
Node 2. Receiving leftNumber.
Node 1. Starting
Node 1. Receiving leftNumber.
Node 3. Starting
Node 3. Sending 153782927 to Node 1
Node 3. Receiving sum.
Node 1. Recieved 153782927
Node 1. Receiving rightNumber.
Node 4. Starting
Node 4. Sending 96345053 to Node 1
Node 1. Recieved 96345053
Node 1. Sending 1223941041 to Node 0
Node 1. Receiving sum.
Node 0. Recieved 1223941041
Node 0. Receiving leftNumber.
Node 4. Receiving sum.
Node 5. Starting
Node 5. Sending -1016266174 to Node 2
Node 5. Receiving sum.
Node 2. Recieved -1016266174
Node 2. Receiving rightNumber.
Node 6. Starting
Node 6. Sending 1703230443 to Node 2
Node 6. Receiving sum.
Node 2. Recieved 1703230443
Node 2. Sending -1480744796 to Node 0
Node 2. Receiving sum.
Node 0. Recieved -1480744796
Node 0. Sending 1070642881 to Node 1
Node 0. Sending 1070642881 to Node 2
Node 0. Calculated sum - 1070642881
Node 1. Recieved 1070642881
Node 1. Sending 1070642881 to Node 3
Node 1. Sending 1070642881 to Node 4
Node 1. Calculated sum - 1070642881
Node 4. Recieved 1070642881
Node 4. Calculated sum - 1070642881
Node 3. Recieved 1070642881
Node 3. Calculated sum - 1070642881
Node 2. Recieved 1070642881
Node 2. Sending 1070642881 to Node 5
Node 2. Sending 1070642881 to Node 6
Node 2. Calculated sum - 1070642881
Node 5. Recieved 1070642881
Node 5. Calculated sum - 1070642881
Node 6. Recieved 1070642881
Node 6. Calculated sum - 1070642881
Program end
```


# 4. Приложение с использованием Akka и выбором между тремя алгоритмами - простым, алгоритмом, в котором сумма - затратная операция и алгоритме основанном на двоичном дереве. Есть возможность указания количества нод.

Приблизительный вывод на консоль:

```
sbt run
Input node count (1 - 2147483647): 5
1. Simple algorithm.
2. Expensive sum algorithm.
3. Tree-based algorithm.
Select algorithm (1 or 2 or 3): 1
Info. Node 3. Number - 385889962
Info. Node 4. Number - -1019037654
Info. Node 2. Number - 220990364
Info. Node 1. Number - 1601151300
Info. Node 0. Number - 2058084702
Info. Node 1. Sending 1601151300 to Node 0
Info. Node 3. Sending 385889962 to Node 0
Info. Node 2. Sending 220990364 to Node 0
Info. Node 4. Sending -1019037654 to Node 0
Info. Node 0. Recieved 385889962
Info. Node 0. Recieved 1601151300
Info. Node 0. Recieved 220990364
Info. Node 0. Recieved -1019037654
Info. Node 0. Sending -1047888622 to Node 1
Info. Node 0. Sending -1047888622 to Node 2
Info. Node 1. Recieved -1047888622
Info. Node 2. Recieved -1047888622
Info. Node 0. Sending -1047888622 to Node 3
Info. Node 2. Calculated sum - -1047888622
Info. Node 1. Calculated sum - -1047888622
Info. Node 3. Recieved -1047888622
Info. Node 3. Calculated sum - -1047888622
Info. Node 0. Sending -1047888622 to Node 4
Info. Node 0. Calculated sum - -1047888622
Info. Node 4. Recieved -1047888622
Info. Node 4. Calculated sum - -1047888622
```

```
sbt run
Input node count (1 - 2147483647): 5
1. Simple algorithm.
2. Expensive sum algorithm.
3. Tree-based algorithm.
Select algorithm (1 or 2 or 3): 2
Info. Node 2. Number - -838547795
Info. Node 3. Number - -347199041
Info. Node 1. Number - -242957373
Info. Node 4. Number - -1299176102
Info. Node 0. Number - 1269303590
Info. Node 0. Sending 1269303590 to Node 1
Info. Node 1. Recieved 1269303590
Info. Node 1. Sending 1026346217 to Node 2
Info. Node 2. Recieved 1026346217
Info. Node 2. Sending 187798422 to Node 3
Info. Node 3. Recieved 187798422
Info. Node 3. Sending -159400619 to Node 4
Info. Node 4. Recieved -159400619
Info. Node 4. Sending -1458576721 to Node 0
Info. Node 0. Recieved -1458576721
Info. Node 4. Calculated sum - -1458576721
Info. Node 0. Calculated sum - -1458576721
Info. Node 0. Sending -1458576721 to Node 1
Info. Node 1. Recieved -1458576721
Info. Node 1. Calculated sum - -1458576721
Info. Node 1. Sending -1458576721 to Node 2
Info. Node 2. Recieved -1458576721
Info. Node 2. Calculated sum - -1458576721
Info. Node 2. Sending -1458576721 to Node 3
Info. Node 3. Recieved -1458576721
Info. Node 3. Calculated sum - -1458576721
```

```
Input node count (1 - 2147483647): 5
1. Simple algorithm.
2. Expensive sum algorithm.
3. Tree-based algorithm.
Select algorithm (1 or 2 or 3): 3
Info. Node 1. Number - 1057108801
Info. Node 0. Number - 1130868442
Info. Node 4. Number - 1480056064
Info. Node 3. Number - -821556568
Info. Node 2. Number - 334999934
Info. Node 4. Sending 1480056064 to Node 1
Info. Node 2. Sending 334999934 to Node 0
Info. Node 3. Sending -821556568 to Node 1
Info. Node 0. Recieved 334999934
Info. Node 1. Recieved 1480056064
Info. Node 1. Recieved -821556568
Info. Node 1. Sending 1715608297 to Node 0
Info. Node 0. Recieved 1715608297
Info. Node 0. Sending -1113490623 to Node 1
Info. Node 0. Sending -1113490623 to Node 2
Info. Node 1. Recieved -1113490623
Info. Node 2. Recieved -1113490623
Info. Node 0. Calculated sum - -1113490623
Info. Node 2. Calculated sum - -1113490623
Info. Node 1. Sending -1113490623 to Node 3
Info. Node 1. Sending -1113490623 to Node 4
Info. Node 3. Recieved -1113490623
Info. Node 3. Calculated sum - -1113490623
Info. Node 1. Calculated sum - -1113490623
Info. Node 4. Recieved -1113490623
Info. Node 4. Calculated sum - -1113490623
```

Вывод `sbt test`
```
[info] SimpleAlgorithmSpec:
[info] A simple algorithm
[info] - should give correct sum on one node
[info] - should give correct sum on n nodes
[info] TreeBasedAlgorithmSpec:
[info] An tree-based algorithm
[info] - should give correct sum on one node
[info] - should give correct sum on 2 nodes
[info] - should give correct sum on n nodes
[info] ExpensiveSumAlgorithmSpec:
[info] An expensive sum algorithm
[info] - should give correct sum on one node
[info] - should give correct sum on 2 nodes
[info] - should give correct sum on n nodes
[info] Run completed in 532 milliseconds.
[info] Total number of tests run: 8
[info] Suites: completed 3, aborted 0
[info] Tests: succeeded 8, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```