--
--  The MIT License (MIT)
--
--  Copyright (c) 2017-2019 Bernardo Martínez Garrido
--  
--  Permission is hereby granted, free of charge, to any person obtaining a copy
--  of this software and associated documentation files (the "Software"), to deal
--  in the Software without restriction, including without limitation the rights
--  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
--  copies of the Software, and to permit persons to whom the Software is
--  furnished to do so, subject to the following conditions:
--  
--  The above copyright notice and this permission notice shall be included in all
--  copies or substantial portions of the Software.
--  
--  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
--  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
--  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
--  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
--  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
--  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
--  SOFTWARE.
--


-- ****************************************
-- This SQL script populates the initial users data.
-- ********


INSERT INTO users (login, password, name, last_name, email , city, role) VALUES
   ('santiago.paz', '123', 'santiago', 'paz perez', 'santi@udc.es', 'naron', 0),
   ('adrian.ulla','123','adrian', 'Ulla Rubinos', 'adrian.rubinos@udc.es', 'rabade', 0),
   ('user.example','123','exaplme', 'last name', 'example@udc.es', 'rabade', 0);


INSERT INTO sale_advertisements  (product_title, product_description, user_id, add_date, price) VALUES  
	('FirstProduct','FirstProductDescription',1,'2011-03-12 10:34:09', 10),
	('SecondProduct','SecondProductDescription',1,'2011-03-12 10:34:09', 15),
	('ThirdProduct','ThirdProductDescription',1,'2011-03-12 10:34:09', 20),
	('FourthProduct','FourthProductDescription',1,'2012-02-02 12:30:19', 25);
	
INSERT INTO images (title, image_path, sale_advertisement_id) VALUES
	('imageTitle1','imagePath1',1),
	('imageTitle2','imagePath2',1),
	('imageTitle3','imagePath3',1);
