WITH tempo AS
(SELECT DISTINCT A.year
FROM movieawards A, movies M
WHERE A.title = M.title AND A.year = M.year AND M.director = 'Spielberg' AND A.result = 'won' 
GROUP BY A.title, A.year
HAVING COUNT(*) >= 3) 
SELECT DISTINCT D.director
FROM tempo, directorawards D
WHERE D.result = 'won' AND D.year = tempo.year
GROUP BY D.director, D.year
HAVING COUNT(*) = COUNT(tempo)

                   