CREATE TABLE odpowiedzi (
  id_pytania int(11) NOT NULL,
  id_odpowiedzi char(1) NOT NULL,
  tresc_odpowiedzi varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE odpowiedzi_uzytkownikow (
  id_uzytkownika int(11) NOT NULL,
  id_pytania int(11) NOT NULL,
  id_odpowiedzi char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE pytania (
  id_pytania int(11) NOT NULL,
  tresc_pytania varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE odpowiedzi
  ADD PRIMARY KEY (id_pytania,id_odpowiedzi);
ALTER TABLE odpowiedzi_uzytkownikow
  ADD PRIMARY KEY (id_uzytkownika,id_pytania),
  ADD KEY id_pytania (id_pytania),
  ADD KEY id_odpowiedzi (id_odpowiedzi);
ALTER TABLE pytania
  ADD PRIMARY KEY (id_pytania);  
ALTER TABLE odpowiedzi_uzytkownikow
	ADD FOREIGN KEY (id_pytania, id_odpowiedzi) REFERENCES odpowiedzi(id_pytania, id_odpowiedzi),
	ADD FOREIGN KEY (id_pytania) REFERENCES pytania(id_pytania);