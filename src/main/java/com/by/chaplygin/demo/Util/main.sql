create table party (id serial not null,
                    address varchar(255), age_restriction integer,
                    date_of_create timestamp(6) without time zone,
                    date_of_event timestamp(6) without time zone,
                    description varchar(255),
                    instagram varchar(255),
                    name varchar(255),
                    person_id integer not null,
                    primary key (id));

create table person (id serial not null,
                     age integer,
                     date_of_create timestamp(6) without time zone,
                     date_of_update timestamp(6) without time zone,
                     email varchar(255),
                     is_active boolean, password varchar(255),
                     phone varchar(255),
                     score float(53),
                     username varchar(255),
                     primary key (id));

create table person_roles (person_id integer not null,
                           roles varchar(255) check (roles in ('ORGANIZER','ADMIN','USER')));

alter table if exists party add constraint FK9q6rcrq4oiwb1pik6mfepn2uh foreign key (person_id) references person;

alter table if exists person_roles add constraint FKs955luj19xyjwi3s1omo1pgh4 foreign key (person_id) references person