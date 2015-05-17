#!/usr/bin/env bash

#s3cmd get s3://testq4/output42/*

#cat part* | ./seperate.py > q4.csv
#rm part*

mysql -uroot -proot twitter_db < ct.sql
echo "start load q4"
date
mysqlimport --local --fields-terminated-by='\t' --lines-terminated-by='\n' -uroot -proot --default-character-set=utf8mb4 twitter_db q4.csv
date
echo "load q4 completed"

echo "start q4 index"
date
mysql -uroot -proot twitter_db -e "create index q4_tl on q4 (time,location)"
date
echo "finshi q4 index"
