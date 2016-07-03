# Wordcount_HaMR_SpMR
map with duplicate shakespeare.txt ~13GB

**for Hadoop: ~166 sec**
```
hadoop com.sun.tools.javac.Main WordCount.java

jar cf wc.jar WordCount*.class

hadoop jar wc.jar WordCount /wordcount/bigsp.txt /output/3
```
**for Spark:**
***python:~229 sec***

```
  spark-submit --master yarn-client --num-executors 60  wordcount.py hdfs://InvPM30:9000/wordcount/bbigsp.txt
```
***Java:~113 sec***
```
  spark-submit --master yarn-client --num-executors 60 --class WordCount.JavaSpark.JavaWordCount target/JavaSpark-0.0.1-SNAPSHOT-jar-with-dependencies.jar hdfs://InvPM30:9000/wordcount/bbigsp.txt
```

***scala:~101 sec***
```
  spark-submit --master yarn-client --num-executors 60 --class Wordcount.Spark.SparkScala target/SparkScala-0.0.1-SNAPSHOT-jar-with-dependencies.jar hdfs://InvPM30:9000/wordcount/bbigsp.txt
```

