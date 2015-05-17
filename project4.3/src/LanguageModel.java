/*
 * 15-619 Cloud Computing
 * Project4.3
 * File: LanguageModel.java
 * Name: Huacong Cai
 * AndrewID: hcai
 * Date: Nov, 29, 2014 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class LanguageModel {
	private static final int defaultMinCount = 2;
	private static final int defaultMaxWords = 5;
	
	/**
	 * Mapper
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		private Text prefix = new Text();
		private Text wordAndCount = new Text();
		private int minCount; //phrases that appear below this threshold will be ignored
		
		/**
		 * set up
		 */
		protected void setup(Context context)  {
			Configuration conf = context.getConfiguration();
			this.minCount = conf.getInt("min.count", defaultMinCount); //get min count
		}
		
		/**
		 * map
		 * Split the "phrase \t count" into "prefix \t lastWord count"
		 */
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {			
			String line = value.toString();
			
			//split the input into phrase and count
			String[] strSplit = line.split("\t");
			String phraseStr = strSplit[0];
			String countStr = strSplit[1];
			int count = Integer.valueOf(countStr);
			
			if (count < this.minCount) //phrase appear below threshold
				return;
			
			//split the phrase into prefix and last word
            int endIndex = phraseStr.lastIndexOf(" ");
            
            if (endIndex == -1) //phrase only consists of one word
            	return;
            
            String prefixStr = phraseStr.substring(0, endIndex).trim();
            String lastWordStr = phraseStr.substring(endIndex+1).trim();
            
            //output result
            prefix.set(prefixStr);
            wordAndCount.set(lastWordStr + " " + countStr);
            
            context.write(prefix, wordAndCount);
		}
	}
	
	/**
	 * Reducer
	 */
	public static class Reduce extends TableReducer<Text, Text, ImmutableBytesWritable> {
		private int maxWords; //HBase store only the top maxWords words with the highest probabilities
		private byte[] columnFamily = "wordpairs".getBytes();
		
		/**
		 * set up
		 */
		protected void setup(Context context)  {
			Configuration conf = context.getConfiguration();
			this.maxWords = conf.getInt("max.words", defaultMaxWords); //get max words
		}
		
		/**
		 * reduce
		 * Gather words follow the phrase (key) and store them to HBase with their probabilities
		 */
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
            
            List<WordAndCount> wordList = new ArrayList<WordAndCount>();
            int prefixCount = 0;
            
            //add words and counts into list
            for (Text value : values) {
            	String[] strSplit = value.toString().split(" ");
            	int wordCount = Integer.valueOf(strSplit[1]);
            	wordList.add(new WordAndCount(strSplit[0], wordCount));
            	
            	prefixCount += wordCount;
            }

            //sort the words according to their counts
            Collections.sort(wordList);

            //get the num of columns in this row
            int totalColumn = wordList.size() >= this.maxWords ? this.maxWords : wordList.size();

            Put put = new Put(Bytes.toBytes(key.toString()));

            for (int i = 0; i < totalColumn; ++i) {
                double probability = wordList.get(i).count * 100.0 / prefixCount; //percentage
                put.add(this.columnFamily,
                		wordList.get(i).word.getBytes(),
        	            String.valueOf(probability).getBytes());
            }

            context.write(null, put);
		}
	}
	
	/**
	 * Store word and its count, also implement Comparable
	 * @author chc
	 *
	 */
	private static class WordAndCount implements Comparable<WordAndCount> {
		public String word;
		public int count;
		
		public WordAndCount (String word, int count) {
			this.word = word;
			this.count = count;
		}
		
		@Override
		public int compareTo(WordAndCount o) {
			return -Integer.compare(this.count, o.count); //- to implement descending order
		}
		
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = HBaseConfiguration.create();
		
		Options opts = new Options();
		opts.addOption("t", true, "phrases that appear below this threshold will be ignored");
		opts.addOption("n", true, "HBase store only the top maxWords words with the highest probabilities");
		
		GenericOptionsParser optionParser = new GenericOptionsParser(conf, opts, args);
	    String[] remainingArgs = optionParser.getRemainingArgs();
	    
	    if (remainingArgs.length != 1) {
	    	System.err.println();
	    	System.err.println("Usage: hadoop jar LanguageModel.jar [Generic options] [-t minCount] [-n maxWords] <input path>");
	    	System.err.println();
	    	System.err.println("-t minCount, phrases that appear below this threshold will be ignored");
	    	System.err.println("-n maxWords, HBase store only the top maxWords words with the highest probabilities");
	    	System.err.println();
	    	GenericOptionsParser.printGenericCommandUsage(System.out);
	    	System.exit(2);
	    }
	    
	    //parse parameters
	    CommandLine cmd = optionParser.getCommandLine();
	    
	    if (cmd.hasOption("t"))
	    	conf.setInt("min.count", Integer.valueOf(cmd.getOptionValue("t")));
	    
	    if (cmd.hasOption("n"))
	    	conf.setInt("max.words", Integer.valueOf(cmd.getOptionValue("n")));
	    
	    
		Job job = Job.getInstance(conf, "language_model");
		
		job.setJarByClass(LanguageModel.class);

		job.setMapperClass(Map.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(remainingArgs[0]));

		TableMapReduceUtil.initTableReducerJob(
				"wp",			//output table
				Reduce.class,	//reducer class
				job);
		
		job.setInputFormatClass(TextInputFormat.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
