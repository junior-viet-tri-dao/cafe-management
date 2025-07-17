CREATE DATABASE  IF NOT EXISTS `cafe_v2` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cafe_v2`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: cafe_v2
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `permissions` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`account_id`),
  UNIQUE KEY `UKk8h1bgqoplx0rkngj01pm1rgp` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (1,_binary '\0','https://dummyimage.com/764x563','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','ADMIN','admin'),(2,_binary '\0','https://dummyimage.com/764x563','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','user'),(3,_binary '\0','https://dummyimage.com/780x257','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','phamminhduc'),(4,_binary '\0','https://placekitten.com/663/539','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','lethihong'),(5,_binary '\0','https://placekitten.com/105/575','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','hodangkhoa'),(6,_binary '\0','https://www.lorempixel.com/883/251','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','vuthanhcong'),(7,_binary '\0','https://dummyimage.com/779x129','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','tranthingoc'),(8,_binary '\0','https://www.lorempixel.com/644/577','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','nguyenba'),(9,_binary '\0','https://placekitten.com/54/139','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','phamthithuy'),(10,_binary '\0','https://dummyimage.com/368x441','$2a$08$trXNcE0TsX2EkmNQo/LeVuoyvj0fQgSa5g9ZclKkY7gavtysjZzZG','USER','buiquanghuy');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  `position_id` int DEFAULT NULL,
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `UKguw5r3c5brob94evptgkjl9hr` (`account_id`),
  KEY `FKngcpgx7fx5kednw3m7u0u8of3` (`position_id`),
  CONSTRAINT `FK8dwhb0qmor08fl06pde1bef3c` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
  CONSTRAINT `FKngcpgx7fx5kednw3m7u0u8of3` FOREIGN KEY (`position_id`) REFERENCES `positions` (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'78 Nguyễn Văn Cừ, Quận 5, TP.HCM',_binary '\0','Nguyễn Văn Hùng','0338641606',1,9),(2,'550 Lê Lợi, Quận 1, TP.HCM',_binary '\0','Trần Quốc Tuấn','0338641601',2,9),(3,'420 Trần Phú, Quận Hải Châu, Đà Nẵng',_binary '\0','Phạm Minh Tâm','0338641602',3,1),(4,'543 Phạm Văn Đồng, Quận Cầu Giấy, Hà Nội',_binary '\0','Lê Quang Vinh','0338641603',4,8),(5,'738 Nguyễn Trãi, Quận 7, TP.HCM',_binary '\0','Hoàng Thị Mai','0338641604',5,10),(6,'856 Lý Thường Kiệt, TP. Cần Thơ',_binary '\0','Vũ Thị Lan','0338641605',6,6),(7,'198 Hai Bà Trưng, TP. Đà Lạt',_binary '\0','Bùi Văn Nam','0338641607',7,8),(8,'795 Trường Chinh, TP. Hải Phòng',_binary '\0','Nguyễn Thị Hồng','0338641608',8,3),(9,'352 Nguyễn Huệ, TP. Nha Trang',_binary '\0','Trần Văn Long','0338641609',9,10),(10,'856 Võ Văn Tần, TP. Vũng Tàu',_binary '\0','Phạm Quốc Dũng','0338641611',10,3);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `equipment_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `equipment_name` varchar(255) DEFAULT NULL,
  `purchase_date` date DEFAULT NULL,
  `purchase_price` decimal(15,0) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `total_amount` decimal(15,0) DEFAULT NULL,
  PRIMARY KEY (`equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (12,_binary '\0','Máy pha cà phê','2025-07-01',17000000,2,34000000),(13,_binary '\0','a2','1111-01-01',400000,10,4000000),(15,_binary '\0','Máy rửa chén','2025-07-10',17000000,2,34000000),(16,_binary '\0','Hello','2025-07-10',2000000,1,2000000),(17,_binary '\0','Quạt máy lớn','2025-07-10',300000,5,1500000);
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `expenses`
--

DROP TABLE IF EXISTS `expenses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `expenses` (
  `expense_id` int NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `expense_date` date DEFAULT NULL,
  `expense_name` varchar(255) DEFAULT NULL,
  `account_id` int DEFAULT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `FKn24s4d0wvf06dfuslsbcjy8po` (`account_id`),
  CONSTRAINT `FKn24s4d0wvf06dfuslsbcjy8po` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=250002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `expenses`
--

LOCK TABLES `expenses` WRITE;
/*!40000 ALTER TABLE `expenses` DISABLE KEYS */;
INSERT INTO `expenses` VALUES (1,150000,_binary '\0','2025-07-02','helo',1),(250001,150000,_binary '\0','2025-07-02','132',1);
/*!40000 ALTER TABLE `expenses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exports`
--

DROP TABLE IF EXISTS `exports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exports` (
  `exports_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `export_date` date DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `total_export_amount` double DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`exports_id`),
  KEY `FKs7o8jhojdttbj46cafvq0t8n7` (`employee_id`),
  KEY `FK712nnq5e9csl024e80u917a0i` (`product_id`),
  CONSTRAINT `FK712nnq5e9csl024e80u917a0i` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `FKs7o8jhojdttbj46cafvq0t8n7` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exports`
--

LOCK TABLES `exports` WRITE;
/*!40000 ALTER TABLE `exports` DISABLE KEYS */;
INSERT INTO `exports` VALUES (11,_binary '\0','2025-07-03',10,NULL,1,1);
/*!40000 ALTER TABLE `exports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imports`
--

DROP TABLE IF EXISTS `imports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imports` (
  `imports_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `import_date` date DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `total_amount` decimal(15,0) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`imports_id`),
  KEY `FKldt5y2glovutfcbiy9ldu94by` (`employee_id`),
  KEY `FK1gmcvbxpy4y6s79lfb3q98w7w` (`product_id`),
  CONSTRAINT `FK1gmcvbxpy4y6s79lfb3q98w7w` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `FKldt5y2glovutfcbiy9ldu94by` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imports`
--

LOCK TABLES `imports` WRITE;
/*!40000 ALTER TABLE `imports` DISABLE KEYS */;
INSERT INTO `imports` VALUES (12,_binary '\0','2025-07-01',10,500000,1,1);
/*!40000 ALTER TABLE `imports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_details`
--

DROP TABLE IF EXISTS `invoice_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice_details` (
  `is_deleted` bit(1) DEFAULT NULL,
  `price_at_sale_time` decimal(15,0) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `invoice_id` int NOT NULL,
  `menu_item_id` int NOT NULL,
  PRIMARY KEY (`invoice_id`,`menu_item_id`),
  KEY `FKfo4cyx0oxrsf2v06nlypgt537` (`menu_item_id`),
  CONSTRAINT `FK439lfpbc6j1k0cn26wtp8f96r` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`invoice_id`),
  CONSTRAINT `FKfo4cyx0oxrsf2v06nlypgt537` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`menu_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_details`
--

LOCK TABLES `invoice_details` WRITE;
/*!40000 ALTER TABLE `invoice_details` DISABLE KEYS */;
INSERT INTO `invoice_details` VALUES (_binary '\0',50000,5,176,1),(_binary '\0',50000,5,177,1),(_binary '\0',50,10,178,1),(_binary '\0',50,2,178,2),(_binary '\0',50000,2,179,1),(_binary '\0',40000,2,179,10);
/*!40000 ALTER TABLE `invoice_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `invoice_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `total_amount` decimal(15,0) DEFAULT NULL,
  `promotion_id` int DEFAULT NULL,
  `status` enum('UNPAID','PAID','MERGE_TABLE','CANCELLED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`invoice_id`),
  KEY `FKsev3d3jlk86toj8l50myia2ts` (`promotion_id`),
  CONSTRAINT `FKsev3d3jlk86toj8l50myia2ts` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (176,'2025-07-03 18:14:10.858156',_binary '\0',250000,NULL,'PAID'),(177,'2025-07-10 22:33:55.377940',_binary '\0',250000,NULL,'UNPAID'),(178,'2025-07-10 22:58:32.670682',_binary '\0',600,NULL,'PAID'),(179,'2025-07-10 23:01:32.272464',_binary '\0',180000,NULL,'UNPAID');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_item_ingredients`
--

DROP TABLE IF EXISTS `menu_item_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_item_ingredients` (
  `is_deleted` bit(1) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `menu_item_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`menu_item_id`,`product_id`),
  KEY `FKfgqds9o0jawovc4jrle80ys4l` (`product_id`),
  CONSTRAINT `FKbrorwv22pmcc7k7icifrdq3kw` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`menu_item_id`),
  CONSTRAINT `FKfgqds9o0jawovc4jrle80ys4l` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_item_ingredients`
--

LOCK TABLES `menu_item_ingredients` WRITE;
/*!40000 ALTER TABLE `menu_item_ingredients` DISABLE KEYS */;
INSERT INTO `menu_item_ingredients` VALUES (_binary '',3.11,'cái',1,9),(_binary '\0',1.27,'ml',2,3),(_binary '\0',1.55,'kg',3,10),(_binary '',3.61,'ml',4,5),(_binary '\0',3.01,'ml',5,3),(_binary '',2.81,'ml',6,7),(_binary '\0',4.16,'kg',7,10),(_binary '\0',2.42,'cái',8,2),(_binary '',1.62,'kg',9,10),(_binary '',1.74,'kg',10,10),(_binary '\0',1,'1',11,1),(_binary '\0',5,'ml',12,2),(_binary '\0',10,'ml',12,6),(_binary '\0',2,'ml',13,1),(_binary '\0',1,'ml',13,2);
/*!40000 ALTER TABLE `menu_item_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_items`
--

DROP TABLE IF EXISTS `menu_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_items` (
  `menu_item_id` int NOT NULL AUTO_INCREMENT,
  `current_price` decimal(15,0) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `item_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`menu_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_items`
--

LOCK TABLES `menu_items` WRITE;
/*!40000 ALTER TABLE `menu_items` DISABLE KEYS */;
INSERT INTO `menu_items` VALUES (1,50000,_binary '\0','Cà phê đen'),(2,50000,_binary '\0','Trà đào cam sả'),(3,50000,_binary '\0','Cà phê sữa đá'),(4,50000,_binary '\0','Latte nghệ'),(5,50000,_binary '\0','Trà xanh matcha'),(6,50000,_binary '\0','Bánh mì trứng'),(7,40000,_binary '\0','Bánh tiramisu'),(8,40000,_binary '\0','Sinh tố xoài'),(9,40000,_binary '\0','Nước ép cam'),(10,40000,_binary '\0','Bánh donut'),(11,40000,_binary '\0','Matcha Latte'),(12,450000,_binary '\0','Mattcha Lattte V2'),(13,500000,_binary '\0','Thịt Anh Cao Ver2');
/*!40000 ALTER TABLE `menu_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `positions`
--

DROP TABLE IF EXISTS `positions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `positions` (
  `position_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `position_name` varchar(255) DEFAULT NULL,
  `salary` int DEFAULT NULL,
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `positions`
--

LOCK TABLES `positions` WRITE;
/*!40000 ALTER TABLE `positions` DISABLE KEYS */;
INSERT INTO `positions` VALUES (1,_binary '\0','Quản lý quán cà phê',1200000000),(2,_binary '\0','Nhân viên pha chế',8000000),(3,_binary '\0','Nhân viên phục vụ',7000000),(4,_binary '\0','Kế toán trưởng',10000000),(5,_binary '\0','Nhân viên thu ngân',7500000),(6,_binary '\0','Nhân viên vệ sinh',6000000),(7,_binary '\0','Nhân viên marketing',9000000),(8,_binary '\0','Nhân viên giao hàng',7000000),(9,_binary '\0','Trưởng ca',9500000),(10,_binary '\0','Nhân viên kho',6500000);
/*!40000 ALTER TABLE `positions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `unit_id` int DEFAULT NULL,
  `price` decimal(15,0) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `FKeex0i50vfsa5imebrfdiyhmp9` (`unit_id`),
  CONSTRAINT `FKeex0i50vfsa5imebrfdiyhmp9` FOREIGN KEY (`unit_id`) REFERENCES `units` (`unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,_binary '\0','Hạt cà phê Arabica',0,5,50000),(2,_binary '\0','Sữa tươi',0,2,50000),(3,_binary '\0','Đường cát trắng',0,5,50000),(4,_binary '\0','Sirô vani',0,5,50000),(5,_binary '\0','Bột cacao',0,6,50000),(6,_binary '\0','Trà xanh matcha',0,9,50000),(7,_binary '\0','Kem tươi',0,7,50000),(8,_binary '\0','Hạt cà phê Robusta',0,1,50000),(9,_binary '\0','Siro caramel',0,8,50000),(10,_binary '\0','Bột quế',0,2,50000);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotions`
--

DROP TABLE IF EXISTS `promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotions` (
  `promotion_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `discount_value` double DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `promotion_name` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  PRIMARY KEY (`promotion_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotions`
--

LOCK TABLES `promotions` WRITE;
/*!40000 ALTER TABLE `promotions` DISABLE KEYS */;
INSERT INTO `promotions` VALUES (1,_binary '\0',25.15,'2025-05-09','Khuyến mãi Buổi sáng','2024-12-19'),(2,_binary '\0',13.97,'2025-06-19','Khuyến mãi Trà sữa','2024-09-06'),(3,_binary '\0',6.76,'2025-06-22','Khuyến mãi Sinh viên','2024-11-06'),(4,_binary '\0',15.27,'2025-06-01','Khuyến mãi Combo','2024-12-15'),(5,_binary '',29.97,'2025-04-30','Khuyến mãi Lễ hội','2024-09-08'),(6,_binary '',49.78,'2025-04-03','Khuyến mãi Mùa đông','2024-07-02'),(7,_binary '',43.06,'2025-06-22','Khuyến mãi VIP','2024-09-12'),(8,_binary '\0',40.61,'2025-05-27','Khuyến mãi Văn phòng','2024-08-18'),(9,_binary '\0',41.18,'2025-05-27','Khuyến mãi Đồ uống mới','2024-07-20'),(10,_binary '',36.76,'2025-04-24','Khuyến mãi Khai trương','2024-07-01');
/*!40000 ALTER TABLE `promotions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservationentity`
--

DROP TABLE IF EXISTS `reservationentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservationentity` (
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone_number` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `reservation_datetime` datetime(6) DEFAULT NULL,
  `employee_id` int NOT NULL,
  `invoice_id` int NOT NULL,
  `table_id` int NOT NULL,
  PRIMARY KEY (`employee_id`,`invoice_id`,`table_id`),
  KEY `FKimd9jlu6v0c09wsetxgey5kfr` (`invoice_id`),
  KEY `FK5rp014056usmve4oq07yl81o8` (`table_id`),
  CONSTRAINT `FK5rp014056usmve4oq07yl81o8` FOREIGN KEY (`table_id`) REFERENCES `tables` (`table_id`),
  CONSTRAINT `FKaj4rs2rqen79pxl4s2ybnabqi` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`),
  CONSTRAINT `FKimd9jlu6v0c09wsetxgey5kfr` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`invoice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservationentity`
--

LOCK TABLES `reservationentity` WRITE;
/*!40000 ALTER TABLE `reservationentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservationentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_reservations_detail`
--

DROP TABLE IF EXISTS `table_reservations_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `table_reservations_detail` (
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_phone_number` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  `reservation_datetime` datetime(6) DEFAULT NULL,
  `employee_id` int NOT NULL,
  `invoice_id` int NOT NULL,
  `table_id` int NOT NULL,
  PRIMARY KEY (`employee_id`,`invoice_id`,`table_id`),
  KEY `FK3iisnr050ecxivbx94w3ruhpx` (`invoice_id`),
  KEY `FKj0ruf9srt0m9s5u4es387m4ux` (`table_id`),
  CONSTRAINT `FK3iisnr050ecxivbx94w3ruhpx` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`invoice_id`),
  CONSTRAINT `FKj0ruf9srt0m9s5u4es387m4ux` FOREIGN KEY (`table_id`) REFERENCES `tables` (`table_id`),
  CONSTRAINT `FKlifvqa4hqbmi7l6f5ff0g0mkf` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_reservations_detail`
--

LOCK TABLES `table_reservations_detail` WRITE;
/*!40000 ALTER TABLE `table_reservations_detail` DISABLE KEYS */;
INSERT INTO `table_reservations_detail` VALUES (NULL,NULL,_binary '\0','2025-07-05 18:14:10.945291',1,176,18),(NULL,NULL,_binary '\0','2025-07-10 22:33:55.425655',1,177,17),(NULL,NULL,_binary '\0','2025-07-10 22:58:32.745175',1,178,19),(NULL,NULL,_binary '\0','2025-07-10 23:01:32.278466',1,179,20);
/*!40000 ALTER TABLE `table_reservations_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tables`
--

DROP TABLE IF EXISTS `tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tables` (
  `table_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `status` enum('AVAILABLE','OCCUPIED','RESERVED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tables`
--

LOCK TABLES `tables` WRITE;
/*!40000 ALTER TABLE `tables` DISABLE KEYS */;
INSERT INTO `tables` VALUES (1,_binary '\0','Bàn 1','AVAILABLE'),(2,_binary '\0','Bàn 2','AVAILABLE'),(3,_binary '\0','Bàn 3','AVAILABLE'),(4,_binary '\0','Bàn 4','AVAILABLE'),(5,_binary '\0','Bàn 5','AVAILABLE'),(6,_binary '\0','Bàn 6','AVAILABLE'),(7,_binary '\0','Bàn 7','AVAILABLE'),(8,_binary '\0','Bàn 8','AVAILABLE'),(9,_binary '\0','Bàn 9','AVAILABLE'),(10,_binary '\0','Bàn 10','AVAILABLE'),(11,_binary '\0','Bàn 11','AVAILABLE'),(12,_binary '\0','Bàn 12','AVAILABLE'),(13,_binary '\0','Bàn 13','AVAILABLE'),(14,_binary '\0','Bàn 14','AVAILABLE'),(15,_binary '\0','Bàn 15','AVAILABLE'),(16,_binary '\0','Bàn 16','AVAILABLE'),(17,_binary '\0','Bàn 17','OCCUPIED'),(18,_binary '\0','Bàn 18','AVAILABLE'),(19,_binary '\0','Bàn 19','AVAILABLE'),(20,_binary '\0','Bàn 20','OCCUPIED');
/*!40000 ALTER TABLE `tables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `units`
--

DROP TABLE IF EXISTS `units`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `units` (
  `unit_id` int NOT NULL AUTO_INCREMENT,
  `is_deleted` bit(1) DEFAULT NULL,
  `unit_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`unit_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `units`
--

LOCK TABLES `units` WRITE;
/*!40000 ALTER TABLE `units` DISABLE KEYS */;
INSERT INTO `units` VALUES (1,_binary '\0','g'),(2,_binary '','kg'),(3,_binary '','cái'),(4,_binary '\0','lít'),(5,_binary '\0','cái'),(6,_binary '\0','kg'),(7,_binary '','g'),(8,_binary '\0','kg'),(9,_binary '','cái'),(10,_binary '','lít');
/*!40000 ALTER TABLE `units` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-12 18:17:00
