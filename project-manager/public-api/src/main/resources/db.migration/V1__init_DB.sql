create table card (
                      id serial not null,
                      primary key (id)
)

create table conversation (
                              id serial not null,
                              created_at timestamp(6),
                              updated_at timestamp(6),
                              primary key (id)
)

create table friendship_request (
                                    id serial not null,
                                    receiver_id integer,
                                    sender_id integer,
                                    status smallint not null check (status between 0 and 2),
                                    created_at timestamp(6),
                                    primary key (id)
)

create table images (
                        id serial not null,
                        party_id integer not null,
                        name varchar(255),
                        status varchar(255),
                        primary key (id)
)

create table messages (
                          conversation_id integer not null,
                          id serial not null,
                          sender_id integer not null,
                          send_at timestamp(6),
                          body varchar(400),
                          primary key (id)
)

create table operations (
                            amount float(53) not null,
                            card_id integer not null,
                            id serial not null,
                            party_id integer not null,
                            person_id integer not null,
                            status smallint not null check (status between 0 and 2),
                            created_at timestamp(6),
                            primary key (id)
)

create table participation_request (
                                       id serial not null,
                                       party_id integer not null,
                                       person_id integer not null,
                                       primary key (id)
)

create table party (
                       age_restriction integer not null,
                       average_rate float(53),
                       count_of_places integer not null,
                       draft_id integer unique,
                       id serial not null,
                       minimal_rating float(53) check (minimal_rating<=5),
                       person_id integer not null,
                       status_id integer not null,
                       ticket_cost float(53),
                       type_id integer,
                       created_at timestamp(6),
                       date_of_event timestamp(6),
                       updated_at timestamp(6),
                       address varchar(20),
                       city varchar(20),
                       description varchar(255),
                       name varchar(255),
                       primary key (id)
)

create table party_create_draft (
                                    age_restriction integer not null,
                                    count_of_places integer not null,
                                    id serial not null,
                                    minimal_rating float(53),
                                    person_id integer,
                                    status_id integer,
                                    ticket_cost float(53),
                                    type_id integer,
                                    created_at timestamp(6),
                                    date_of_event timestamp(6),
                                    updated_at timestamp(6),
                                    address varchar(255),
                                    city varchar(255),
                                    description varchar(255),
                                    name varchar(255),
                                    primary key (id)
)

create table party_draft (
                             age_restriction integer not null,
                             count_of_places integer not null,
                             draft_flag boolean not null,
                             id serial not null,
                             minimal_rating float(53),
                             party_id integer not null,
                             person_id integer,
                             status_id integer,
                             ticket_cost float(53),
                             type_id integer,
                             created_at timestamp(6),
                             date_of_event timestamp(6),
                             updated_at timestamp(6),
                             address varchar(255),
                             city varchar(255),
                             description varchar(255),
                             name varchar(255),
                             primary key (id)
)

create table party_status (
                              id serial not null,
                              status varchar(255),
                              primary key (id)
)

create table party_draft_status_model (
                                          id serial not null,
                                          name varchar(255),
                                          primary key (id)
)

create table party_draft_type (
                                  id serial not null,
                                  name varchar(255),
                                  primary key (id)
)

create table party_entity_rates (
                                    party_entity_id integer not null,
                                    rates integer
)

create table party_type (
                            id serial not null,
                            type varchar(255),
                            primary key (id)
)

create table person (
                        age integer not null check ((age>=14) and (age<=100)),
                        id serial not null,
                        is_active boolean not null,
                        rating float(53) not null,
                        role integer,
                        created_at timestamp(6),
                        updated_at timestamp(6),
                        city varchar(15),
                        email varchar(255) unique,
                        status varchar(255),
                        username varchar(255) unique,
                        primary key (id)
)

create table person_conversations (
                                      conversation_id integer not null,
                                      id integer not null
)

create table person_friends (
                                friend_id integer not null,
                                person_id integer not null
)

create table person_parties (
                                id integer not null,
                                party_id integer not null
)

create table person_role (
                             id serial not null,
                             role_name varchar(255),
                             primary key (id)
)

