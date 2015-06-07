-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ UserProfile ----------------------------------

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE Bid;
DROP TABLE Product;
DROP TABLE Category;
DROP TABLE UserProfile;
SET FOREIGN_KEY_CHECKS = 1;



CREATE TABLE UserProfile (
    usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    enPassword VARCHAR(13) NOT NULL, 
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL, email VARCHAR(60) NOT NULL,
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId) ,
    CONSTRAINT LoginNameUniqueKey UNIQUE (loginName)) 
    ENGINE = InnoDB;

CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);


CREATE TABLE Category (
    categoryId BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) COLLATE latin1_bin NOT NULL,    
    CONSTRAINT Category_PK PRIMARY KEY (categoryId))
    ENGINE = InnoDB;


CREATE TABLE Product (
    productId BIGINT NOT NULL AUTO_INCREMENT,
    usrId BIGINT NOT NULL,
    name VARCHAR(50) COLLATE latin1_bin NOT NULL,
    description VARCHAR(50) COLLATE latin1_bin NOT NULL,
	deliveryInfo VARCHAR(40) COLLATE latin1_bin NOT NULL, 
	outPutPrice DECIMAL(8,2) NOT NULL,
	outPutDate TIMESTAMP DEFAULT 0 NOT NULL,
    expirationTime BIGINT NOT NULL,     
	categoryId BIGINT NOT NULL,
	actualPrice DECIMAL(8,2) NOT NULL,
	winner VARCHAR(30) COLLATE latin1_bin,
	bidMax BIGINT DEFAULT 0,
	version BIGINT NOT NULL,
	CONSTRAINT ProductName_PK PRIMARY KEY (productId),
	CONSTRAINT UserProfile_FK FOREIGN KEY(usrId) REFERENCES UserProfile(usrId) ON DELETE CASCADE,
	CONSTRAINT Category_FK FOREIGN KEY(categoryId) REFERENCES Category(categoryId) ON DELETE CASCADE)
    
    ENGINE = InnoDB;

CREATE TABLE Bid (
    bidId BIGINT NOT NULL AUTO_INCREMENT,
    bidder BIGINT NOT NULL,
	productId BIGINT NOT NULL,	    
	bidMax DECIMAL(8,2) NOT NULL,
	bidTime TIMESTAMP DEFAULT 0 NOT NULL,
	instantWinner VARCHAR(30) COLLATE latin1_bin,
	instantPrice DECIMAL(8,2) NOT NULL,
	CONSTRAINT Bid_PK PRIMARY KEY (bidId),
	CONSTRAINT Userr_FK FOREIGN KEY(bidder) REFERENCES UserProfile(usrId) ON DELETE CASCADE,
    CONSTRAINT Pro_FK FOREIGN KEY(productId) REFERENCES Product(productId) ON DELETE CASCADE)

    
    ENGINE = InnoDB;
	
	
ALTER TABLE Product
  ADD CONSTRAINT bidMax_FK
  FOREIGN KEY (bidMax)
  REFERENCES Bid(bidId);	
	
