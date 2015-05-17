#!/usr/bin/env bash

date
mysql -uroot -proot twitter_db -e "create index q2_ut on q2 (time,usr)"
date
