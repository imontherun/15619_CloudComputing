#!/usr/bin/env python

import sys

for line in sys.stdin:
	p1,p2=line.split('\t',1)
	usrid=p1[:-19]
	datetime=p1[-19:]	
	datetime=datetime.replace('+',' ')
	print usrid+"\t"+datetime+"\t"+p2,
