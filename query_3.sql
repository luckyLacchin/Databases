SELECT M.title, 'most profitable' as feature
FROM Movies M
WHERE M.title IN 
(SELECT Temp2.title
FROM (SELECT F.title, F.gross - F.budget AS profit2 FROM movies F) AS TEMP2, 
(SELECT  MAX(Temp.profit)AS maxProfit
FROM  (SELECT M.title, M.gross - M.budget AS profit
FROM movies M) AS Temp) AS TEMP3
WHERE TEMP2.profit2=TEMP3.maxProfit)
UNION
SELECT M.title, 'least expensive' as feature
FROM movies M
WHERE M.budget IN (SELECT MIN(M.budget) FROM movies M)






