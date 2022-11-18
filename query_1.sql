SELECT DISTINCT Temp.directorTemp, Temp.yearTemp, MIN(Temp.profit) AS MINprofit, MAX(Temp.profit) AS MAXprofit, AVG (Temp.profit) AS AVGprofit
FROM (SELECT D.director as directorTemp, D.yearOfBirth as yearTemp, M.gross - M.budget AS profit
FROM movies M, directors D
WHERE D.yearOfBirth <= 2022-50 AND D.director = M.director) AS Temp
GROUP BY Temp.directorTemp, Temp.yearTemp
UNION
SELECT D.director, D.yearOfBirth, -1 AS MINprofit, -1 AS MAXprofit, -1 AS AVGprofit
FROM directors D
WHERE d.director NOT IN ( SELECT M.director FROM movies M)
