#!/usr/bin/env bash

date
mysql -uroot -proot twitter_db -e "create index q3_uid on q3 (usr_id)"
date
