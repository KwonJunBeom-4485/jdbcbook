CREATE DATABASE IF NOT EXISTS jdbc;

use jdbc;

CREATE TABLE IF NOT EXISTS members(
	id int auto_increment primary key,
	memId varchar(50) unique not null,
	memPw varchar(50) not null,
	name varchar(50) not null,
	age int not null,
	email varchar(100) unique not null,
	phone varchar(13) unique not null,
	address varchar(100),
	regDate datetime default CURRENT_TIMESTAMP(),
	modifyDate datetime default CURRENT_TIMESTAMP()
);

CREATE TABLE IF NOT EXISTS Books(
	id int auto_increment primary key,
	bookId varchar(50) UNIQUE not null,	-- 내부 코드에서 자동 처리
	title varchar(100) not null,
	publish varchar(100) default 'Publish Unknown',
	author varchar(100) default 'Author Unknown',
	publicDate datetime,
	stock int default 0	-- 내부 코드에서 처리
);

CREATE TABLE IF NOT EXISTS Rental(
    id int auto_increment primary key,
    bookId varchar(50) not null,
    memId varchar(50) not null,
    rentalDate datetime default CURRENT_TIMESTAMP(),
    returnDate datetime,
    status varchar(20) default 'Rented',
    FOREIGN KEY (bookId) REFERENCES Books(bookId) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (memId) REFERENCES members(memId) ON UPDATE CASCADE ON DELETE CASCADE
);