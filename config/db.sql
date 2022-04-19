DROP DATABASE IF EXISTS `AIOST`;
CREATE DATABASE `AIOST`;
USE `AIOST`;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends` (
  `player_ID` BIGINT(20) UNSIGNED NOT NULL,
  `friend_ID` BIGINT(20) UNSIGNED NOT NULL,
  UNIQUE KEY `composite_player_friend_ID` (`player_ID`, `friend_ID`) USING BTREE,
  CONSTRAINT `friends_ibfk_1` FOREIGN KEY (`player_ID`) REFERENCES `players` (`player_ID`),
  CONSTRAINT `friends_ibfk_2` FOREIGN KEY (`friend_ID`) REFERENCES `players` (`player_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for friend_requests
-- ----------------------------
DROP TABLE IF EXISTS `friend_requests`;
CREATE TABLE `friend_requests` (
  `player_ID` BIGINT(20) UNSIGNED NOT NULL,
  `request_player_ID` BIGINT(20) UNSIGNED NOT NULL,
  `request_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `composite_player_request_player_ID` (`player_ID`, `request_player_ID`) USING BTREE,
  CONSTRAINT `friend_requests_ibfk_1` FOREIGN KEY (`player_ID`) REFERENCES `players` (`player_ID`),
  CONSTRAINT `friend_requests_ibfk_2` FOREIGN KEY (`request_player_ID`) REFERENCES `players` (`player_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player_worlds
-- ----------------------------
DROP TABLE IF EXISTS `player_worlds`;
CREATE TABLE `player_worlds` (
  `uuid` BINARY(16) NOT NULL,
  `location_ID` BIGINT(20) UNSIGNED NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `owner_ID` BIGINT(20) UNSIGNED NOT NULL,
  `environment` TINYINT(4) UNSIGNED NOT NULL,
  `type` INT(11) NOT NULL,
  `generateStructures` BIT(1) NOT NULL,
  `last_save_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uuid`),
  KEY `owner_ID` (`owner_ID`) USING BTREE,
  CONSTRAINT `player_worlds_ibfk_1` FOREIGN KEY (`owner_ID`) REFERENCES `players` (`player_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for games
-- ----------------------------
DROP TABLE IF EXISTS `games`;
CREATE TABLE `games` (
  `uuid` BINARY(16) NOT NULL,
  `world_uuid` BINARY(16) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `type` INT(11) NOT NULL,
  `rate_Amount` BIGINT(20) UNSIGNED DEFAULT '0',
  `rate_Value` BIGINT(20) UNSIGNED DEFAULT '0',
  `rate` DOUBLE DEFAULT '0',
  `release_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uuid`),
  KEY `type` (`type`) USING BTREE,
  KEY `name` (`name`) USING BTREE,
  CONSTRAINT `games_ibfk_1` FOREIGN KEY (`world_uuid`) REFERENCES `player_worlds` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for played_games
-- ----------------------------
DROP TABLE IF EXISTS `played_games`;
CREATE TABLE `played_games` (
  `type` INT(11) NOT NULL,
  `uuid` BINARY(16) NOT NULL,
  `time` INT(11) UNSIGNED NOT NULL,
  `player` TINYINT(4) UNSIGNED NOT NULL,
  `winner` VARCHAR(100) NOT NULL,
  `playerData` VARCHAR(10000) NOT NULL,
  `save_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `type` (`type`) USING BTREE,
  KEY `uuid` (`uuid`) USING BTREE,
  CONSTRAINT `played_games_ibfk_1` FOREIGN KEY (`uuid`) REFERENCES `games` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for players
-- ----------------------------
DROP TABLE IF EXISTS `players`;
CREATE TABLE `players` (
  `player_ID` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `UUID` BINARY(16) NOT NULL,
  `player_Name` VARCHAR(16) NOT NULL,
  `rank_ID` TINYINT(4) UNSIGNED DEFAULT '0',
  `score` INT(11) UNSIGNED DEFAULT '0',
  `credits` INT(11) UNSIGNED DEFAULT '5000',
  `level` TINYINT(4) UNSIGNED DEFAULT '1',
  `experience` INT(11) UNSIGNED DEFAULT '0',
  `health` DOUBLE DEFAULT '20',
  `max_Health` DOUBLE DEFAULT '20',
  `mana` DOUBLE DEFAULT '20',
  `max_Mana` DOUBLE DEFAULT '20',
  `hunger` TINYINT(4) UNSIGNED DEFAULT '20',
  `thirst` TINYINT(4) UNSIGNED DEFAULT '20',
  `mc_Level` INT(11) UNSIGNED DEFAULT '0',
  `mc_Experience` FLOAT DEFAULT '0',
  PRIMARY KEY (`player_ID`),
  UNIQUE KEY `UUID` (`UUID`) USING BTREE,
  KEY `player_Name` (`player_Name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player_inventory
-- ----------------------------
DROP TABLE IF EXISTS `player_inventory`;
CREATE TABLE `player_inventory` (
  `player_ID` BIGINT(20) UNSIGNED NOT NULL,
  `nbt_Tag` VARCHAR(10000) DEFAULT NULL,
  UNIQUE KEY `player_inventory_fk1` (`player_ID`) USING BTREE,
  CONSTRAINT `player_inventory_ibfk_1` FOREIGN KEY (`player_ID`) REFERENCES `players` (`player_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player_permissions
-- ----------------------------
DROP TABLE IF EXISTS `player_permissions`;
CREATE TABLE `player_permissions` (
  `player_ID` BIGINT(20) UNSIGNED NOT NULL,
  `permission_ID` SMALLINT(5) UNSIGNED NOT NULL,
  UNIQUE KEY `composite_player_permission_ID` (`player_ID`, `permission_ID`) USING BTREE,
  CONSTRAINT `player_permissions_ibfk_1` FOREIGN KEY (`player_ID`) REFERENCES `players` (`player_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player_settings
-- ----------------------------
DROP TABLE IF EXISTS `player_settings`;
CREATE TABLE `player_settings` (
  `player_ID` BIGINT(20) UNSIGNED NOT NULL,
  `setting_ID` SMALLINT(5) UNSIGNED NOT NULL,
  `value` SMALLINT(5) UNSIGNED NOT NULL,
  UNIQUE KEY `composite_player_setting_ID` (`player_ID`, `setting_ID`) USING BTREE,
  CONSTRAINT `player_settings_ibfk_1` FOREIGN KEY (`player_ID`) REFERENCES `players` (`player_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player_unlockables
-- ----------------------------
DROP TABLE IF EXISTS `player_unlockables`;
CREATE TABLE `player_unlockables` (
  `player_ID` BIGINT(20) UNSIGNED NOT NULL,
  `type_ID` INT(11) NOT NULL,
  `unlockable_ID` SMALLINT(5) UNSIGNED NOT NULL,
  UNIQUE KEY `composite_player_unlockables_ID` (`player_ID`, `type_ID`, `unlockable_ID`) USING BTREE,
  CONSTRAINT `player_unlockables_ibfk_1` FOREIGN KEY (`player_ID`) REFERENCES `players` (`player_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Procedure structure for addPlayer
-- ----------------------------
DROP PROCEDURE IF EXISTS `addPlayer`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addPlayer`(
		IN `playerUUID` BINARY(16),
		IN `playerName` VARCHAR(16),
		OUT `playerID` BIGINT(20) UNSIGNED,
		OUT `rankID` TINYINT(4) UNSIGNED
	)
BEGIN

INSERT INTO players (UUID, player_Name)		-- create new player
	VALUES (playerUUID, playerName);						 
	
SELECT p.player_ID, p.rank_ID INTO playerID, rankID FROM players AS p WHERE p.UUID = playerUUID;		-- select create data

INSERT INTO player_inventory (player_ID, nbt_Tag)		-- create entry in player inventory
	VALUES (playerID, NULL);

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getPlayer
-- ----------------------------
DROP PROCEDURE IF EXISTS `getPlayer`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPlayer`(
	IN `playerID` BIGINT UNSIGNED,
	IN `sendInventory` BOOL
	)
BEGIN

SELECT GROUP_CONCAT(ps.setting_ID), GROUP_CONCAT(ps.`value`)		-- get player settings
INTO @settingIDs, @settingValues
FROM player_settings AS ps 
WHERE ps.player_ID = playerID;

IF sendInventory
THEN
	SELECT pi.nbt_Tag INTO @playerInventory FROM player_inventory AS pi WHERE pi.player_ID = playerID;		-- get player inventory
ELSE
	SET @playerInventory := NULL;
END IF;

SELECT p.rank_ID, p.score, p.credits, p.level, p.experience, p.health, p.max_Health, p.mana, p.max_Mana, p.hunger, p.thirst, p.mc_Level, p.mc_Experience,		-- get player data
	(SELECT GROUP_CONCAT(pp.permission_ID) FROM player_permissions AS pp WHERE pp.player_ID = playerID) AS player_Permissions,		-- get player permissions
	@settingIDs AS player_Setting_IDs, @settingValues AS player_Setting_Values,
	@playerInventory AS player_Inventory
FROM players AS p 
WHERE p.player_ID = playerID;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getPlayerCore
-- ----------------------------
DROP PROCEDURE IF EXISTS `getPlayerCore`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPlayerCore`(
		IN `UUID` BINARY(36),
		IN `playerName` VARCHAR(16)
	)
BEGIN

SET @uuidBin := UUID_TO_BIN(UUID);
SET @playerID := NULL;
	
SELECT p.player_ID, p.player_Name, p.rank_ID		-- get current player data
INTO @playerID,  @playerName, @rankID 
FROM players AS p
WHERE p.UUID = @uuidBin;

IF @playerID IS NULL
THEN CALL addPlayer(@uuidBin, playerName, @playerID, @rankID);		-- add new player
ELSEIF STRCMP(playerName, @playerName) != 0
THEN  
	UPDATE players AS p SET p.player_Name = playerName WHERE p.player_ID = @playerID;		-- update player name
END IF;

SELECT @playerID, @rankID;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updatePlayer
-- ----------------------------
DROP PROCEDURE IF EXISTS `updatePlayer`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `updatePlayer`(
        IN `playerID` BIGINT UNSIGNED,
        IN `newHealth` DOUBLE UNSIGNED,
        IN `newmaxHealth` DOUBLE UNSIGNED,
        IN `newMana` DOUBLE UNSIGNED,
        IN `newmaxMana` DOUBLE UNSIGNED,
        IN `newHunger` TINYINT UNSIGNED,
        IN `newThirst` TINYINT UNSIGNED,
        IN `newmclevel` INTEGER UNSIGNED,
        IN `newmcexperience` FLOAT UNSIGNED,
		IN `newPermissions` VARCHAR(1000),
		IN `changedSettings` VARCHAR(1000),
		IN `changedSettingValues` VARCHAR(1000),
		IN `inventoryTag` VARCHAR(10000)
    )
BEGIN

UPDATE players SET 		-- update player stats
    health = newHealth,
		max_Health = newmaxHealth, 
    mana = newMana, 
    max_Mana = newmaxMana, 
    hunger = newHunger, 
    thirst = newThirst, 
    mc_Level = newmclevel, 
    mc_Experience = newmcexperience 
WHERE player_ID = playerID;

IF newPermissions IS NOT NULL		-- update permissions
THEN
	SET @lastIndex := 1;
	SET @index := LOCATE(',',newPermissions);
	SET @query := 'INSERT INTO player_permissions VALUES';
	 WHILE @index != 0
	 DO
	  SET @query := CONCAT(@query, '(', playerID, ',', CONVERT(SUBSTR(newPermissions, @lastIndex, @index - 1), UNSIGNED INTEGER), '),');
    SET @lastIndex := @index + 1;
		SET @index := LOCATE(',',newPermissions, @lastIndex);
  END WHILE;
	SET @query := CONCAT(@query, '(', playerID, ',', CONVERT(SUBSTR(newPermissions, @lastIndex), UNSIGNED INTEGER), ')');
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt; 
END IF;

IF changedSettings IS NOT NULL AND changedSettingValues IS NOT NULL		-- update settings
THEN
	SET @lastIndex := 1;
	SET @index := LOCATE(',',changedSettings);
	SET @lastIndex2 := 1;
	SET @index2 := LOCATE(',',changedSettingValues);
	SET @query := 'INSERT INTO player_settings VALUES';
	 WHILE @index != 0
	 DO
	  SET @query := CONCAT(@query, '(', playerID, ',', CONVERT(SUBSTR(changedSettings, @lastIndex, @index - 1), UNSIGNED INTEGER), ',', 
			CONVERT(SUBSTR(changedSettingValues, @lastIndex2, @index2 - 1), UNSIGNED INTEGER), '),');
    SET @lastIndex := @index + 1;
		SET @index := LOCATE(',',changedSettings, @lastIndex);
		SET @lastIndex2 := @index2 + 1;
		SET @index2 := LOCATE(',',changedSettingValues, @lastIndex2);
  END WHILE;
	SET @query := CONCAT(@query, '(', playerID, ',', CONVERT(SUBSTR(changedSettings, @lastIndex), UNSIGNED INTEGER), ',', 
		CONVERT(SUBSTR(changedSettingValues, @lastIndex2), UNSIGNED INTEGER), ') ON DUPLICATE KEY UPDATE 
		player_ID = values(player_ID), 
		setting_ID = values(setting_ID),
		`value` = values(`value`);');
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt; 
END IF;

IF inventoryTag IS NOT NULL		-- update inventory
THEN
	UPDATE player_inventory AS pi SET pi.nbt_Tag = inventoryTag WHERE pi.player_ID = playerID;
END IF;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for setRank
-- ----------------------------
DROP PROCEDURE IF EXISTS `setRank`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `setRank`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_rank_ID` TINYINT UNSIGNED
    )
UPDATE players AS p SET p.rank_ID = _rank_ID WHERE p.player_ID = _player_ID;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for addCredits
-- ----------------------------
DROP PROCEDURE IF EXISTS `addCredits`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addCredits`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_credits` INT UNSIGNED
    )
UPDATE players AS p SET p.credits = p.credits + _credits WHERE p.player_ID = _player_ID;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for addUnlockable
-- ----------------------------
DROP PROCEDURE IF EXISTS `addUnlockable`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addUnlockable`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_type_ID` INT,
        IN `_unlockable_ID` SMALLINT UNSIGNED
    )
INSERT INTO player_unlockables (player_ID, type_ID, unlockable_ID) 
	VALUES(_player_ID, _type_ID, _unlockable_ID)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for buyUnlockable
-- ----------------------------
DROP PROCEDURE IF EXISTS `buyUnlockable`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `buyUnlockable`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_type_ID` INT,
        IN `_unlockable_ID` SMALLINT UNSIGNED,
        IN `_price` INT UNSIGNED
    )
BEGIN

SELECT p.credits INTO @player_Credits FROM players AS p WHERE p.player_ID = _player_ID;

IF @player_Credits >= _price
THEN
	UPDATE players AS p SET p.credits = (@player_Credits - _price) WHERE p.player_ID = _player_ID;
	CALL addUnlockable(_player_ID, _type_ID, _unlockable_ID);
END IF;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getUnlockables
-- ----------------------------
DROP PROCEDURE IF EXISTS `getUnlockables`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUnlockables`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_type_ID` INT
    )
SELECT unlockable_ID 
FROM player_unlockables AS pu
WHERE pu.player_ID = _player_ID AND pu.type_ID = _type_ID
ORDER BY unlockable_ID ASC
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getUnlockablesRegion
-- ----------------------------
DROP PROCEDURE IF EXISTS `getUnlockablesRegion`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getUnlockablesRegion`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_type_ID` INT,
        IN `_start_ID` SMALLINT UNSIGNED,
        IN `_size` SMALLINT UNSIGNED
    )
SELECT unlockable_ID 
FROM player_unlockables AS pu
WHERE pu.player_ID = _player_ID AND pu.type_ID = _type_ID AND pu.unlockable_ID >= _start_ID AND pu.unlockable_ID < _start_ID + _size
ORDER BY unlockable_ID ASC
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for hasUnlockable
-- ----------------------------
DROP PROCEDURE IF EXISTS `hasUnlockable`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `hasUnlockable`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_type_ID` INT,
        IN `_unlockable_ID` SMALLINT UNSIGNED,
        OUT `_result` SMALLINT UNSIGNED
    )
SET _result := EXISTS(
	SELECT unlockable_ID 
	FROM player_unlockables AS pu
	WHERE pu.player_ID = _player_ID AND pu.type_ID = _type_ID AND pu.unlockable_ID = _unlockable_ID
	)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for addFriend
-- ----------------------------
DROP PROCEDURE IF EXISTS `addFriend`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addFriend`(
        IN `_player_ID_1` BIGINT UNSIGNED,
        IN `_player_ID_2` BIGINT UNSIGNED
    )
BEGIN

INSERT INTO friends (player_ID, friend_ID)
	values(_player_ID_1, _player_ID_2);
INSERT INTO friends (player_ID, friend_ID)
	values(_player_ID_2, _player_ID_1);
	
DELETE FROM friend_requests WHERE player_ID = _player_ID_2 AND request_player_ID = _player_ID_1;
	
END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for removeFriend
-- ----------------------------
DROP PROCEDURE IF EXISTS `removeFriend`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `removeFriend`(
        IN `_player_ID_1` BIGINT UNSIGNED,
        IN `_player_ID_2` BIGINT UNSIGNED
    )
BEGIN

DELETE FROM friends WHERE player_ID = _player_ID_1 AND friend_ID = _player_ID_2;
DELETE FROM friends WHERE player_ID = _player_ID_2 AND friend_ID = _player_ID_1;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for removeFriendPerName
-- ----------------------------
DROP PROCEDURE IF EXISTS `removeFriendPerName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `removeFriendPerName`(
        IN `_player_ID_1` BIGINT UNSIGNED,
        IN `_player_Name` VARCHAR(16)
    )
BEGIN

SELECT player_ID INTO @player_ID_2 FROM players WHERE player_Name = _player_Name LIMIT 1;

IF @player_ID_2 IS NOT NULL
THEN
	CALL removeFriend(_player_ID_1, @player_ID_2);
END IF;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getFriends
-- ----------------------------
DROP PROCEDURE IF EXISTS `getFriends`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getFriends`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_startIndex` INT UNSIGNED
    )
BEGIN

PREPARE stmt FROM
"SELECT f.friend_ID, p.player_Name, p.rank_ID, p.level, p.score
FROM friends AS f
INNER JOIN players AS p          
ON f.friend_ID = p.player_ID   
WHERE f.player_ID = ?
LIMIT 28
OFFSET ?";

EXECUTE stmt USING _player_ID, _startIndex;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for addFriendRequest
-- ----------------------------
DROP PROCEDURE IF EXISTS `addFriendRequest`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addFriendRequest`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_request_player_ID` BIGINT UNSIGNED,
        OUT `_answer` TINYINT
    )
BEGIN

IF( EXISTS(SELECT player_ID FROM friends WHERE player_ID = _player_ID AND friend_ID = _request_player_ID))
THEN
	SET _answer := 4;
ELSEIF( EXISTS(SELECT player_ID FROM friend_requests WHERE player_ID = _request_player_ID AND request_player_ID = _player_ID))
THEN
	CALL addFriend(_player_ID, _request_player_ID);
	SET _answer := 1;
ELSE
	INSERT INTO friend_requests (player_ID, request_player_ID)
		values(_player_ID, _request_player_ID);
	SET _answer := 0;
END IF;
	
END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for addFriendRequestPerName
-- ----------------------------
DROP PROCEDURE IF EXISTS `addFriendRequestPerName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addFriendRequestPerName`(
        IN `_player_Name` VARCHAR(16),
        IN `_request_player_ID` BIGINT UNSIGNED,
        OUT `_answer` TINYINT
    )
BEGIN

SELECT player_ID INTO @playerID FROM players WHERE player_Name = _player_Name LIMIT 1;

IF @playerID IS NOT NULL
THEN
	CALL addFriendRequest(@playerID, _request_player_ID, _answer);
ELSE
	SET _answer := 2;
END IF;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for removeFriendRequest
-- ----------------------------
DROP PROCEDURE IF EXISTS `removeFriendRequest`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `removeFriendRequest`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_request_player_ID` BIGINT UNSIGNED
    )
DELETE FROM friend_requests
WHERE player_ID = _player_ID AND request_player_ID = _request_player_ID
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for removeFriendRequestPerName
-- ----------------------------
DROP PROCEDURE IF EXISTS `removeFriendRequestPerName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `removeFriendRequestPerName`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_request_player_Name` VARCHAR(16)
    )
BEGIN

SELECT player_ID INTO @requestPlayerID FROM players WHERE player_Name = _request_player_Name LIMIT 1;

IF @requestPlayerID IS NOT NULL
THEN
	CALL removeFriendRequest(_player_ID, @requestPlayerID);
END IF;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getFriendRequests
-- ----------------------------
DROP PROCEDURE IF EXISTS `getFriendRequests`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getFriendRequests`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_startIndex` INT UNSIGNED
    )
BEGIN

PREPARE stmt FROM
"SELECT fr.request_date, fr.request_player_ID, p.player_Name, p.rank_ID, p.level, p.score
FROM friend_requests AS fr
INNER JOIN players AS p          
ON fr.request_player_ID = p.player_ID    
WHERE fr.player_ID = ?
LIMIT 28
OFFSET ?";

EXECUTE stmt USING _player_ID, _startIndex;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getSendFriendRequests
-- ----------------------------
DROP PROCEDURE IF EXISTS `getSendFriendRequests`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getSendFriendRequests`(
        IN `_request_player_ID` BIGINT UNSIGNED,
        IN `_startIndex` INT UNSIGNED
    )
BEGIN

PREPARE stmt FROM
"SELECT fr.request_date, fr.player_ID, p.player_Name, p.rank_ID, p.level, p.score
FROM friend_requests AS fr
INNER JOIN players AS p          
ON fr.player_ID = p.player_ID    
WHERE fr.request_player_ID = ?
LIMIT 28
OFFSET ?";

EXECUTE stmt USING _request_player_ID, _startIndex;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for addPlayerWorld
-- ----------------------------
DROP PROCEDURE IF EXISTS `addPlayerWorld`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addPlayerWorld`(
        IN `_owner_ID` BIGINT UNSIGNED,
        IN `_location_ID` BIGINT UNSIGNED,
        IN `_name` VARCHAR(100),
        IN `_environment` TINYINT UNSIGNED,
        IN `_type` INT,
        IN `_generateStructures` BIT,
        OUT `_uuid` BINARY(36)
    )
BEGIN

SET _uuid := UUID();

INSERT INTO player_worlds (uuid, location_ID, name, owner_ID, environment, type, generateStructures)
		values(UUID_TO_BIN(_uuid), _location_ID, _name, _owner_ID, _environment, _type, _generateStructures);
		
END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for removePlayerWorld
-- ----------------------------
DROP PROCEDURE IF EXISTS `removePlayerWorld`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `removePlayerWorld`(
        IN `_uuid` BINARY(36)
    )
DELETE FROM player_worlds
WHERE uuid = UUID_TO_BIN(_uuid)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getPlayerWorlds
-- ----------------------------
DROP PROCEDURE IF EXISTS `getPlayerWorlds`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getPlayerWorlds`(
        IN `_owner_ID` BIGINT UNSIGNED,
        IN `_limit` INT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

PREPARE stmt FROM
"SELECT BIN_TO_UUID(pw.uuid), pw.location_ID, pw.name, pw.environment, pw.type, pw.generateStructures, pw.last_save_date
FROM player_worlds AS pw
WHERE pw.owner_ID = ?
ORDER BY pw.last_save_date DESC
LIMIT ?
OFFSET ?";

EXECUTE stmt USING _owner_ID, _limit, _offset;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for renamePlayerWorld
-- ----------------------------
DROP PROCEDURE IF EXISTS `renamePlayerWorld`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `renamePlayerWorld`(
        IN `_uuid` BINARY(36),
        IN `_new_Name` VARCHAR(100)
    )
UPDATE player_worlds SET
	name = _new_Name
WHERE uuid = UUID_TO_BIN(_uuid)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updatePlayerWorldLastSaveDate
-- ----------------------------
DROP PROCEDURE IF EXISTS `updatePlayerWorldLastSaveDate`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `updatePlayerWorldLastSaveDate`(
        IN `_uuid` BINARY(36)
    )
UPDATE player_worlds SET
	last_save_date = CURRENT_TIMESTAMP
WHERE uuid = UUID_TO_BIN(_uuid)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for addGame
-- ----------------------------
DROP PROCEDURE IF EXISTS `addGame`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `addGame`(
        IN `_world_uuid` BINARY(36),
        IN `_name` VARCHAR(100),
        IN `_type` INT,
        OUT `_uuid` BINARY(36)
    )
BEGIN

SET _uuid := UUID();

INSERT INTO games (uuid, world_uuid, name, type)
		values(UUID_TO_BIN(_uuid), UUID_TO_BIN(_world_uuid), _name, _type);

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updateGame
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateGame`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateGame`(
        IN `_uuid` BINARY(36)
    )
UPDATE games SET
	update_date = CURRENT_TIMESTAMP
WHERE uuid = UUID_TO_BIN(_uuid)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for updateGameRate
-- ----------------------------
DROP PROCEDURE IF EXISTS `updateGameRate`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `updateGameRate`(
        IN `_uuid` BINARY(36),
        IN `_amount` SMALLINT UNSIGNED,
        IN `_value` SMALLINT UNSIGNED
    )
UPDATE games SET
	rate_Amount = rate_Amount + _amount,
	rate_value = rate_value + _value,
	rate = rate_value / rate_Amount
WHERE uuid = UUID_TO_BIN(_uuid)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerType
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerType`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerType`(
        IN `_type` INT,
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM games AS g
INNER JOIN player_worlds AS pw ON pw.uuid = g.world_uuid
INNER JOIN players AS p ON p.player_ID = pw.owner_ID
WHERE g.type = ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy), 
" LIMIT 28 OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING _type, _offset * 28;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerTypeAndName
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerTypeAndName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerTypeAndName`(
        IN `_type` INT,
        IN `_name` VARCHAR(100),
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM games AS g
INNER JOIN player_worlds AS pw ON pw.uuid = g.world_uuid
INNER JOIN players AS p ON p.player_ID = pw.owner_ID
WHERE g.type = ? AND g.name LIKE ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy),
" LIMIT 28
OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING _type, CONCAT('%', _name, '%'), _offset * 28;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerPlayer
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerPlayer`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerPlayer`(
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM games AS g
INNER JOIN player_worlds AS pw ON pw.uuid = g.world_uuid
INNER JOIN players AS p ON p.player_ID = pw.owner_ID
WHERE pw.owner_ID = ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy), 
" LIMIT 28
OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING _player_ID, _offset * 28;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerTypeAndPlayer
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerTypeAndPlayer`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerTypeAndPlayer`(
        IN `_type` INT,
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM games AS g
INNER JOIN player_worlds AS pw ON pw.uuid = g.world_uuid
INNER JOIN players AS p ON p.player_ID = pw.owner_ID
WHERE g.type = ? AND pw.owner_ID = ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy),
" LIMIT 28
OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING _type, _player_ID, _offset * 28;

DEALLOCATE PREPARE stmt;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerTypeAndPlayerName
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerTypeAndPlayerName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerTypeAndPlayerName`(
        IN `_type` INT,
        IN `_player_Name` VARCHAR(16),
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM players AS p
INNER JOIN player_worlds AS pw ON pw.owner_ID = p.player_ID
INNER JOIN games AS g ON g.world_uuid = pw.uuid
WHERE p.player_Name LIKE ? AND g.type = ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy),
" LIMIT 28
OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING CONCAT('%', _player_Name, '%'), _type, _offset * 28;

DEALLOCATE PREPARE stmt;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerNameAndPlayer
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerNameAndPlayer`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerNameAndPlayer`(
        IN `_name` VARCHAR(100),
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM games AS g
INNER JOIN player_worlds AS pw ON pw.uuid = g.world_uuid
INNER JOIN players AS p ON p.player_ID = pw.owner_ID
WHERE pw.owner_ID = ? AND g.name LIKE ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy),
" LIMIT 28
OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING _player_ID, CONCAT('%', _name, '%'), _offset * 28;

DEALLOCATE PREPARE stmt; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerTypeAndNameAndPlayer
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerTypeAndNameAndPlayer`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerTypeAndNameAndPlayer`(
        IN `_type` INT,
        IN `_name` VARCHAR(100),
        IN `_player_ID` BIGINT UNSIGNED,
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM games AS g
INNER JOIN player_worlds AS pw ON pw.uuid = g.world_uuid
INNER JOIN players AS p ON p.player_ID = pw.owner_ID
WHERE g.type = ? AND pw.owner_ID = ? AND g.name LIKE ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy),
" LIMIT 28
OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING _type, _player_ID, CONCAT('%', _name, '%'), _offset * 28;

DEALLOCATE PREPARE stmt;

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamesPerTypeAndNameAndPlayerName
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamesPerTypeAndNameAndPlayerName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamesPerTypeAndNameAndPlayerName`(
        IN `_type` INT,
        IN `_name` VARCHAR(100),
        IN `_player_Name` VARCHAR(16),
        IN `_orderBy` TINYINT UNSIGNED,
        IN `_offset` INT UNSIGNED
    )
BEGIN

SET @query := CONCAT("SELECT BIN_TO_UUID(g.uuid), g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM players AS p
INNER JOIN player_worlds AS pw ON pw.owner_ID = p.player_ID
INNER JOIN games AS g ON g.world_uuid = pw.uuid
WHERE p.player_Name LIKE ? AND g.type = ? AND g.name LIKE ?
ORDER BY ", GAME_ORDER_BY_STRING(_orderBy),
" LIMIT 28
OFFSET ?");

PREPARE stmt FROM @query;

EXECUTE stmt USING CONCAT('%',_player_Name, '%'), _type, CONCAT('%', _name, '%'), _offset * 28;

DEALLOCATE PREPARE stmt;

END;
;;
DELIMITER ;

-- ----------------------------
-- Function structure for Game_Order_By_String
-- ----------------------------
DROP FUNCTION IF EXISTS `Game_Order_By_String`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `Game_Order_By_String`(
        _orderBy TINYINT
    )
    RETURNS VARCHAR(20)
    LANGUAGE SQL  DETERMINISTIC  CONTAINS SQL  SQL SECURITY INVOKER
BEGIN

CASE
    WHEN _orderBy = 0 THEN RETURN 'g.release_date DESC';
    WHEN _orderBy = 1 THEN RETURN 'g.release_date';
    WHEN _orderBy = 2 THEN RETURN 'g.rate DESC';
    WHEN _orderBy = 3 THEN RETURN 'g.rate';
    WHEN _orderBy = 4 THEN RETURN 'g.rate_amount';
    ELSE BEGIN END;
END CASE; 

END;
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for getGamePerUUID
-- ----------------------------
DROP PROCEDURE IF EXISTS `getGamePerUUID`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `getGamePerUUID`(
        IN `_uuid` BINARY(36)
    )
SELECT _uuid, g.name, g.type, pw.owner_ID, p.player_Name, pw.environment, pw.type, 
	pw.generateStructures, g.rate_amount, g.rate, g.release_date, g.update_date
FROM games AS g
INNER JOIN player_worlds AS pw ON pw.uuid = g.world_uuid
INNER JOIN players AS p ON p.player_ID = pw.owner_ID
WHERE g.uuid = UUID_TO_BIN(_uuid)
;;
DELIMITER ;

-- ----------------------------
-- Procedure structure for saveGameStats
-- ----------------------------
DROP PROCEDURE IF EXISTS `saveGameStats`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `saveGameStats`(
        IN `_type` INT,
        IN `_uuid` BINARY(36),
        IN `_time` INT UNSIGNED,
        IN `_playerSize` INT UNSIGNED,
        IN `_winner` VARCHAR(100),
        IN `_rateAmount` INT UNSIGNED,
        IN `_rateValue` INT UNSIGNED,
        IN `_playerData` VARCHAR(10000)
    )
BEGIN

SET @uuidBin := UUID_TO_BIN(_uuid);

UPDATE games SET 		-- update game rate
    rate_Amount = rate_Amount + _rateAmount, 
    rate_Value = rate_Value + _rateValue,
    rate = rate_Value / rate_Amount
WHERE uuid = @uuidBin;

INSERT INTO played_games (type, uuid, time, player, winner, playerData)
		values(_type, @uuidBin, _time, _playerSize, _winner, _playerData);
		
SET @lastIndex := 1;
SET @index := LOCATE(';',_playerData);
WHILE @index != 0
	DO
	SET @data := SUBSTR(_playerData, @lastIndex, @index - 1);
	SET @lastIndex := @index + 1;
	SET @index := LOCATE(';', _playerData, @lastIndex);
	
	SET @firstComma := LOCATE(',', @data);
	SET @playerID := CONVERT(SUBSTR(@data, 1, @firstComma - 1), UNSIGNED INT);
	SET @secondComma := LOCATE(',', @data, @firstComma + 1);
	SET @credits := CONVERT(SUBSTR(@data, @firstComma + 1, @secondComma - 1), INT);
	
	UPDATE players SET 		-- update player credits
	    credits = credits + @credits
	WHERE player_ID = @playerID;
END WHILE;
		
END;
;;
DELIMITER ;

-- ----------------------------
-- Function structure for Uuid_To_Bin
-- ----------------------------
DROP FUNCTION IF EXISTS `Uuid_To_Bin`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `Uuid_To_Bin`(
        _uuid BINARY(36)
    )
    RETURNS BINARY(16)
    LANGUAGE SQL  DETERMINISTIC  CONTAINS SQL  SQL SECURITY INVOKER
RETURN
	UNHEX(CONCAT(
            SUBSTR(_uuid, 15, 4),
            SUBSTR(_uuid, 10, 4),
            SUBSTR(_uuid,  1, 8),
            SUBSTR(_uuid, 20, 4),
            SUBSTR(_uuid, 25) ));
;;
DELIMITER ;

-- ----------------------------
-- Function structure for Bin_To_Uuid
-- ----------------------------
DROP FUNCTION IF EXISTS `Bin_To_Uuid`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `Bin_To_Uuid`(
        _bin  BINARY(16)
    )
    RETURNS BINARY(36)
    LANGUAGE SQL  DETERMINISTIC  CONTAINS SQL  SQL SECURITY INVOKER
RETURN
	LCASE(CONCAT_WS('-',
            HEX(SUBSTR(_bin,  5, 4)),
            HEX(SUBSTR(_bin,  3, 2)),
            HEX(SUBSTR(_bin,  1, 2)),
            HEX(SUBSTR(_bin,  9, 2)),
            HEX(SUBSTR(_bin, 11))
                 ));
;;
DELIMITER ;