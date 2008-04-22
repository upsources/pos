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

-- Database upgrade script for MYSQL
-- v2.00 - v2.10

ALTER TABLE PEOPLE ADD COLUMN CARD VARCHAR(255);  
CREATE INDEX PEOPLE_CARD_INX ON PEOPLE(CARD);

ALTER TABLE CUSTOMERS ADD COLUMN TAXID VARCHAR(255);  
ALTER TABLE CUSTOMERS ADD COLUMN CARD VARCHAR(255);  
ALTER TABLE CUSTOMERS ADD COLUMN MAXDEBT DOUBLE DEFAULT 0 NOT NULL;
ALTER TABLE CUSTOMERS ADD COLUMN CURDATE DATETIME;
ALTER TABLE CUSTOMERS ADD COLUMN CURDEBT DOUBLE;
CREATE INDEX CUSTOMERS_TAXID_INX ON CUSTOMERS(TAXID);
CREATE INDEX CUSTOMERS_CARD_INX ON CUSTOMERS(CARD);

ALTER TABLE TICKETS ADD COLUMN CUSTOMER VARCHAR(255);
ALTER TABLE TICKETS ADD CONSTRAINT TICKETS_CUSTOMERS_FK FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID);

ALTER TABLE RECEIPTS ADD COLUMN DATENEW DATETIME;
CREATE INDEX RECEIPTS_INX_1 ON RECEIPTS(DATENEW);
UPDATE RECEIPTS SET DATENEW = (SELECT DATENEW FROM TICKETS WHERE TICKETS.ID = RECEIPTS.ID);
ALTER TABLE RECEIPTS ALTER COLUMN DATENEW SET NOT NULL;

DROP INDEX TICKETS_INX_1
ALTER TABLE TICKETS DROP COLUMN DATENEW;

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('14', 'Menu.Root', 0, $FILE{/com/openbravo/pos/templates/Menu.Root.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('15', 'Printer.CustomerPaid', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('16', 'Printer.CustomerPaid2', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid2.xml});

DELETE FROM SHAREDTICKETS;

UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = '2.10' WHERE ID = $APP_ID{};
