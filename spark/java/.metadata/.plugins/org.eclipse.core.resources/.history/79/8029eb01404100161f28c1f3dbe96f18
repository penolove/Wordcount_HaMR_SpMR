package WordCount.JavaSpark;
import scala.Tuple2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
/**
 * Hello world!
 *
 */
public class JavaWordCount 
{	private static final Pattern SPACE = Pattern.compile(" ");
    public static void main( String[] args )
    {

        if (args.length < 1) {
          System.err.println("Usage: JavaWordCount <file>");
          System.exit(1);
        }

        SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount");
        JavaSparkContext ctx = new JavaSparkContext(sparkConf);
        JavaRDD<String> lines = ctx.textFile(args[0], 1);

    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      public Iterable<String> call(String s) {
        return Arrays.asList(SPACE.split(s.toLowerCase().replaceAll("[^a-zA-Z0-9 ]","")));
      }
    });


    JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
      public Tuple2<String, Integer> call(String s) {
        return new Tuple2<String, Integer>(s, 1);
      }
    });

    JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {

      public Integer call(Integer i1, Integer i2) {
        return i1 + i2;
      }
    });

	JavaPairRDD<Integer, String> swappedPair = counts.mapToPair(new PairFunction<Tuple2<String, Integer>, Integer, String>() {
		   public Tuple2<Integer, String> call(Tuple2<String, Integer> item) throws Exception {
		       return item.swap();
		   }

		});

	JavaPairRDD<Integer, String> sortbySP=swappedPair.sortByKey(false);

        List<Tuple2<Integer, String>> output = sortbySP.take(10);
        for (Tuple2<?,?> tuple : output) {
          System.out.println(tuple._1() + ": " + tuple._2());
        }
        ctx.stop();
    }
}
