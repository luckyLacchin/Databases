SELECT DISTINCT M.director
FROM movieawards A, movies M
WHERE A.result = 'won' AND A.award LIKE '%best director' AND A.title = M.title AND A.year = M.year;