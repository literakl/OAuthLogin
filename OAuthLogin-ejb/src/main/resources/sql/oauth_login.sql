-- --------------------------------------------------------
-- Hostitel:                     127.0.0.1
-- Verze serveru:                5.5.34-MariaDB - mariadb.org binary distribution
-- OS serveru:                   Win32
-- HeidiSQL Verze:               8.0.0.4396
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Exportování struktury pro tabulka test.oauth_login
CREATE TABLE IF NOT EXISTS `oauth_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `provider_code` varchar(2) NOT NULL,
  `provider_id` varchar(255) NOT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  `registration_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_BASE_USER_ID` (`user_id`),
  CONSTRAINT `FK_BASE_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `base_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Export dat nebyl vybrán.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
