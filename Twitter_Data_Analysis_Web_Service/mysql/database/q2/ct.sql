DROP TABLE IF EXISTS `q2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `q2` (
	`usr` BIGINT(20) UNSIGNED NOT NULL,
	`time` datetime NOT NULL, 
	`resp` text
) ENGINE=InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*mysql -u root -pdb15319root song_db < create_tables.sql*/
