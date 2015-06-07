-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pojo" database.
-------------------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;


INSERT INTO UserProfile VALUES (1,'pepe','PTIOwIamfI36c','pepe','pepe','zas@udc.es');
INSERT INTO UserProfile VALUES (2,'lau','PTIOwIamfI36c','lau','lau','lau@udc.es');

INSERT INTO Category VALUES(1,'Libros');
INSERT INTO Category VALUES(2,'Películas');
INSERT INTO Category VALUES(3,'Portátiles');
INSERT INTO Category VALUES(4,'Música');
INSERT INTO Category VALUES(5,'Cámaras de fotos');



SET FOREIGN_KEY_CHECKS = 1;

