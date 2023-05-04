CREATE TABLE IF NOT EXISTS todolist
(
    id bigint NOT NULL AUTO_INCREMENT,
    title varchar(50) NOT NULL,
    content text NOT NULL,
    is_complete bit NOT NULL,
    parent bigint,
    PRIMARY KEY (id)
);