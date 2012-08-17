# --- First database schema
 
# --- !Ups

create table article (
  id   	     bigint not null primary key,
  title	     varchar(255) not null,
  content    clob,
  date	     timestamp
);

create sequence article_seq start with 1000;

# --- !Downs

drop table if exists article;
drop sequence if exists article_seq;
