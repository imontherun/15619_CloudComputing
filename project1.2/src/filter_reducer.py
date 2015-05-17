#!/usr/bin/env python

#using built-in function print in python2
from __future__ import print_function

"""
 15-619 Cloud Computing
 Project1.2 Elastic MapReduce
 File: filter_reducer.py
 Name: Huacong Cai
 AndrewID: hcai
 Date: Sep, 13, 2014
"""
""" Reducer
    
    Aggregate the pageviews from hourly views to daily views from mapper,
    and calculate the total pageviews for each page, print the pages whose
    total views > 100000 with detailed info
    
    """

import sys

#aggregate page views by date
viewsForDate = dict()

for line in sys.stdin:
    line = line.strip()
    page, date, views = line.split('\t')
    
    try:
        views = int(views)
    except ValueError:
        continue
    
    #create new dict item for this date
    if date not in viewsForDate:
        viewsForDate[date] = dict()
    
    if page in viewsForDate[date]:
        viewsForDate[date][page] += views
    else: #new page record
        viewsForDate[date][page] = views

#aggregate page views by month
totalViews = dict()     
  
for date in viewsForDate:
    for page in viewsForDate[date]:
        if page in totalViews:
            totalViews[page] += viewsForDate[date][page]
        else: #new page record
            totalViews[page] = viewsForDate[date][page]

#print pages info whose views > 10000        
for page in totalViews:
    if totalViews[page] > 100000:
        print("%d\t%s\t" %(totalViews[page], page), end='')
        
        for date in xrange(20140701, 20140732): #print record for each date in ascending order
            try:
                dateStr = str(date)
                print("<%s:%d>\t" %(dateStr, viewsForDate[dateStr][page]), end='')
            except KeyError: #this date do not have record of this page
                continue
        print()
        
