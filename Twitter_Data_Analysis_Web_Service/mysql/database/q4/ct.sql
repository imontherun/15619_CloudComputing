DROP TABLE IF EXISTS `q4`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `q4` (
	`time` date NOT NULL, 
	`location` varchar(65) NOT NULL,
	`rank` int NOT NULL,
	`resp` MEDIUMTEXT
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;
