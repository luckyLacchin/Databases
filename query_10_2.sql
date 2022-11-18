SELECT D.director
FROM directors D
WHERE NOT EXISTS (SELECT DISTINCT D1.director
                  FROM directorawards D1
                  WHERE D1.director = D.director AND NOT EXISTS (SELECT M.title, M.year
                                                                 FROM movies M
                                                                 WHERE M.year = D1.year AND M.director = 'Spielberg' AND NOT EXISTS (SELECT DISTINCT A.title, A.year
                                                                                                           								FROM movieawards A
                                                                                                          								WHERE A.title = M.title AND A.year = M.year AND A.result = 'won'
                                                                                                                                     	GROUP BY A.title, A.year
                                                                                                                                     	HAVING COUNT (*) >= 3)));

                   