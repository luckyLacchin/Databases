SELECT Temp.title, ROUND(CAST(Temp.vinti AS DECIMAL)/Temp1.totali,2) AS success_rate
FROM (SELECT A.title, A.year, COUNT(*) AS vinti
FROM movieawards A
WHERE A.result = 'won'
GROUP BY A.title, A.year) AS Temp,
(SELECT A.title, A.year, COUNT(*) AS totali
FROM movieawards A
GROUP BY A.title, A.year) AS Temp1
WHERE Temp1.title = Temp.title AND Temp1.year = Temp.year
UNION
SELECT M.title, -1 AS success_rate
FROM Movies M
WHERE NOT EXISTS (SELECT * FROM MovieAwards A WHERE A.title = M.title AND A.year = M.year);