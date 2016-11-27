/**
 * need to  change utf-8 charset
 */

CREATE DATABASE `seek_job` /*!40100 DEFAULT CHARACTER SET latin1 */;
CREATE TABLE `jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `advertiser` varchar(45) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `area` varchar(255) DEFAULT NULL,
  `teaser` varchar(255) DEFAULT NULL,
  `jobid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;

