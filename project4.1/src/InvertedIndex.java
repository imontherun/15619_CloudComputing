/*
 * 15-619 Cloud Computing
 * Project4.1
 * File: InvertedIndex.java
 * Name: Huacong Cai
 * AndrewID: hcai
 * Date: Nov, 16, 2014 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.StringUtils;

public class InvertedIndex {
	
	/**
	 * Mapper
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {
		private Text word = new Text();
		private Text location = new Text();
		private Set<String> stopWords = new HashSet<String>();

		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			// get the file name
			FileSplit fs = (FileSplit) context.getInputSplit();
			location.set(fs.getPath().getName());

			// split the input
			String line = value.toString();
			line = line.replaceAll("[^a-zA-Z0-9\']+", " "); //remove punctuation except '
			StringTokenizer tokenizer = new StringTokenizer(line);

			while (tokenizer.hasMoreTokens()) {
				String tempWord = tokenizer.nextToken().toLowerCase(); //case-insensitive

				if (!stopWords.contains(tempWord)) { //not a stop word
					tempWord = tempWord.replaceAll("\'+", " "); //remove '
					//resplit words
					StringTokenizer tokenizer2 = new StringTokenizer(tempWord);
					while (tokenizer2.hasMoreTokens()) {
						String tempWord2 = tokenizer2.nextToken();
						word.set(tempWord2);
						context.write(word, location);
					}
				}
			}
		}
		
		//set up distributed cache
		protected void setup(Context context)  {
			Configuration conf = context.getConfiguration();
			if (conf.getBoolean("invertedindex.skip.patterns", false)) {
				Path[] patternsFiles = new Path[0];
				
				try {
					patternsFiles = DistributedCache.getLocalCacheFiles(conf);
				} catch (IOException ioe) {
					System.err.println("Caught exception while getting cached files: "
									+ StringUtils.stringifyException(ioe));
				}
				
				for (Path patternsFile : patternsFiles) {
					parseSkipFile(patternsFile);
				}
			}
		}
		
		//load stop words list
		private void parseSkipFile(Path patternsFile) {
			try {
				BufferedReader fis = new BufferedReader(new FileReader(patternsFile.toString()));
				String stopWord = null;
				while ((stopWord = fis.readLine()) != null) {
					stopWords.add(stopWord);
				}
				fis.close();
			} catch (IOException ioe) {
				System.err.println("Caught exception while parsing the cached file '"
								+ patternsFile + "' : " + StringUtils.stringifyException(ioe));
			}
		}
	}
	
	/**
	 * Reducer
	 */
	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		private Text word = new Text();
		private Text list = new Text();
		
		public void reduce(Text key, Iterable<Text> filenames, Context context)
				throws IOException, InterruptedException {

			Set<String> filenameSet = new HashSet<String>();
			for (Text filename : filenames) {
				filenameSet.add(filename.toString());
			}

			String tempWord = key.toString() + ":";
			StringBuilder filelist = new StringBuilder();
			Iterator<String> iter = filenameSet.iterator();

			if (iter.hasNext()) // first file name
				filelist.append(iter.next());
			while (iter.hasNext()) { // the other file name
				filelist.append(", ");
				filelist.append(iter.next());
			}
			
			word.set(tempWord);
			list.set(filelist.toString());
			context.write(word, list);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		
		//distributed cache
		for (int i = 0; i < args.length; ++i) {
			if ("-skip".equals(args[i])) {
				DistributedCache.addCacheFile(new Path(args[++i]).toUri(), conf);
				conf.setBoolean("invertedindex.skip.patterns", true);
			}
		}
		
		Job job = new Job(conf, "inverted_index");
		
		job.setJarByClass(InvertedIndex.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}
}
