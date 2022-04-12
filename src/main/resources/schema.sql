CREATE MEMORY TABLE PUBLIC.BANKS(
    ID BINARY(255) NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL);

CREATE MEMORY TABLE PUBLIC.BANKS_CREDITS(
    BANK_ID BINARY(255) NOT NULL,
    CREDITS_ID BINARY(255) NOT NULL,
    CONSTRAINT "BANK_ID" FOREIGN KEY(BANK_ID) REFERENCES PUBLIC.BANKS(ID));

CREATE MEMORY TABLE PUBLIC.BANKS_CUSTOMERS(
    BANK_ID BINARY(255) NOT NULL,
    CUSTOMERS_ID BINARY(255) NOT NULL,
    CONSTRAINT "BANK_ID" FOREIGN KEY(BANK_ID) REFERENCES PUBLIC.BANKS(ID));

CREATE MEMORY TABLE PUBLIC.CREDIT_PROPOSITION(
    ID BINARY(255) NOT NULL PRIMARY KEY,
    CREDIT_AMOUNT NUMERIC(19,2) NOT NULL,
    CREDIT_TERM INTEGER NOT NULL,
    TOTAL_INTERESTS NUMERIC(19,2) NOT NULL,
    BANK_ID BINARY(255),
    CREDIT_ID BINARY(255),
    CUSTOMER_ID BINARY(255),
    PAYMENT_SCHEDULE_ID BINARY(255),
    CREDIT_PROPOSITION_ID BINARY(255),
    CONSTRAINT "BANK_ID" FOREIGN KEY(BANK_ID) REFERENCES PUBLIC.BANKS(ID));

CREATE MEMORY TABLE PUBLIC.CREDITS(
    ID BINARY(255) NOT NULL PRIMARY KEY,
    CREDIT_LIMIT NUMERIC(19,2),
    RATE_PERCENT NUMERIC(19,2));

CREATE MEMORY TABLE PUBLIC.CUSTOMERS(
    ID BINARY(255) NOT NULL PRIMARY KEY,
    EMAIL VARCHAR(255),
    NAME VARCHAR(255),
    PASSPORT_NUMBER VARCHAR(255),
    PATRONYMIC VARCHAR(255),
    SURNAME VARCHAR(255));

CREATE MEMORY TABLE PUBLIC.PAYMENT_RECORDS(
    ID BINARY(255) NOT NULL PRIMARY KEY,
    DATE DATE,
    INTEREST_REPAYMENT_AMOUNT NUMERIC(19,2),
    PAYMENT NUMERIC(19,2),
    REPAYMENT_AMOUNT_OF_THE_LOAN_BODY NUMERIC(19,2),
    PAYMENT_SCHEDULE_ID BINARY(255));

CREATE MEMORY TABLE PUBLIC.PAYMENT_SCHEDULE(
    ID BINARY(255) NOT NULL PRIMARY KEY);

ALTER TABLE PUBLIC.BANKS_CREDITS ADD CONSTRAINT "CREDITS_ID" FOREIGN KEY(CREDITS_ID)
    REFERENCES PUBLIC.CREDITS(ID);
ALTER TABLE PUBLIC.BANKS_CUSTOMERS ADD CONSTRAINT "CUSTOMERS_ID" FOREIGN KEY(CUSTOMERS_ID)
    REFERENCES PUBLIC.CUSTOMERS(ID);
ALTER TABLE PUBLIC.CREDIT_PROPOSITION ADD CONSTRAINT "CREDIT_ID" FOREIGN KEY(CREDIT_ID)
    REFERENCES PUBLIC.CREDITS(ID);
ALTER TABLE PUBLIC.CREDIT_PROPOSITION ADD CONSTRAINT "CUSTOMER_ID" FOREIGN KEY(CUSTOMER_ID)
    REFERENCES PUBLIC.CUSTOMERS(ID);
ALTER TABLE PUBLIC.CREDIT_PROPOSITION ADD CONSTRAINT "PAYMENT_SCHEDULE_ID" FOREIGN KEY(PAYMENT_SCHEDULE_ID)
    REFERENCES PUBLIC.PAYMENT_SCHEDULE(ID);
ALTER TABLE PUBLIC.PAYMENT_RECORDS ADD CONSTRAINT "PAYMENT_SCHEDULE_ID" FOREIGN KEY(PAYMENT_SCHEDULE_ID)
    REFERENCES PUBLIC.PAYMENT_SCHEDULE(ID);