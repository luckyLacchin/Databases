SELECT DISTINCT K.director, 'oldest' AS feature
FROM (SELECT MIN(I.yearOfBirth) AS minBirth
FROM directorawards D, movies M, movieawards A, Directors I
WHERE (D.award = 'Oscar' OR (A.award = 'Oscar, best director' AND M.title = A.title AND M.year = A.year AND M.director = D.director)) AND (D.director = I.director)) TEMP, directors K
WHERE K.yearOfBirth = minBirth
UNION
SELECT DISTINCT K.director, 'youngest' AS feature
FROM ( SELECT MIN(I.yearOfBirth) AS maxBirth
FROM directorawards D, movies M, movieawards A, Directors I
WHERE (D.award = 'Oscar' OR (A.award = 'Oscar, best director' AND M.title = A.title AND M.year = A.year AND M.director = D.director))AND (D.director = I.director)) TEMP, directors K
WHERE K.yearOfBirth = maxBirth;

