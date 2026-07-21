create database dbmechanicalworkshop;
use dbmechanicalworkshop;

create table tuser(
idUser char(36) not null,
firstName varchar(70) not null,
surName varchar(40) not null,
email varchar(100) not null,
password varchar(2000) not null,
role varchar(70) not null, /*ADMIN, MECHANIC, RECEPTIONIST*/
createdAt datetime not null,
updatedAt datetime not null,
primary key(idUser)
) engine=innodb;

create table tcustomer(
idCustomer char(36) not null,
firstName varchar(70) not null,
surName varchar(40) not null,
documentNumber varchar(15) not null,
phone varchar(20) null,
email varchar(100) null,
address varchar(200) null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idCustomer)
) engine=innodb;

create table tvehicle(
idVehicle char(36) not null,
idCustomer char(36) not null,
brand varchar(50) not null,
model varchar(50) not null,
year int not null,
plate varchar(15) not null,
color varchar(30) null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idVehicle),
foreign key(idCustomer) references tcustomer(idCustomer) on delete cascade on update cascade
) engine=innodb;

create table tmechanic(
idMechanic char(36) not null,
firstName varchar(70) not null,
specialty varchar(80) not null,
phone varchar(20) null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idMechanic)
) engine=innodb;

create table tworkorder(
idWorkOrder char(36) not null,
idVehicle char(36) not null,
idMechanic char(36) null,
entryDate datetime not null,
problemDescription text not null,
diagnosis text null,
status varchar(70) not null, /*PENDING, IN_REPAIR, FINISHED, DELIVERED*/
createdAt datetime not null,
updatedAt datetime not null,
primary key(idWorkOrder),
foreign key(idVehicle) references tvehicle(idVehicle) on delete cascade on update cascade,
foreign key(idMechanic) references tmechanic(idMechanic) on delete cascade on update cascade
) engine=innodb;

create table tservice(
idService char(36) not null,
name varchar(100) not null,
description varchar(300) null,
price decimal(10,2) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idService)
) engine=innodb;

create table tsparepart(
idSparePart char(36) not null,
name varchar(100) not null,
brand varchar(60) null,
price decimal(10,2) not null,
stock int not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idSparePart)
) engine=innodb;

create table tinvoice(
idInvoice char(36) not null,
idCustomer char(36) not null,
issueDate datetime not null,
total decimal(10,2) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idInvoice),
foreign key(idCustomer) references tcustomer(idCustomer) on delete cascade on update cascade
) engine=innodb;

create table tinvoicedetail(
idInvoiceDetail char(36) not null,
idInvoice char(36) not null,
idService char(36) null,
idSparePart char(36) null,
idWorkOrder char(36) null,
concept varchar(150) not null,
quantity int not null,
unitPrice decimal(10,2) not null,
subtotal decimal(10,2) not null,
createdAt datetime not null,
updatedAt datetime not null,
primary key(idInvoiceDetail),
foreign key(idInvoice) references tinvoice(idInvoice) on delete cascade on update cascade,
foreign key(idService) references tservice(idService) on delete cascade on update cascade,
foreign key(idSparePart) references tsparepart(idSparePart) on delete cascade on update cascade,
foreign key(idWorkOrder) references tworkorder(idWorkOrder) on delete cascade on update cascade
) engine=innodb;
