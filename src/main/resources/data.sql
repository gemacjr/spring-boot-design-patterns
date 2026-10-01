-- Seed rows for the Repository pattern demo. Loaded because
-- spring.jpa.defer-datasource-initialization=true lets data.sql run after
-- Hibernate has created the schema.
INSERT INTO book (title, author, published_year) VALUES ('A Wizard of Earthsea', 'Ursula K. Le Guin', 1968);
INSERT INTO book (title, author, published_year) VALUES ('The Dispossessed', 'Ursula K. Le Guin', 1974);
INSERT INTO book (title, author, published_year) VALUES ('The Left Hand of Darkness', 'Ursula K. Le Guin', 1969);
INSERT INTO book (title, author, published_year) VALUES ('Kindred', 'Octavia E. Butler', 1979);
INSERT INTO book (title, author, published_year) VALUES ('Parable of the Sower', 'Octavia E. Butler', 1993);
