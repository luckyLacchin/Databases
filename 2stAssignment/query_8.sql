SELECT ROUND(CAST(num_oscar_table.num_oscar AS DECIMAL)/num_movies_table.num_movies,2) AS Oscar_rate
FROM 
(SELECT COUNT(TEMP1) AS num_oscar
FROM (SELECT A.title, A.year
FROM movieawards A
WHERE A.year >= 1980 AND A.year <= 1989 AND A.award LIKE 'Oscar%' AND A.result = 'won'
GROUP BY A.title, A.year) AS TEMP1) AS num_oscar_table, 
(SELECT COUNT(*) AS num_movies
FROM movies M
WHERE M.year >= 1980 AND M.year <= 1989) AS num_movies_table
WHERE num_movies_table.num_movies != 0
UNION
SELECT -1 AS Oscar_rate
FROM
(SELECT COUNT(*) AS num_movies
FROM movies M
WHERE M.year >= 1980 AND M.year <= 1989) AS num_movies_table
WHERE num_movies_table.num_movies = 0;








