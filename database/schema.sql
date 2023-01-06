
CREATE DATABASE eshop;

create table customers (
    name varchar(32) not null,
    address varchar(128) not null,
    email varchar(128),

    primary key (name)

    
);

insert into customers values ('fred', '201 Cobblestone Lane', 'fredflintstone@bedrock.com');
insert into customers values ('sherlock', '21B Baker Street, London', 'sherlock@consultingdetective.org');
insert into customers values ('spongebob', '124 Conch Street, Bikini Bottom', 'spongebob@yahoo.com');
insert into customers values ('jessica', '698 Candlewood Land, Cabot Cove', 'fletcher@gmail.com');
insert into customers values ('dursley', '4 Privet Drive, Little Whinging, Surrey', 'dursley@gmail.com');

create table orders (

    orderId char(8) not null,
    deliveryId varchar(32) not null,
    address varchar(128) not null,
    email varchar(128),
    status enum('dispatched', 'pending') not null,
    orderDate date not null,

    primary key (orderId),

    -- foreign key
    name varchar(32) not null,

    constraint fk_name
    foreign key(name) references customers(name)

);


create table lineitem (
    id int auto_increment not null,
    item varchar(128) not null,
    quantity int not null,

    -- foreign key
    orderId char(8) not null,

    primary key (id),

    constraint fk_orderId
    foreign key(orderId) references orders(orderId)

);


create table order_status (
    order_id char(8) not null,
    delivery_id varchar(32) not null,
    status enum('dispatched', 'pending') not null,
    status_update date not null,

    primary key (order_id)
    
);

    