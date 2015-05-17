#!/usr/bin/env bash

#s3cmd get s3://testq3/output42/*

#cat part* | ./seperate.py > q3.csv
#rm part*

mysql -uroot -proot twitter_db < ct.sql
echo "start load q3"
date
mysqlimport --local --fields-terminated-by='\t' --lines-terminated-by='\n' -uroot -proot --default-character-set=utf8mb4 twitter_db q3.csv
date
echo "load q3 completed"

echo "start q3 index"
date
mysql -uroot -proot twitter_db -e "create index q3_usr on q3 (usr_id)"
date
echo "finsh q3 index"
