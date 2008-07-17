--    Openbravo POS is a point of sales application designed for touch screens.
--    Copyright (C) 2007-2008 Openbravo, S.L.
--    http://sourceforge.net/projects/openbravopos
--
--    This program is free software; you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation; either version 2 of the License, or
--    (at your option) any later version.
--
--    This program is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with this program; if not, write to the Free Software
--    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

-- Database upgrade script for ORACLE
-- v2.10 - v2.20

ALTER TABLE CUSTOMERS ADD SEARCHKEY VARCHAR2(255);
UPDATE CUSTOMERS SET SEARCHKEY = ID;
ALTER TABLE CUSTOMERS MODIFY SEARCHKEY VARCHAR2(255) NOT NULL;
CREATE UNIQUE INDEX CUSTOMERS_SKEY_INX ON CUSTOMERS(SEARCHKEY);

ALTER TABLE CUSTOMERS ADD ADDRESS2 VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD POSTAL VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD CITY VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD REGION VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD COUNTRY VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD FIRSTNAME VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD LASTNAME VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD EMAIL VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD PHONE VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD PHONE2 VARCHAR2(255);
ALTER TABLE CUSTOMERS ADD FAX VARCHAR2(255);

UPDATE PEOPLE SET CARD = NULL WHERE CARD = '';
DELETE FROM SHAREDTICKETS;

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};
