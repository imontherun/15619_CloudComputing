# Twitter Data Analysis Web Service - HBase & MySQL

15619 team project
Team: sudoCloud
Member1: Qinyu Tong - qtong, database, github:@ironAtom
Member2: Huacong Cai - hcai, frontend
Member3: Hansi Mou - hmou, ETL

## Query type

Query1: Heartbeat and Authentication

The query asks about the state of the web service. The front end server responds with the project team id, AWS account ids, the current timestamp and the authentication key.

Query2: Text Cleaning and Analysis

The query asks for the tweet(s) posted by a given user at a specific time. And server response with:
a. The tweet ID
b. The censored text
c. The sentiment score of that tweet

Query3: Retweet Buddies

The query asks for information about how popular a particular users’ tweets are. And the server responds with a list of users who have retweeted any of the tweets posted by the given user.

Query4: Whats Happening Here

The query asks for the most popular ‘n’ hashtag(s) from all tweets from a single location on a single day.

Query5: Hot Or Not

The query asks the “hotter” user amongst two users provided. The Hotness Quotient is determined by the following:
Score 1: Each tweet (#tweets): +1
Score 2: Every time this user’s tweet was retweeted (#retweets): +3 Score3:Everyuniquepersonwhoretweetedthisuser’stweet (#retweeters):+10 Score: S1 + S2 + S3

Query6: Shutter Count

The query asks the number of photos (entities -> media --> type == photo) uploaded by users within a range of userids. 