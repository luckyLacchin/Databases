SELECT DISTINCT M.title
FROM movieawards M
WHERE m.year = (SELECT MAX(Temp.year)
FROM (SELECT A.title as title, MAX(A.year) AS year
FROM movieawards A
WHERE A.result='won'
GROUP BY A.title
HAVING COUNT(*) >= 3) AS Temp) AND M.title IN (SELECT A.title
FROM movieawards A
WHERE A.result='won'
GROUP BY A.title
HAVING COUNT(*) >= 3) 

