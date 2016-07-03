# Wordcount_HaMR_SpMR
map with duplicate shakespeare.txt ~13GB

**for Hadoop: ~166 sec**
```
hadoop com.sun.tools.javac.Main WordCount.java

jar cf wc.jar WordCount*.class

hadoop jar wc.jar WordCount /wordcount/bigsp.txt /output/3
```


**for Spark:**

1. all doing tolowercase 
2. replace [^a-zA-z0-9 ]to ""
3. split by " "
4. filter empty string
5. map to (x,1)
6. reduce ((a,b)=>a+b)
7. swap (a,b)=>(b,a)
8. sortBykey
9. take 10

***python:~229 sec***

```
  spark-submit --master yarn-client --num-executors 60  wordcount.py hdfs://InvPM30:9000/wordcount/bbigsp.txt
```
***Java:~109 sec***

```
cd spark/java/JavaSpark/
mvn clean package
spark-submit --master yarn-client --num-executors 60 --class WordCount.JavaSpark.JavaWordCount target/JavaSpark-0.0.1-SNAPSHOT-jar-with-dependencies.jar hdfs://InvPM30:9000/wordcount/bbigsp.txt
```

***scala:~101 sec***
```
cd spark/scala/SparkScala/
mvn clean package
spark-submit --master yarn-client --num-executors 60 --class Wordcount.Spark.SparkScala target/SparkScala-0.0.1-SNAPSHOT-jar-with-dependencies.jar hdfs://InvPM30:9000/wordcount/bbigsp.txt
```

