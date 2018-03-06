CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city` text,
  `state` text,
  `person_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `person_id` (`person_id`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE
);