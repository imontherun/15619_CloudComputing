#!/usr/bin/env python

#using built-in function print in python2
from __future__ import print_function

"""
 15-619 Cloud Computing
 Project1.2 Elastic MapReduce
 File: dataProcessing.py
 Name: Huacong Cai
 AndrewID: hcai
 Date: Sep, 13, 2014
"""

""" Process the result of MapReduce, which shows the page info whose total
    views > 100000.
    
    You should provide the path of the input file
    Usage: ./dataProcessing.py filePath
    """
    
import sys

class viewsInfo(dict):
    """ store the views info of a page, including total views and views by date
        page is string, totalViews is integer, dateViews is a list which has the
        format {date1:views, date2:views, ...}"""
    def __init__(self, page, totalViews, dateViews):
        self.page = page
        self.totalViews = totalViews
        self.dateViews = dict()
        for item in dateViews:
            date, views = item.split(':')
            
            try:
                views = int(views)
            except ValueError:
                continue
            
            self.dateViews[date] = views
            

def dataProcessing(inputPath):
    """ Process the inputFile to answer the questions"""
    print()
    
    #store records in the input file
    mostPopular = str()
    mostViews = 0
    viewsDict = dict()
    with open(inputPath, "rt") as inputFile:
        for line in inputFile:
            line = line.strip()
            #delete the '<' and '>' in the line
            line = line.replace('<', '')
            line = line.replace('>', '')
            
            splitResult = line.split('\t')
            
            viewsNum = int(splitResult[0])
            if splitResult[1] not in viewsDict:
                viewsDict[splitResult[1]] = viewsInfo(splitResult[1], 
                                            viewsNum, splitResult[2:])
                if mostViews < viewsNum:
                    mostPopular = splitResult[1]
                    mostViews = viewsNum
            else: #the page has more than one record
                print("%s has duplicate", splitResult[0])
    
    #lines of output file
    print("Q7: %d lines" %(len(viewsDict)), end="\n\n")
    
    #most popular page
    print("Q8: The most popular page is %s" %(mostPopular), end="\n\n")
    
    #most popular page's total views
    print("Q9: Its total views is %d" %(mostViews), end="\n\n")
    
    #sort by total views
    sortByTotal = ("Neymar", "Tim_Howard", "Miroslav_Klose", 
                   "Cristiano_Ronaldo", "Arjen_Robben")
    totalResult = dict()
    for page in sortByTotal:
        totalResult[page] = viewsDict[page].totalViews
    sortedResult = sorted(totalResult.items(), key=lambda x: x[1], reverse = True)
    print("Q10: ", sortedResult, end="\n\n")
    
    #sort by single-day maximum views
    sortByDate = ("Reign_(TV_series)", "Suits_(TV_series)", 
                  "Elementary_(TV_series)", "Arrested_Development_(TV_series)",
                  "The_Walking_Dead_(TV_series)")
    dateResult = dict()
    for page in sortByDate:
        dateResult[page] = max(viewsDict[page].dateViews.values())
    sortedResult = sorted(dateResult.items(), key=lambda x: x[1], reverse = True)
    print("Q11: ", sortedResult, end="\n\n")
    
    #how many days (out of 31) was "Google" more popular than "Amazon"
    days = 0;
    for date in xrange(20140701, 20140732):
        keyDate = str(date)
        if keyDate in viewsDict["Google"].dateViews:
            googleViews = viewsDict["Google"].dateViews[keyDate]
        else:
            googleViews = 0
        
        if keyDate in viewsDict["Amazon.com"].dateViews:
            amazonViews = viewsDict["Amazon.com"].dateViews[keyDate]
        else:
            amazonViews = 0
            
        if googleViews > amazonViews:
            days += 1
    print("Q12: Google is more popular than Amazon: %d days" %(days), end="\n\n")
    
    #date that Dawn_of_the_Planet_of_the_Apes have most views
    mostDate = max(viewsDict["Dawn_of_the_Planet_of_the_Apes"].dateViews.items(),
                   key=lambda x: x[1])
    print("Q13: ", mostDate, end="\n\n")

"""Enterance of the program"""
if __name__ == "__main__":
    if len(sys.argv) >= 2:
        inputPath = sys.argv[1]  
        dataProcessing(inputPath)
    else:
        print("Usage: ./dataProcessing.py filePath")
