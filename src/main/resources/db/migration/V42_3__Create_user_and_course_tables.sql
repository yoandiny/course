create table "users" (
    id uuid primary key,
    first_name varchar(255),
    last_name varchar(255),
    address text,
    email varchar(255)
);

create table "courses" (
    id uuid primary key,
    name varchar(255),
    start_date timestamp,
    end_date timestamp
);

create table "courses_subscription" (
    user_id uuid references "users"(id),
    course_id uuid references "courses"(id),
    primary key (user_id, course_id)
);
