#!/usr/bin/env python

import sys

for line in sys.stdin:
	p1,p2=line.split('\t',1)
	p2=p2.replace(' ','\\n')
	print p1+'\t'+p2,
