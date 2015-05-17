#!/usr/bin/env python
from __future__ import print_function
"""
 15-619 Cloud Computing
 Project1.1 Sequential Analysis
 Name: Huacong Cai
 AndrewID: hcai
 Date: Sep, 5, 2014
"""

""" This is the "dataFilter.py" program. It provides one function called 
    dataFilter(inputPath, outputPath), which filter the page view statistics
    of Wikimedia, and print the statistics info. It could be run through 
    command line with/without specify the input file path and output file path
    
    usage(command line): python3 dataFilter.py [inputPath [outputPath]]
    
    The default inputPath is ./pagecounts-20140701-000000 and the default
    outputPath is inputPath + "_filtering"
    """

import sys
import time

excludeStart = ("Media:", "Special:", "Talk:", "User:", "User_talk:", 
                "Project:", "Project_talk:", "File:", "File_talk:",
                "MediaWiki:", "MediaWiki_talk:", "Template:", "Template_talk:",
                "Help:", "Help_talk:", "Category:", "Category_talk:",
                "Portal:", "Wikipedia:", "Wikipedia_talk:")

excludeExtensions = (".jpg", ".gif", ".png", ".JPG", ".GIF", ".PNG", ".txt", 
                     ".ico")

excludeTitles = ("404_error/", "Main_Page", "Hypertext_Transfer_Protocol",
                 "Favicon.ico", "Search")

def dataFilter(inputPath, outputPath):
    """ Filter the specific lines of the page view statistics of Wikimedia,
        and print the statistics info.
        
        The first argument is the path of the input file, and the second
        argument is the path of the output file
        
        Logs format:
        <project name> <page title> <number of accesses> <total data returned in bytes>

        Filtering rules:
        1.Project name is not en
        2.Page title that starts with excludeStart
        3.Page title that starts with lowercase English characters
        4.Page title that ends with excludeExtensions
        5.Page title that exactly match the excludeTitles"""
        
    try:
        numOfLines = 0
        numOfRequests = 0
        numOfFilterLines = 0
        filterResult = list()
        #encoding = "utf-8", errors = "surrogateescape"
        inputFile = open(inputPath, "rt")
        for line in inputFile:
            result = line.split()
            numOfLines += 1
            numOfRequests += int(result[2])
            
            #not English wikipedia
            if not result[0] == "en":
                continue
            
            #special start
            if result[1].startswith(excludeStart):
                continue
            
            #start with lowercase English characters
            if result[1][0].islower():
                continue
            
            #special extensions
            if result[1].endswith(excludeExtensions):
                continue
            
            #special articles
            if result[1] in excludeTitles:
                continue
            
            numOfFilterLines += 1
            filterResult.append({"name":result[1], "views":int(result[2])})
                
                #print(result[1] + "\t" + result[2], file = outputFile)
        
        print("Total number of lines is %d" %(numOfLines))
        print("Total number of requests is %d" %(numOfRequests))
        print("Total number of lines after filter is %d" %(numOfFilterLines))
        sortedResult = sorted(filterResult, key = lambda x:x["views"], reverse = True)
        #errors = "surrogateescape"
        outputFile = open(outputPath, "wt")
        for item in sortedResult:
            print("%s\t%d" %(item["name"], item["views"]), file = outputFile)
                
        print("The most popular article is %s. It gets %d views" 
              %(sortedResult[0]["name"], sortedResult[0]["views"]))
                
    except IOError as err:
        print("File error: " + str(err))
    
    finally:
        if "inputFile" in locals():
            inputFile.close()
        if "outputFile" in locals():
            outputFile.close()

"""Enterance of the program"""
if __name__ == "__main__":
    startTime = time.clock()
    
    inputPath = "pagecounts-20140701-000000"
    
    if len(sys.argv) >= 2:
        inputPath = sys.argv[1]
    
    outputPath = inputPath + "_filtering"
        
    if len(sys.argv) >= 3:
        outputPath = sys.argv[2]
        
    dataFilter(inputPath, outputPath)
    
    endTime = time.clock()
    print("Process time of this file is %f secs" %(endTime - startTime))
        
