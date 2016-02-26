/*
SQLyog Community v11.31 (64 bit)
MySQL - 5.6.16 : Database - bgj
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `stockdailyinfo` */

DROP TABLE IF EXISTS `stockdailyinfo`;

CREATE TABLE `stockdailyinfo` (
  `stockid` varchar(6) NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(10) DEFAULT NULL,
  `currentprice` double DEFAULT NULL,
  `zde` double DEFAULT NULL,
  `zdf` double DEFAULT NULL,
  `zf` double DEFAULT NULL,
  `hsl` double DEFAULT NULL,
  `todayopenprice` double DEFAULT NULL,
  `yesterdaycloseprice` double DEFAULT NULL,
  `highestprice` double DEFAULT NULL,
  `lowestprice` double DEFAULT NULL,
  `cje` double DEFAULT NULL,
  `cjl` double DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lastmodifiedtime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`stockid`,`date`),
  KEY `DateIndex` (`date`),
  KEY `StockIndex` (`stockid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
