/* 회원 관리 */
create table zmember(
	mem_num number not null,
	id varchar2(12) unique not null,
	auth number(1) default 2 not null, /*회원등급: 0(탈퇴) 1(정지) 2(일반) 3(관리자)*/
	constraint zmember_pk primary key (mem_num)
);

create table zmember_detail(
	mem_num number not null,
	name varchar2(30) not null,
	passwd varchar2(12) not null,
	phone varchar2(15) not null,
	email varchar2(50) not null,
	zipcode varchar2(5) not null,
	address1 varchar2(90) not null,
	address2 varchar2(90) not null,
	photo varchar2(150),
	reg_date date default sysdate not null,
	modify_date date,
	constraint zmember_detail_pk primary key (mem_num),
	constraint zmember_datail_fk foreign key (mem_num) references zmember (mem_num)
);

create sequence zmember_seq;


/* 게시판 관리 */
create table zboard(
	board_num number not null,
	title varchar2(150) not null,
	content clob not null, /* clob : 최대 4GB */
	hit number(5) default 0 not null, /*조회수*/
	reg_date date default sysdate not null,
	modify_date date,
	filename varchar2(150),
	ip varchar2(40) not null,
	mem_num number not null, /* FK는 대체로 참조할 다른 테이블의 PK명과 동일하는게 편함 */
	constraint zboard_pk primary key (board_num),
	constraint zboard_fk foreign key (mem_num) references zmember (mem_num)
);

create sequence zboard_seq;


/* 댓글 관리 */
create table zboard_reply(
	re_num number not null,
	re_content varchar2(900) not null,
	re_date date default sysdate not null,
	re_modifydate date,
	re_ip varchar2(40) not null,
	board_num number not null,
	mem_num number not null,
	constraint zreply_pk primary key (re_num),
	constraint zreply_fk1 foreign key (board_num) references zboard (board_num),
	constraint zreply_fk2 foreign key (mem_num) references zmember (mem_num)
);

create sequence zboard_reply_seq;


/* 상품 관리 */
create table zitem(
	item_num number not null,
	name varchar2(30),
	price number(8) not null,
	quantity number(5) not null,
	photo1 varchar2(60) not null,
	photo2 varchar2(60) not null,
	detail clob not null,
	reg_date date default sysdate not null,
	modify_date date,
	status number(1) not null,
	constraint zitem_pk primary key (item_num)
);

create sequence zitem_seq;