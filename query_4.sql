SELECT DISTINCT D.director, D.award
FROM movies M, directorawards D
WHERE M.director = D.director AND M.gross > 10000000 AND D.result='won' AND 2022 - M.year <= 5
UNION
SELECT DISTINCT D.director, A.award
FROM movieawards A, movies M, directorawards D
WHERE A.title = M.title AND A.year = M.year AND M.director = D.director AND A.award LIKE '%best director%'
AND M.gross > 10000000 AND D.result='won' AND 2022 - M.year <= 5;
