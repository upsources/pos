--    Openbravo POS is a point of sales application designed for touch screens.
--    Copyright (C) 2007-2008 Openbravo, S.L.
--    http://sourceforge.net/projects/openbravopos
--
--    This file is modified as part of fastfood branch of Openbravo POS. 2008
--    These modifications are copyright Open Sistemas de Información Internet, S.L.
--    http://www.opensistemas.com
--    e-mail: imasd@opensistemas.com
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

-- Database initial script for MYSQL
-- v2.10 fastfood


CREATE TABLE APPLICATIONS (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    VERSION VARCHAR(255) NOT NULL,
    PRIMARY KEY (ID)
);
INSERT INTO APPLICATIONS(ID, NAME, VERSION) VALUES($APP_ID{}, $APP_NAME{}, $APP_VERSION{});

CREATE TABLE ROLES (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PERMISSIONS MEDIUMBLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX ROLES_NAME_INX ON ROLES(NAME);
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('0', 'Administrator role', $FILE{/com/openbravo/pos/templates/Role.Administrator.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('1', 'Manager role', $FILE{/com/openbravo/pos/templates/Role.Manager.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('2', 'Employee role', $FILE{/com/openbravo/pos/templates/Role.Employee.xml} );
INSERT INTO ROLES(ID, NAME, PERMISSIONS) VALUES('3', 'Guest role', $FILE{/com/openbravo/pos/templates/Role.Guest.xml} );

CREATE TABLE PEOPLE (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    APPPASSWORD VARCHAR(255),
    CARD VARCHAR(255),
    ROLE VARCHAR(255) NOT NULL,
    VISIBLE BIT NOT NULL,
    IMAGE MEDIUMBLOB,
    PRIMARY KEY (ID),
    CONSTRAINT PEOPLE_FK_1 FOREIGN KEY (ROLE) REFERENCES ROLES(ID)
);
CREATE UNIQUE INDEX PEOPLE_NAME_INX ON PEOPLE(NAME);
CREATE INDEX PEOPLE_CARD_INX ON PEOPLE(CARD);

INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('0', 'Administrator', NULL, '0', TRUE, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('1', 'Manager', NULL, '1', TRUE, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('2', 'Employee', NULL, '2', TRUE, NULL);
INSERT INTO PEOPLE(ID, NAME, APPPASSWORD, ROLE, VISIBLE, IMAGE) VALUES ('3', 'Guest', NULL, '3', TRUE, NULL);

CREATE TABLE RESOURCES (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    RESTYPE INTEGER NOT NULL,
    CONTENT MEDIUMBLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX RESOURCES_NAME_INX ON RESOURCES(NAME);
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('0', 'Printer.Start', 0, $FILE{/com/openbravo/pos/templates/Printer.Start.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('1', 'Printer.Ticket', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('2', 'Printer.Ticket2', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('3', 'Printer.TicketPreview', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('4', 'Printer.TicketTotal', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketTotal.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('5', 'Printer.OpenDrawer', 0, $FILE{/com/openbravo/pos/templates/Printer.OpenDrawer.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('6', 'Printer.Ticket.Logo', 1, $FILE{/com/openbravo/pos/templates/Printer.Ticket.Logo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('7', 'Printer.TicketLine', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketLine.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('8', 'Printer.CloseCash', 0, $FILE{/com/openbravo/pos/templates/Printer.CloseCash.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('9', 'Window.Logo', 1, $FILE{/com/openbravo/pos/templates/Window.Logo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('10', 'Window.Title', 0, $FILE{/com/openbravo/pos/templates/Window.Title.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('11', 'Ticket.Buttons', 0, $FILE{/com/openbravo/pos/templates/Ticket.Buttons.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('12', 'Ticket.Line', 0, $FILE{/com/openbravo/pos/templates/Ticket.Line.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('13', 'Printer.Inventory', 0, $FILE{/com/openbravo/pos/templates/Printer.Inventory.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('14', 'Menu.Root', 0, $FILE{/com/openbravo/pos/templates/Menu.Root.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('15', 'Printer.CustomerPaid', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('16', 'Printer.CustomerPaid2', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('17', 'payment.cash', 0, $FILE{/com/openbravo/pos/templates/payment.cash.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('18', 'banknote.50euro', 1, $FILE{/com/openbravo/pos/templates/banknote.50euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('19', 'banknote.20euro', 1, $FILE{/com/openbravo/pos/templates/banknote.20euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('20', 'banknote.10euro', 1, $FILE{/com/openbravo/pos/templates/banknote.10euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('21', 'banknote.5euro', 1, $FILE{/com/openbravo/pos/templates/banknote.5euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('22', 'coin.2euro', 1, $FILE{/com/openbravo/pos/templates/coin.2euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('23', 'coin.1euro', 1, $FILE{/com/openbravo/pos/templates/coin.1euro.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('24', 'coin.50cent', 1, $FILE{/com/openbravo/pos/templates/coin.50cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('25', 'coin.20cent', 1, $FILE{/com/openbravo/pos/templates/coin.20cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('26', 'coin.10cent', 1, $FILE{/com/openbravo/pos/templates/coin.10cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('27', 'coin.5cent', 1, $FILE{/com/openbravo/pos/templates/coin.5cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('28', 'coin.2cent', 1, $FILE{/com/openbravo/pos/templates/coin.2cent.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('29', 'coin.1cent', 1, $FILE{/com/openbravo/pos/templates/coin.1cent.png});

CREATE TABLE CUSTOMERS (
    ID VARCHAR(255) NOT NULL,
    TAXID VARCHAR(255),
    NAME VARCHAR(255) NOT NULL,
    CARD VARCHAR(255),
    MAXDEBT DOUBLE DEFAULT 0 NOT NULL,
    ADDRESS VARCHAR(255),
    NOTES VARCHAR(255),
    VISIBLE BIT NOT NULL,
    CURDATE DATETIME,
    CURDEBT DOUBLE,
    PRIMARY KEY (ID)
);
CREATE INDEX CUSTOMERS_TAXID_INX ON CUSTOMERS(TAXID);
CREATE UNIQUE INDEX CUSTOMERS_NAME_INX ON CUSTOMERS(NAME);
CREATE INDEX CUSTOMERS_CARD_INX ON CUSTOMERS(CARD);

CREATE TABLE CATEGORIES (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    PARENTID VARCHAR(255),
    IMAGE MEDIUMBLOB,
    PRIMARY KEY(ID),
    CONSTRAINT CATEGORIES_FK_1 FOREIGN KEY (PARENTID) REFERENCES CATEGORIES(ID)
);
CREATE UNIQUE INDEX CATEGORIES_NAME_INX ON CATEGORIES(NAME);
INSERT INTO CATEGORIES(ID, NAME, PARENTID, IMAGE) VALUES ('0', 'Composiciones', null, null);
INSERT INTO CATEGORIES(ID, NAME, PARENTID, IMAGE) VALUES ('-1', 'BUCATARIE', null, null);
INSERT INTO CATEGORIES(ID, NAME, PARENTID, IMAGE) VALUES ('-2', 'BAR', null, null);

CREATE TABLE TAXES (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    RATE DOUBLE NOT NULL,
    PRIMARY KEY(ID)
);
CREATE UNIQUE INDEX TAXES_NAME_INX ON TAXES(NAME);
INSERT INTO TAXES(ID, NAME, RATE) VALUES ('0', '*', 0);
INSERT INTO TAXES(ID, NAME, RATE) VALUES ('-1', 'NOTAX', 0.0);

CREATE TABLE PRODUCTS (
    ID VARCHAR(255) NOT NULL,
    REFERENCE VARCHAR(255) NOT NULL,
    CODE VARCHAR(255) NOT NULL,
    CODETYPE VARCHAR(255),
    NAME VARCHAR(255) NOT NULL,
    PRICEBUY DOUBLE NOT NULL,
    PRICESELL DOUBLE NOT NULL,
    CATEGORY VARCHAR(255) NOT NULL,
    TAX VARCHAR(255) NOT NULL,
    STOCKCOST DOUBLE,
    STOCKVOLUME DOUBLE,
    IMAGE MEDIUMBLOB,
    ISCOM BIT NOT NULL,
    ISSCALE BIT NOT NULL,
    ATTRIBUTES MEDIUMBLOB,
    PRIMARY KEY (ID),
    CONSTRAINT PRODUCTS_FK_1 FOREIGN KEY (CATEGORY) REFERENCES CATEGORIES(ID),
    CONSTRAINT PRODUCTS_FK_2 FOREIGN KEY (TAX) REFERENCES TAXES(ID)
);
CREATE UNIQUE INDEX PRODUCTS_INX_0 ON PRODUCTS(REFERENCE);
CREATE UNIQUE INDEX PRODUCTS_INX_1 ON PRODUCTS(CODE);
CREATE UNIQUE INDEX PRODUCTS_NAME_INX ON PRODUCTS(NAME);

CREATE TABLE PRODUCTS_CAT (
    PRODUCT VARCHAR(255) NOT NULL,
    CATORDER INTEGER,
    PRIMARY KEY (PRODUCT),
    CONSTRAINT PRODUCTS_CAT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID)
);
CREATE INDEX PRODUCTS_CAT_INX_1 ON PRODUCTS_CAT(CATORDER);

CREATE TABLE PRODUCTS_COM (
    PRODUCT VARCHAR(255) NOT NULL,
    PRODUCT2 VARCHAR(255) NOT NULL,
    PRIMARY KEY (PRODUCT, PRODUCT2),
    CONSTRAINT PRODUCTS_COM_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT PRODUCTS_COM_FK_2 FOREIGN KEY (PRODUCT2) REFERENCES PRODUCTS(ID)
);

CREATE TABLE PRODUCTS_MAT (
    PRODUCT VARCHAR(255) NOT NULL,
    MATERIAL VARCHAR(255) NOT NULL,
    AMOUNT DOUBLE NOT NULL,
    PRIMARY KEY (PRODUCT, MATERIAL),
    CONSTRAINT PROD_MAT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT PROD_MAT_FK_2 FOREIGN KEY (MATERIAL) REFERENCES PRODUCTS(ID) 
);

CREATE TABLE SUBGROUPS (
    ID VARCHAR(255) NOT NULL,
    COMPOSITION VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    IMAGE MEDIUMBLOB,
    PRIMARY KEY(ID),
    CONSTRAINT SUBGROUPS_FK_1 FOREIGN KEY (COMPOSITION) REFERENCES PRODUCTS(ID) ON DELETE CASCADE
);

CREATE TABLE SUBGROUPS_PROD (
    SUBGROUP VARCHAR(255) NOT NULL,
    PRODUCT VARCHAR(255) NOT NULL,
    PRIMARY KEY (SUBGROUP, PRODUCT),
    CONSTRAINT SUBGROUPS_PROD_FK_1 FOREIGN KEY (SUBGROUP) REFERENCES SUBGROUPS(ID) ON DELETE CASCADE,
    CONSTRAINT SUBGROUPS_PROD_FK_2 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID) ON DELETE CASCADE
);

CREATE TABLE TARIFFAREAS (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    TARIFFORDER INTEGER DEFAULT 0,
    PRIMARY KEY(ID)
);
CREATE UNIQUE INDEX TARIFFAREAS_NAME_INX ON TARIFFAREAS(NAME);

CREATE TABLE TARIFFAREAS_PROD (
    TARIFFID VARCHAR(255) NOT NULL,
    PRODUCTID VARCHAR(255) NOT NULL,
    PRICESELL DOUBLE NOT NULL,
    PRIMARY KEY (TARIFFID, PRODUCTID),
    CONSTRAINT TARIFFAREAS_PROD_FK_1 FOREIGN KEY (TARIFFID) REFERENCES TARIFFAREAS(ID) ON DELETE CASCADE,
    CONSTRAINT TARIFFAREAS_PROD_FK_2 FOREIGN KEY (PRODUCTID) REFERENCES PRODUCTS(ID) ON DELETE CASCADE
);

CREATE TABLE LOCATIONS (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    ADDRESS VARCHAR(255),
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX LOCATIONS_NAME_INX ON LOCATIONS(NAME);
INSERT INTO LOCATIONS(ID, NAME,ADDRESS) VALUES('0', 'General', NULL);

CREATE TABLE STOCKDIARY (
    ID VARCHAR(255) NOT NULL,
    DATENEW DATETIME NOT NULL,
    REASON INTEGER NOT NULL,
    LOCATION VARCHAR(255) NOT NULL,
    PRODUCT VARCHAR(255) NOT NULL,
    UNITS DOUBLE NOT NULL,
    PRICE DOUBLE NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT STOCKDIARY_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKDIARY_FK_2 FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);
CREATE INDEX STOCKDIARY_INX_1 ON STOCKDIARY(DATENEW);

CREATE TABLE STOCKCURRENT (
    LOCATION VARCHAR(255) NOT NULL,
    PRODUCT VARCHAR(255) NOT NULL,
    STOCKSECURITY DOUBLE,
    STOCKMAXIMUM DOUBLE,
    UNITS DOUBLE NOT NULL,
    PRIMARY KEY (LOCATION, PRODUCT),
    CONSTRAINT STOCKCURRENT_FK_1 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT STOCKCURRENT_FK_2 FOREIGN KEY (LOCATION) REFERENCES LOCATIONS(ID)
);

CREATE TABLE CLOSEDCASH (
    MONEY VARCHAR(255) NOT NULL,
    HOST VARCHAR(255) NOT NULL,
    DATESTART DATETIME NOT NULL,
    DATEEND DATETIME,
    PRIMARY KEY(MONEY)
);
CREATE INDEX CLOSEDCASH_INX_1 ON CLOSEDCASH(DATESTART);

CREATE TABLE RECEIPTS (
    ID VARCHAR(255) NOT NULL,
    MONEY VARCHAR(255) NOT NULL,
    DATENEW DATETIME NOT NULL,
    PRIMARY KEY(ID),
    CONSTRAINT RECEIPTS_FK_MONEY FOREIGN KEY (MONEY) REFERENCES CLOSEDCASH(MONEY)
);
CREATE INDEX RECEIPTS_INX_1 ON RECEIPTS(DATENEW);

CREATE TABLE DISCOUNTS (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    QUANTITY DOUBLE NOT NULL,
    PERCENTAGE BIT NOT NULL,
    PRIMARY KEY(ID)
);
CREATE UNIQUE INDEX DISCOUNTS_NAME_INX ON DISCOUNTS(NAME);

CREATE TABLE TICKETS (
    ID VARCHAR(255) NOT NULL,
    TICKETID INTEGER NOT NULL,
    PERSON VARCHAR(255) NOT NULL,
    CUSTOMER VARCHAR(255),
    STATUS INTEGER DEFAULT 0 NOT NULL,
    DISCOUNTNAME VARCHAR(255),
    DISCOUNTVALUE VARCHAR(255),
    TARIFFAREA VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT TICKETS_FK_ID FOREIGN KEY (ID) REFERENCES RECEIPTS(ID),
    CONSTRAINT TICKETS_FK_2 FOREIGN KEY (PERSON) REFERENCES PEOPLE(ID),
    CONSTRAINT TICKETS_CUSTOMERS_FK FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID),
    CONSTRAINT TICKETS_TARIFFAREA FOREIGN KEY (TARIFFAREA) REFERENCES TARIFFAREAS(ID)
);

CREATE INDEX TICKETS_TICKETID ON TICKETS(TICKETID);

CREATE TABLE TICKETSNUM (ID INTEGER NOT NULL);
INSERT INTO TICKETSNUM VALUES(1);

CREATE TABLE TICKETLINES (
    TICKET VARCHAR(255) NOT NULL,
    LINE INTEGER NOT NULL,
    PRODUCT VARCHAR(255),
    NAME VARCHAR(255),
    ISCOM BIT,
    ISDISCOUNT BIT,
    UNITS DOUBLE NOT NULL,
    PRICE DOUBLE NOT NULL,
    TAXID VARCHAR(255) NOT NULL,
    ATTRIBUTES MEDIUMBLOB,
    PRIMARY KEY (TICKET, LINE),
    CONSTRAINT TICKETLINES_FK_TICKET FOREIGN KEY (TICKET) REFERENCES TICKETS(ID),
    CONSTRAINT TICKETlINES_FK_2 FOREIGN KEY (PRODUCT) REFERENCES PRODUCTS(ID),
    CONSTRAINT TICKETLINES_FK_3 FOREIGN KEY (TAXID) REFERENCES TAXES(ID)
);

CREATE TABLE PAYMENTS (
    ID VARCHAR(255) NOT NULL,
    RECEIPT VARCHAR(255) NOT NULL,
    PAYMENT VARCHAR(255) NOT NULL,
    TOTAL DOUBLE NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT PAYMENTS_FK_RECEIPT FOREIGN KEY (RECEIPT) REFERENCES RECEIPTS(ID)
);
CREATE INDEX PAYMENTS_INX_1 ON PAYMENTS(PAYMENT);

CREATE TABLE FLOORS (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    IMAGE MEDIUMBLOB,
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX FLOORS_NAME_INX ON FLOORS(NAME);
INSERT INTO FLOORS(ID, NAME, IMAGE) VALUES ('0', 'Restaurant floor', $FILE{/com/openbravo/pos/templates/restaurantsample.png});

CREATE TABLE PLACES (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    X INTEGER NOT NULL,
    Y INTEGER NOT NULL,
    FLOOR VARCHAR(255) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT PLACES_FK_1 FOREIGN KEY (FLOOR) REFERENCES FLOORS(ID)
);
CREATE UNIQUE INDEX PLACES_NAME_INX ON PLACES(NAME);
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('1', 'Table 1', 133, 151, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('2', 'Table 2', 532, 151, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('3', 'Table 3', 133, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('4', 'Table 4', 266, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('5', 'Table 5', 399, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('6', 'Table 6', 532, 264, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('7', 'Table 7', 133, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('8', 'Table 8', 266, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('9', 'Table 9', 399, 377, '0');
INSERT INTO PLACES(ID, NAME, X, Y, FLOOR) VALUES ('10', 'Table 10', 532, 377, '0');

CREATE TABLE RESERVATIONS (
    ID VARCHAR(255) NOT NULL,
    CREATED DATETIME NOT NULL,
    DATENEW DATETIME DEFAULT '2001-01-01 00:00:00' NOT NULL,
    TITLE VARCHAR(255) NOT NULL,
    CHAIRS INTEGER NOT NULL,
    ISDONE BIT NOT NULL,
    DESCRIPTION VARCHAR(255),
    PRIMARY KEY (ID)
);
CREATE INDEX RESERVATIONS_INX_1 ON RESERVATIONS(DATENEW);

CREATE TABLE RESERVATION_CUSTOMERS (
    ID VARCHAR(255) NOT NULL,
    CUSTOMER VARCHAR(255) NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT RES_CUST_FK_1 FOREIGN KEY (ID) REFERENCES RESERVATIONS(ID),
    CONSTRAINT RES_CUST_FK_2 FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMERS(ID)
);

CREATE TABLE THIRDPARTIES (
    ID VARCHAR(255) NOT NULL,
    CIF VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    ADDRESS VARCHAR(255),
    CONTACTCOMM VARCHAR(255),
    CONTACTFACT VARCHAR(255),
    PAYRULE VARCHAR(255),
    FAXNUMBER VARCHAR(255),
    PHONENUMBER VARCHAR(255),
    MOBILENUMBER VARCHAR(255),
    EMAIL VARCHAR(255),
    WEBPAGE VARCHAR(255),
    NOTES VARCHAR(255),
    PRIMARY KEY (ID)
);
CREATE UNIQUE INDEX THIRDPARTIES_CIF_INX ON THIRDPARTIES(CIF);
CREATE UNIQUE INDEX THIRDPARTIES_NAME_INX ON THIRDPARTIES(NAME);

CREATE TABLE SHAREDTICKETS (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    CONTENT MEDIUMBLOB,
    USERID VARCHAR,
    PRIMARY KEY(ID)
);

CREATE TABLE UNITS (
    ID VARCHAR(255) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    SYMBOL VARCHAR(255) NOT NULL,
    PRIMARY KEY(ID)
);
CREATE UNIQUE INDEX UNITS_NMAE_INX ON UNITS(NAME);

CREATE TABLE MATERIALS_UNITS (
    MATERIAL VARCHAR(255) NOT NULL,
    UNIT VARCHAR(255) NOT NULL,
    AMOUNT DOUBLE NOT NULL,
    PRICEBUY DOUBLE NOT NULL,
    PRIMARY KEY(MATERIAL, UNIT),
    CONSTRAINT MAT_UNIT_FK_1 FOREIGN KEY (MATERIAL) REFERENCES PRODUCTS(ID),
    CONSTRAINT MAT_UNIT_FK_2 FOREIGN KEY (UNIT) REFERENCES UNITS(ID)
);

