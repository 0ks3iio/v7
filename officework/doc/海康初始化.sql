--兴华中学
delete from sys_option where INIID='HK.MQ.BROKER.URL';

insert into sys_option (ID, INIID, NAME, DVALUE, DESCRIPTION, NOWVALUE, VIEWABLE, VALIDATEJS, ORDERID, SUBSYSTEMID, COERCIVE,VALUE_TYPE)
values (sys_guid(), 'HK.MQ.BROKER.URL', '海康MQ,BROKER.URL', 'failover:(tcp://122.227.68.194:61618?wireFormat.maxInactivityDuration=0)?timeout=2000', '', 'failover:(tcp://122.227.68.194:61618?wireFormat.maxInactivityDuration=0)?timeout=2000', 0, '', 3504, '35', '',0);

Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','西2-3','1048576-2','03',1);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','西2-2','1048576-3','03',1);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','西2-1','1048576-4','03',2);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','西1-1','1048576-10','03',2);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东1-1','1048576-16','03',1);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东1-2','1048576-17','03',1);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东1-3','1048576-18','03',2);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东2-1','1048576-19','03',2);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东2-2','1048576-20','03',1);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东3-1','1048576-21','03',1);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东3-2','1048576-22','03',2);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东4-2','1048576-26','03',2);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东4-2','1048576-27','03',1);
Insert into office_health_device (ID,UNIT_ID,NAME,SERIAL_NUMBER,TYPE,FLAG) values (sys_guid(),'8A8A98545CC88FF3015CCEA31EFC0106','东5-1','1048576-28','03',2);
commit;