create table Man (
                     id int primary key generated by default as identity,
                     name varchar(50) UNIQUE,
                     year_of_birth int check ( year_of_birth > 1900 )
);

create table Book (
                      id int primary key generated by default as identity,
                      name varchar(100),
                      author varchar(50),
                      date date,
                      man_id int,
                      FOREIGN KEY(man_id) REFERENCES Man(id) ON DELETE SET NULL
);