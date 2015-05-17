#!/usr/bin/env python

import sys

for line in sys.stdin:
	p1,p2=line.split('\t',1)
	print p1[:10]+"\t"+p1[10:]+"\t"+p2,
