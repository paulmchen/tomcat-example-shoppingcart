
-- create database shopping_cart charset utf8;
use shopping_cart;

    drop table if exists authorities;
    drop table if exists billingAddress;
    drop table if exists cart;
    drop table if exists cartitem;
    drop table if exists customer;
    drop table if exists customerorder;
    drop table if exists item;
    drop table if exists query;

    create table authorities (
        authorityId varchar(255) not null ,
        authorities varchar(255),
        emailId varchar(255),
        primary key (authorityId)
    );
    create table billingAddress (
        billindAddressId varchar(255) not null ,
        address varchar(255),
        city varchar(255),
        country varchar(255),
        state varchar(255),
        zipcode varchar(255),
        primary key (billindAddressId)
    );
 
    create table cart (
        cartId varchar(255) not null ,
        totalPrice double precision not null,
        customerId varchar(255),
        primary key (cartId)
    );
 
    create table cartitem (
        cartItemId varchar(255) not null ,
        price double precision not null,
        quality integer not null,
        cartId varchar(255),
        productId varchar(255),
        primary key (cartItemId)
    );
 
    create table customer (
        customerId varchar(255) not null ,
        customerPhone varchar(255),
        firstName varchar(255),
        lastName varchar(255),
        billingAddressId varchar(255),
        cartId varchar(255),
        shippingAddressId varchar(255),
        userId varchar(255),
        primary key (customerId)
    );
 
    create table customerorder (
        customerOrderId varchar(255) not null ,
        billingAddressId varchar(255),
        cartId varchar(255),
        customerId varchar(255),
        shippingAddressId varchar(255),
        primary key (customerOrderId)
    );

    create table item (
        Id varchar(255) not null ,
        category varchar(255),
        description text,
        manufacturer varchar(255),
        name varchar(255) not null,
        price double precision not null check (price>=100),
        unit varchar(255),
        primary key (Id)
    );    
    create table shippingAddress (
        shippingAddressId varchar(255) not null ,
        address varchar(255),
        city varchar(255),
        country varchar(255),
        state varchar(255),
        zipcode varchar(255),
        primary key (shippingAddressId)
    );
 
    create table users (
        userId varchar(255) not null ,
        emailId varchar(255),
        enabled boolean not null,
        password varchar(255),
        primary key (userId)
    );

    create table query (
        id integer not null auto_increment,
        email varchar(255),
        message varchar(255),
        subject varchar(255),
        primary key (id)
    );
