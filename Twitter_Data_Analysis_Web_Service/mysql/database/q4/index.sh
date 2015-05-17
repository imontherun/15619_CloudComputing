#!/usr/bin/env bash

date
mysql -uroot -proot twitter_db -e "create index q4_tl on q4 (time,location)"
date
