#!/usr/bin/env bash

date
mysqlimport --local --fields-terminated-by='\t' --lines-terminated-by='\n' -uroot -proot --default-character-set=utf8mb4 twitter_db q3.csv
date
