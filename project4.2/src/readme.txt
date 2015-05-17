run the program:

hadoop jar InvertedIndex.jar /input /output -skip /stop.txt

/input is the input path on the hdfs

/output is the output path on the hdfs, which should not exist before running the job

-skip is the option to remove stop words, path of stop words list is needed

/stop.txt is the stop words list which will be removed after processing