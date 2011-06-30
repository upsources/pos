/* This sql deletes all transactions in Openbravo POS */
/* Delete ticketlines before tickets for consistent constraints */
delete from ticketlines;
delete from tickets;
delete from receipts;
delete from taxlines;
delete from closedcash;
delete from payments;
delete from stockdiary;
delete from stockcurrent;
delete from reservation_customers;
delete from reservations;
update ticketsnum set ID = 0; /* Attempt to modify an identity column ID */
update products set stockcost=0, stockvolume=0;
update customers set curdebt=0, curdate=null;
