/*
 * 15-619 Cloud Computing
 * Project4.2
 * File: NGram.java
 * Name: Huacong Cai
 * AndrewID: hcai
 * Date: Nov, 22, 2014 
 */

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class NGram {
	
	/**
	 * Mapper
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		private Text word = new Text();
		private IntWritable one = new IntWritable(1);

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {			
			String line = value.toString();
			line = line.replaceAll("[^a-zA-Z]+", " "); //remove non-alphabetical characters
			line = line.toLowerCase(); //case-insensitive
			
			//split the input
			StringTokenizer tokenizer = new StringTokenizer(line);
			String[] words = new String[tokenizer.countTokens()];
			for (int i=0; i<words.length; ++i)
				words[i] = tokenizer.nextToken();

			for (int i=0; i<words.length; ++i) {
				//1-gram
				word.set(words[i]); //reuse variable, avoid garbage collection
				context.write(word, one);
				
				//2-gram
				if (i+1 < words.length) {
					word.set(words[i] + " " + words[i+1]);
					context.write(word, one);
				}
				
				//3-gram
				if (i+2 < words.length) {
					word.set(words[i] + " " + words[i+1] + " " + words[i+2]);
					context.write(word, one);
				}
				
				//4-gram
				if (i+3 < words.length) {
					word.set(words[i] + " " + words[i+1] + " " + words[i+2]
							+ " " + words[i+3]);
					context.write(word, one);
				}
				
				//5-gram
				if (i+4 < words.length) {
					word.set(words[i] + " " + words[i+1] + " " + words[i+2]
							+ " " + words[i+3] + " " + words[i+4]);
					context.write(word, one);
				}
			}
		}
	}
	
	/**
	 * Reducer
	 */
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable sumIntWritable = new IntWritable();
		
		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			sumIntWritable.set(sum); //reuse variable, avoid garbage collection
			context.write(key, sumIntWritable);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		Job job = Job.getInstance(conf, "n_gram");
		
		job.setJarByClass(NGram.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(Map.class);
		job.setCombinerClass(Reduce.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
