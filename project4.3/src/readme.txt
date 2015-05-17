run the program:

hadoop jar LanguageModel.jar [Generic options] [-t minCount] [-n maxWords] /input

-t minCount, phrases that appear below this threshold will be ignored

-n maxWords, HBase store only the top maxWords words with the highest probabilities

/input is the input path on the hdfs