#!/usr/bin/env python

#using built-in function print in python2
from __future__ import print_function

"""
 15-619 Cloud Computing
 Project1.2 Elastic MapReduce
 File: filter_mapper.py
 Name: Huacong Cai
 AndrewID: hcai
 Date: Sep, 13, 2014
"""
""" Mapper

    Filter the specific lines of the page view statistics of Wikimedia,
    and print the key-value: "pagename    date    views" to reducer, the
    views are hourly views
    
    Logs format:
    <project name> <page title> <number of accesses> <total data returned in bytes>

    Filtering rules:
    1.Project name is not en
    2.Page title that starts with excludeStart
    3.Page title that starts with lowercase English characters
    4.Page title that ends with excludeExtensions
    5.Page title that exactly match the excludeTitles"""

import sys
import os

excludeStart = ("Media:", "Special:", "Talk:", "User:", "User_talk:", 
                "Project:", "Project_talk:", "File:", "File_talk:",
                "MediaWiki:", "MediaWiki_talk:", "Template:", "Template_talk:",
                "Help:", "Help_talk:", "Category:", "Category_talk:",
                "Portal:", "Wikipedia:", "Wikipedia_talk:")

excludeExtensions = (".jpg", ".gif", ".png", ".JPG", ".GIF", ".PNG", ".txt", 
                     ".ico")

excludeTitles = ("404_error/", "Main_Page", "Hypertext_Transfer_Protocol",
                 "Favicon.ico", "Search")

for line in sys.stdin:
    line = line.strip()
    result = line.split()
    
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
    if result[1][-4:] in excludeExtensions:
        continue
    
    #special articles
    if result[1] in excludeTitles:
        continue
    
    fileName =  os.environ["map_input_file"]
    #input file including three '-'
    #e.g., s3://wikipediatraf/201407-gz/pagecounts-20140701-000000.gz
    date = fileName.split('-')[2]

    print("%s\t%s\t%s" %(result[1], date, result[2]))    