alter table if exists friendship_request
    add constraint FKaua7ace2ug24u05fy5o2m40sb
    foreign key (receiver_id)
    references person

alter table if exists friendship_request
    add constraint FKp7dofi66xvhd1gfmnf5emjyd8
    foreign key (sender_id)
    references person

alter table if exists images
    add constraint FKqj20dedhvbvfbgw5t1ktduxfq
    foreign key (party_id)
    references party

alter table if exists messages
    add constraint FKde7b83jwmlq63aqpj5qtege0h
    foreign key (conversation_id)
    references conversation

alter table if exists messages
    add constraint FKfa47uddmk21gl92dyro0odanh
    foreign key (sender_id)
    references person

alter table if exists operations
    add constraint FK5tfgg53lfreinkdjueya23qx0
    foreign key (card_id)
    references card

alter table if exists operations
    add constraint FKk3c8jcln9f1f63rt3ykmy57sa
    foreign key (party_id)
    references party

alter table if exists operations
    add constraint FKbw38sbebrpln79si8fyv3h0qb
    foreign key (person_id)
    references person

alter table if exists participation_request
    add constraint FKstv9oeq6n6ipraysktddemsx4
    foreign key (party_id)
    references party

alter table if exists participation_request
    add constraint FKmwqlvl8elso3wg3171csqte9y
    foreign key (person_id)
    references person

alter table if exists party
    add constraint FKtfi5dsba4ft6uwq6f72gl96m6
    foreign key (draft_id)
    references party_draft

alter table if exists party
    add constraint FK9q6rcrq4oiwb1pik6mfepn2uh
    foreign key (person_id)
    references person

alter table if exists party
    add constraint FKrx9oq1hl46lfbt8q5ygg66b9k
    foreign key (status_id)
    references party_status

alter table if exists party
    add constraint FKskpedn0mnnikaetjahtuh220l
    foreign key (type_id)
    references party_type

alter table if exists party_create_draft
    add constraint FK81sg1hq8x9tp4e8kp90xf97ku
    foreign key (person_id)
    references person

alter table if exists party_create_draft
    add constraint FK81oc65gl5nysbdvatif43icgi
    foreign key (status_id)
    references party_draft_status_model

alter table if exists party_create_draft
    add constraint FKlf4itsavspryf99vbkog77v9m
    foreign key (type_id)
    references party_draft_type

alter table if exists party_draft
    add constraint FKqvossysr3c748l48exv1bomwa
    foreign key (person_id)
    references person

alter table if exists party_draft
    add constraint FK7wbapt3pslvvrmr78dhdddr29
    foreign key (status_id)
    references party_draft_status_model

alter table if exists party_draft
    add constraint FKhloxle6c2ktvh1314wq55d2jo
    foreign key (type_id)
    references party_draft_type

alter table if exists party_draft
    add constraint FKhrq4nrn33230xg1gphi9n3i6b
    foreign key (party_id)
    references party

alter table if exists party_entity_rates
    add constraint FK8bcxppi3op7omonqa8phtrif2
    foreign key (party_entity_id)
    references party

alter table if exists person
    add constraint FKtajt5xd9dew023xqn5yc6m0lm
    foreign key (role)
    references person_role

alter table if exists person_conversations
    add constraint FKl0fw0becr5wab97qn9lyp7xqj
    foreign key (id)
    references conversation

alter table if exists person_conversations
    add constraint FKgr14vs1h75om90p8lkiibn6gr
    foreign key (conversation_id)
    references person

alter table if exists person_friends
    add constraint FKncwk82h1m2kr2cnifxyxan2o8
    foreign key (friend_id)
    references person

alter table if exists person_friends
    add constraint FKgn86dw7uubwv01fcjbnr91o11
    foreign key (person_id)
    references person

alter table if exists person_parties
    add constraint FK5ptqbb554q34vfi55x6xe1v91
    foreign key (id)
    references party

alter table if exists person_parties
    add constraint FKbv50lnlgeg4ajosimlf7vg359
    foreign key (party_id)
    references person