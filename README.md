# Wordcount_HaMR_SpMR
**for Hadoop:**
```
hadoop com.sun.tools.javac.Main WordCount.java

jar cf wc.jar WordCount*.class

hadoop jar wc.jar WordCount /wordcount/bigsp.txt /output/3
```
**for Spark:**
```
python:
  spark-submit --master yarn-client --num-executors 18 --executor-cores 2 wordcount.py hdfs://InvPM30:9000/wordcount/bbigsp.txt
```
