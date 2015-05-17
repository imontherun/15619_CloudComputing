#!/usr/bin/env bash

date
mysqlimport --local --fields-terminated-by='\t' --lines-terminated-by='\n' -uroot -proot --default-character-set=utf8mb4 twitter_db q2.csv
date
#!/usr/bin/env bash

date
mysql -uroot -proot twitter_db -e "create index q2_ut on q2 (usr,time)"
date
