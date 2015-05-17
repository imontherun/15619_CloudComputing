#!/usr/bin/env python

import sys
for line in sys.stdin:
	rank=1
	line=line.strip()
	time,loc,resp = line.split('\t')
	hashtags = resp.split('\\n')
	for hashtag in hashtags:
		print time+'\t'+loc+'\t'+str(rank)+'\t'+hashtag
		rank = rank + 1
 
