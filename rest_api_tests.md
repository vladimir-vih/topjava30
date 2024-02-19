MealRestController

get:
curl 'http://localhost:8080/topjava/rest/meals/100003'

getAll:
curl 'http://localhost:8080/topjava/rest/meals/'

delete:
curl --request DELETE 'http://localhost:8080/topjava/rest/meals?id=100004'

create:
curl 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '{
"dateTime": "2024-02-19T20:00:00",
"description": "Ужин",
"calories": 777
}'

update:
curl --request PUT 'http://localhost:8080/topjava/rest/meals?id=100005' \
--header 'Content-Type: application/json' \
--data '{
"dateTime": "2024-02-19T19:00:00",
"description": "Новый Ужин",
"calories": 777
}'

getBetween:
curl 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=09%3A00%3A00&endDate=2020-01-30&endTime=14%3A00%3A00'


AdminRestController
getWithMeals:
curl 'http://localhost:8080/topjava/rest/admin/users/with-meals?id=100001'


ProfileRestController
getWithMeals:
curl 'http://localhost:8080/topjava/rest/profile/with-meals?id=100000'