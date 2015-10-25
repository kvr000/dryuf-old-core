DELETE FROM DbConfigEntry WHERE profileName = 'net.dryuf.config.test.Config0Test';
DELETE FROM DbConfigSection WHERE profileName = 'net.dryuf.config.test.Config0Test';
DELETE FROM DbConfigProfile WHERE profileName = 'net.dryuf.config.test.Config0Test';

INSERT INTO DbConfigProfile (profileName) values('net.dryuf.config.test.Config0Test');
INSERT INTO DbConfigSection (profileName, sectionName) values('net.dryuf.config.test.Config0Test', 'section1');
INSERT INTO DbConfigSection (profileName, sectionName) values('net.dryuf.config.test.Config0Test', 'section2');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.config.test.Config0Test', 'section1', 'key1', 'val11');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.config.test.Config0Test', 'section1', 'key2', 'val12');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.config.test.Config0Test', 'section2', 'key1', 'val21');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.config.test.Config0Test', 'section2', 'key2', 'val22');


DELETE FROM DbConfigEntry WHERE profileName = 'net.dryuf.dao.test.TransactionalTestTest';
DELETE FROM DbConfigSection WHERE profileName = 'net.dryuf.dao.test.TransactionalTestTest';
DELETE FROM DbConfigProfile WHERE profileName = 'net.dryuf.dao.test.TransactionalTestTest';

INSERT INTO DbConfigProfile (profileName) values('net.dryuf.dao.test.TransactionalTestTest');
INSERT INTO DbConfigSection (profileName, sectionName) values('net.dryuf.dao.test.TransactionalTestTest', 'section1');
INSERT INTO DbConfigSection (profileName, sectionName) values('net.dryuf.dao.test.TransactionalTestTest', 'section2');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.dao.test.TransactionalTestTest', 'section1', 'key1', 'val11');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.dao.test.TransactionalTestTest', 'section1', 'key2', 'val12');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.dao.test.TransactionalTestTest', 'section2', 'key1', 'val21');
INSERT INTO DbConfigEntry (profileName, sectionName, configKey, configValue) values('net.dryuf.dao.test.TransactionalTestTest', 'section2', 'key2', 'val22');
