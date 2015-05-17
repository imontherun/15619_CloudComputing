hadoop fs -mkdir /phase3
hadoop fs -copyFromLocal /mnt/q6 /phase3
hbase org.apache.hadoop.hbase.mapreduce.ImportTsv  -Dimporttsv.columns=HBASE_ROW_KEY,r q6 /phase3/q6
