CREATE TABLE produto (
	id serial PRIMARY KEY,
	nome VARCHAR (50),
	quantidade INT
);

INSERT INTO produto (id, nome, quantidade) VALUES (1, 'Banana', 100);
INSERT INTO produto (id, nome, quantidade) VALUES (2, 'Laranja', 200);

SELECT * FROM produto;