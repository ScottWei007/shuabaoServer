`test`
CREATE TABLE phone_verification_code (
phone BIGINT(20) COMMENT '手機號碼',
countryCode INT(11) COMMENT '國家代碼',
verificationCode INT(11) COMMENT '驗證碼',
times TINYINT(2) COMMENT '驗證的次數',
logTime BIGINT(20) COMMENT '記錄驗證碼的時間',
PRIMARY KEY (phone, countryCode)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;






CREATE TABLE user_phone_info (
countryCode INT(6) COMMENT '國家代碼',
phone BIGINT(20) COMMENT '手機號碼',
uid INT(11) COMMENT '用戶號',
PRIMARY KEY(phone, countryCode),
UNIQUE KEY(uid)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE user_info_0(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_info_1(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_info_2(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_info_3(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_info_4(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_info_5(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE user_info_6(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_info_7(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE user_info_8(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_info_9(
uid INT(11) COMMENT '用戶號',
nickname VARCHAR(12) COMMENT '昵稱',
avatar VARCHAR(120) COMMENT '頭像',
sex TINYINT(2) DEFAULT 0 COMMENT '性別，0男，1女',
pwd CHAR(32) COMMENT '密碼',
salt CHAR(6) COMMENT '密碼鹽',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
cid VARCHAR(20) COMMENT '渠道號',
addr VARCHAR(10) COMMENT '地址',
birth DATE COMMENT '生日',
`level` TINYINT(2) DEFAULT 1 COMMENT '等級',
bonds BIGINT(20) DEFAULT 0 COMMENT '券幣',
gold BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
onlineTime INT(11) DEFAULT 0 COMMENT '在綫時長，小時',
lastlogintime DATETIME DEFAULT NOW() COMMENT '上次登錄時間',
lastlogouttime DATETIME COMMENT '上次退出时间',
token CHAR(32) DEFAULT '' COMMENT '登陸token',
logTime DATETIME DEFAULT NOW() COMMENT '創建時間',
`status` TINYINT(2) DEFAULT 0 COMMENT '賬號狀態，0，正常，1異常',
PRIMARY KEY(uid),
KEY(nickname),
KEY(logTime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE new_register_log (
did CHAR(36) COMMENT '唯一設備id',
uid INT(11) COMMENT '用戶號',
cid VARCHAR(20) COMMENT '渠道號',
ip CHAR(15) COMMENT '注冊ip',
os VARCHAR(7) COMMENT '移動端系統,ios, android',
`version` VARCHAR(10)  COMMENT 'app版本',
regType TINYINT(2) COMMENT '注冊類型1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
pkg VARCHAR(20) COMMENT '包',
model VARCHAR(20) COMMENT '手機型號',
lang TINYINT(2) COMMENT '語言版本',
logTime DATETIME DEFAULT NOW() COMMENT '記錄時間',
KEY(did),
UNIQUE KEY(uid),
KEY(logtime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_login_log (
uid INT(11) COMMENT '用戶號',
did CHAR(36) COMMENT '唯一設備id',
cid VARCHAR(20) COMMENT '渠道號',
ip CHAR(15) COMMENT '登錄ip',
os VARCHAR(7) COMMENT '移動端系統,ios, android',
`version` VARCHAR(10)  COMMENT 'app版本',
pkg VARCHAR(20) COMMENT '包',
logTime DATETIME DEFAULT NOW() COMMENT '記錄時間',
PRIMARY KEY(uid),
KEY(logtime)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

 CREATE TABLE driver_black_list (
 did CHAR(36) COMMENT '移動端唯一號',
 os VARCHAR(7) COMMENT '移動端系統,ios, android',
 uid INT(11) COMMENT '用戶號',
 logTime DATETIME DEFAULT NOW() COMMENT '記錄時間',
 KEY(did)
 )ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
 
 CREATE TABLE user_open_plat_info (
 uid INT(11) COMMENT ' 用戶號',
 openId VARCHAR(255) COMMENT '平臺id',
 regType TINYINT(2) COMMENT '注冊平臺類型，1，手機(這個沒有)；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
 nickname VARCHAR(50) COMMENT '平臺昵稱',
 avatar VARCHAR(255) COMMENT '平臺頭像',
 logTime DATETIME DEFAULT NOW() COMMENT '記錄時間',
 PRIMARY KEY (openId, regType),
 KEY(uid)
 )ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
 
CREATE TABLE uid_sequence (
id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
a CHAR(1) NOT NULL DEFAULT '',
PRIMARY KEY (id),
UNIQUE KEY(a)
 )ENGINE=MYISAM;
 
INSERT INTO uid_sequence VALUES(1000000,'a');


##########################################

#一對一
CREATE TABLE `user_info_0` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT '0' COMMENT '性別，0男，1女',
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '地址',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth` DATE DEFAULT NULL COMMENT '生日',
  `level` TINYINT(2) DEFAULT '1' COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT '1' COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT '0' COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT '0' COMMENT '游戲幣',
  `price` INT(11) DEFAULT '0' COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT '0' COMMENT '是否免费预览,0，否，1，是',
  `onlineTime` INT(11) DEFAULT '0' COMMENT '在綫時長，小時',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT '0' COMMENT '賬號狀態，0，正常，1異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4



#相親
CREATE TABLE `user_info_0` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_1` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_2` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_3` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_4` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_5` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `user_info_6` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_7` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_8` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_info_9` (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `nickname` VARCHAR(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` VARCHAR(120) DEFAULT NULL COMMENT '頭像',
  `sex` TINYINT(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` VARCHAR(10) DEFAULT NULL COMMENT '工作地區',
  `sign` VARCHAR(32) DEFAULT NULL COMMENT '个性签名',
  `birth`VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `height` INT(11) DEFAULT NULL COMMENT '身高',
  `education` TINYINT(2) DEFAULT 3 COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` TINYINT(2) DEFAULT 1 COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` VARCHAR(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` TINYINT(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内', 
  `pwd` CHAR(32) DEFAULT NULL COMMENT '密碼',
  `salt` CHAR(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` TINYINT(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `level` TINYINT(2) DEFAULT 1 COMMENT '账号等級',
  `viplevel` TINYINT(2) DEFAULT 1 COMMENT '消费等級',
  `bonds` BIGINT(20) DEFAULT 0 COMMENT '券幣',
  `gold` BIGINT(20) DEFAULT 0 COMMENT '游戲幣',
  `price` INT(11) DEFAULT 0 COMMENT '聊天价格',
  `preview` TINYINT(2) DEFAULT 2 COMMENT '是否免费预览,1，是，2，否',
  `lastlogintime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` DATETIME DEFAULT NULL COMMENT '上次退出时间',
  `token` CHAR(32) DEFAULT '' COMMENT '登陸token',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` TINYINT(2) DEFAULT 1 COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE user_info_0;
DROP TABLE user_info_1;
DROP TABLE user_info_2;
DROP TABLE user_info_3;
DROP TABLE user_info_4;
DROP TABLE user_info_5;
DROP TABLE user_info_6;
DROP TABLE user_info_7;
DROP TABLE user_info_8;
DROP TABLE user_info_9;

#===========================
#訂單
CREATE TABLE `payment_order_info` (
  `oid` BIGINT(20) NOT NULL COMMENT '訂單號',
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `os` VARCHAR(7) COMMENT '移動端系統,ios, android',
  `cid` VARCHAR(20) DEFAULT NULL COMMENT '渠道號', 
  `rid` INT(11) DEFAULT NULL COMMENT '充值点id',
  `paytype` TINYINT(2) DEFAULT NULL COMMENT '充值類型:1,apple;2,支付寶; 3,微信支付',
  `payment` TINYINT(2) DEFAULT 1 COMMENT '充值方式:1,APP应用;2,H5;3,扫码;',
  `amount` INT(11) DEFAULT NULL COMMENT '申请支付金额',  
  `country` VARCHAR(10) DEFAULT NULL COMMENT '国家代码',
  `status` TINYINT(2) DEFAULT 1 COMMENT '訂單状态:1等待支付,2支付成功,3支付失败,4交易完成(支付成功才會進入交易階段)',
  `prepayorderId` VARCHAR(128) DEFAULT NULL COMMENT '第三方预支付订单号',
  `notifyurl` VARCHAR(128) DEFAULT NULL COMMENT '回调地址',
  `notifytime` DATETIME DEFAULT NULL COMMENT '第三方支付平台回调时间',
  `notifyoid` VARCHAR(128) DEFAULT NULL COMMENT '第三方支付订单号',
  `ip` CHAR(15) COMMENT 'ip地址',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`oid`),
  KEY `uid` (`uid`),
  KEY `notifytime` (`notifytime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

INSERT INTO payment_order_info(oid, uid, os, cid, rid, paytype, payment, amount, country, notifyurl, `prepayorderId`,ip ) 
VALUES (121221,122,'ios', 'hha', 2, 1, 1,100, 'cn', NULL,NULL,'11.11.2.3')
DROP TABLE payment_order_info
SELECT * FROM payment_order_info

#充值點
CREATE TABLE `setting_recharge` (
  `rid` INT(11) NOT NULL COMMENT '充值点id',
  `amount` INT(11) NOT NULL COMMENT '金額面值',
  `diamond` INT(11) DEFAULT NULL COMMENT '钻石数',
  `freenum` INT(11) DEFAULT 0 COMMENT '赠送钻石数量',
  `paytype` TINYINT(2) DEFAULT NULL COMMENT '充值類型:1,apple;2,支付寶; 3,微信支付',
  `issale` TINYINT(2) DEFAULT 0 COMMENT '是否促销,0,否，1是',
  `isdouble` TINYINT(2) DEFAULT 0 COMMENT '是否双倍:0,否，1是',
  `os` VARCHAR(7) COMMENT '移動端系統,ios, android',
  `pkg` VARCHAR(20) COMMENT '包',
  `sort` TINYINT(2) DEFAULT 0 COMMENT '排序',
  UNIQUE KEY (`os`, `pkg`,`paytype`,`amount`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


 CREATE TABLE order_receipt (
	receiptMd5 CHAR(32) COMMENT '苹果凭证的md5',
	receipt TEXT COMMENT '苹果凭证',
	oid BIGINT(20) COMMENT '订单号',
	`status` TINYINT(1) DEFAULT 0 COMMENT '状态，0，未使用，1已使用',
	logtime DATETIME DEFAULT NOW() COMMENT '记录时间',
	KEY (receiptMd5)
 )
 
INSERT INTO order_receipt(receiptMd5, receipt, oid) 
        VALUES ('65655656d56d5fdfdfdfdfd5664', 'sdsdhoglgag533[dfdjfjgjggg', 1235486325355)
        
        SELECT * FROM order_receipt

CREATE TABLE user_change_log (
  `uid` INT(11) NOT NULL COMMENT '用戶號',
  `num` INT(11) NOT NULL COMMENT '数值',
  `logtype` TINYINT(2) DEFAULT NULL COMMENT '记录類型:1,gold;2,bonds;',
  `logTime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  KEY `uid` (`uid`),
  KEY `logTime` (`logTime`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


drop table user_info_3
#新表
CREATE TABLE `user_info_3` (
  `uid` int(11) NOT NULL COMMENT '用戶號',
  `nickname` varchar(12) DEFAULT NULL COMMENT '昵稱',
  `avatar` varchar(120) DEFAULT NULL COMMENT '頭像',
  `sex` tinyint(2) DEFAULT NULL COMMENT '性別，1男，2女',
  `addr` varchar(10) DEFAULT NULL COMMENT '工作地區',
  `sign` varchar(32) DEFAULT NULL COMMENT '个性签名',
  `birth` varchar(20) DEFAULT NULL COMMENT '生日',
  `height` int(11) DEFAULT NULL COMMENT '身高',
  `education` tinyint(2) DEFAULT '3' COMMENT '学历, 1高中及以下、2专科、3本科、4研究、5博士',
  `marriage` tinyint(2) DEFAULT '1' COMMENT '婚姻状况,1单身，2离异，3丧偶',
  `income` varchar(12) DEFAULT NULL COMMENT '月收入',
  `expectedtime` tinyint(2) DEFAULT NULL COMMENT '期望结婚时间,1,半年内、2,一年内，3,两年内',
  `pwd` char(32) DEFAULT NULL COMMENT '密碼',
  `salt` char(6) DEFAULT NULL COMMENT '密碼鹽',
  `regType` tinyint(2) DEFAULT NULL COMMENT '注冊類型,1,手機；2，Facebook；3，google；4，twitter；5，line；6，微信；7，qq',
  `cid` varchar(20) DEFAULT NULL COMMENT '渠道號',
  `level` tinyint(2) DEFAULT '1' COMMENT '账号等級',
  `viplevel` tinyint(2) DEFAULT '2' COMMENT '是否vip,1，是，2，否',
  `bonds` bigint(20) DEFAULT '0' COMMENT '券幣',
  `gold` bigint(20) DEFAULT '0' COMMENT '游戲幣',
  `price` int(11) DEFAULT '0' COMMENT '聊天价格',
  `preview` tinyint(2) DEFAULT '2' COMMENT '是否完善资料,1，是，2，否',
  `lastlogintime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上次登錄時間',
  `lastlogouttime` datetime DEFAULT NULL COMMENT '上次退出时间',
  `token` char(32) DEFAULT '' COMMENT '登陸token',
  `logTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  `status` tinyint(2) DEFAULT '1' COMMENT '賬號狀態，1，正常，2異常',
  PRIMARY KEY (`uid`),
  KEY `nickname` (`nickname`),
  KEY `logTime` (`logTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

