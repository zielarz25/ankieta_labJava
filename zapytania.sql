-- dodawanie nowego pytania
INSERT INTO pytania (id_pytania, tresc_pytania)
VALUES (1, "Tresc pytania")

-- dodawanie mozliwych odpowiedzi
INSERT INTO odpowiedzi (id_pytania, id_odpowiedzi, tresc_odpowiedzi)
VALUES 	(1, 'a', "OdpA"),
		(1, 'b', "OdpB"),
		(1, 'c', "OdpC"),
		(1, 'd', "OdpD");

-- dodawanie odpowiedzi uzytkownika (uzytkownik 1 - pytanie 1, odpowiedz B)
INSERT INTO odpowiedzi_uzytkownikow (id_uzytkownika, id_pytania, id_odpowiedzi)
VALUES	(1, 1, 'b');

--wyswietlenie tekstowo pytan i odpowiedzi
SELECT id_uzytkownika, tresc_pytania, tresc_odpowiedzi FROM odpowiedzi_uzytkownikow NATURAL JOIN odpowiedzi NATURAL JOIN pytania 