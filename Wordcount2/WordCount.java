import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.map.InverseMapper;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class WordCount {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable>{

            private final static IntWritable one = new IntWritable(1);
            private Text word = new Text();

            public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
                String cleanstr=value.toString().toLowerCase().replaceAll("[^a-zA-Z0-9 ]","");
                StringTokenizer itr = new StringTokenizer(cleanstr);
                while (itr.hasMoreTokens()) {
                    word.set(itr.nextToken());
                    context.write(word, one);
                }
                    }
    }

    public static class IntSumReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {
            private IntWritable result = new IntWritable();

            public void reduce(Text key, Iterable<IntWritable> values,
                    Context context
                    ) throws IOException, InterruptedException {
                int sum = 0;
                for (IntWritable val : values) {
                    sum += val.get();
                }
                result.set(sum);
                context.write(key, result);
                    }
    }

    private static class IntWritableDecreasingComparator extends IntWritable.Comparator {  
        public int compare(WritableComparable a, WritableComparable b) {  
            return -super.compare(a, b);  
        }  

        public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2) {  
            return -super.compare(b1, s1, l1, b2, s2, l2);  
        }  
    }  
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]+"A"));
        job.setOutputFormatClass(SequenceFileOutputFormat.class);  
        if(job.waitForCompletion(true)){
            Job sortJob = new Job(conf,"sort");
            sortJob.setJarByClass(WordCount.class);
            FileInputFormat.addInputPath(sortJob, new Path(args[1]+"A"));
            sortJob.setInputFormatClass(SequenceFileInputFormat.class);
            sortJob.setMapperClass(InverseMapper.class);
            sortJob.setNumReduceTasks(1);
            FileOutputFormat.setOutputPath(sortJob, new Path(args[1]));
            sortJob.setOutputKeyClass(IntWritable.class); 
            sortJob.setOutputValueClass(Text.class);
            sortJob.setSortComparatorClass(IntWritableDecreasingComparator.class);
            System.exit(sortJob.waitForCompletion(true) ? 0 : 1); 
        }
        FileSystem.get(conf).deleteOnExit(new Path(args[1]+"A"));  
    }
}
