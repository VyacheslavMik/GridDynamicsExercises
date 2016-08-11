# 1. Простой алгоритм:
Все ноды кроме нулевой отправляют свое число нулевой ноде. На нулевой ноде все полученные числа складываются вместе с секретным числом нулевой ноды и полученная сумма отправляется обратно каждой ноде.

Примерный вывод в консоль для 5 нод:

`
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
`
# 2. Алгоритм, в котором сумма - затратная операция.
Нулевая нода отправляет свое секретное число первой ноде, первая нода складывает его со своим секретным числом и отправляет дальше. Так до последней ноды. На последней ноде находится сумма всех чисел и она эту сумму отправляет нулевой ноде, нулевая первой и так до предпоследней.

Приблизительный вывод на консоль:
`sbt run`
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

# 4. Приложение с использованием Akka и выбором между двумя алгоритмами простым и алгоритмом, в котором сумма - затратная операция. Есть возможность указания количества нод.

Приблизительный вывод на консоль:

`sbt run`
Input node count (1 - 2147483647): 5
1. Simple algorithm.
2. Expensive sum algorithm.
Select algorithm (1 or 2): 1
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

`sbt run`
Input node count (1 - 2147483647): 5
1. Simple algorithm.
2. Expensive sum algorithm.
Select algorithm (1 or 2): 2
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

Вывод `sbt test`
[info] SimpleAlgorithmSpec:
[info] A simple algorithm
[info] - should give correct sum on one node
[info] - should give correct sum on n nodes
[info] ExpensiveSumAlgorithmSpec:
[info] An expensive sum algorithm
[info] - should give correct sum on one node
[info] - should give correct sum on 2 nodes
[info] - should give correct sum on n nodes
[info] Run completed in 664 milliseconds.
[info] Total number of tests run: 5
[info] Suites: completed 2, aborted 0
[info] Tests: succeeded 5, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.