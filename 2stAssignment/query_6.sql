SELECT M.title, M.year
FROM movieawards M
WHERE m.result ='won'
GROUP BY M.title, M.year
HAVING COUNT(*) = (
SELECT MAX(TEMP.num_oscar)
FROM (
SELECT M.title, M.year, COUNT(*) as num_oscar
FROM movieawards M
WHERE m.result = 'won'
GROUP BY M.title, M.year) AS TEMP)

