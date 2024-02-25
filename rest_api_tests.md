Examples for the Rest API tests using CURL
===============================

# 1. Tests for Meals

### 1.1 Get Meal by ID:
curl 'http://localhost:8080/topjava/rest/meals/100003'

### 1.2 Get all meals:
curl 'http://localhost:8080/topjava/rest/meals/'

### 1.3 Delete meal by ID:
curl --request DELETE 'http://localhost:8080/topjava/rest/meals?id=100004'

### 1.4 Create meal:
curl 'http://localhost:8080/topjava/rest/meals' \
--header 'Content-Type: application/json' \
--data '{
"dateTime": "2024-02-19T20:00:00",
"description": "Ужин",
"calories": 777
}'

### 1.5 Update meal:
curl --request PUT 'http://localhost:8080/topjava/rest/meals?id=100005' \
--header 'Content-Type: application/json' \
--data '{
"dateTime": "2024-02-19T19:00:00",
"description": "Новый Ужин",
"calories": 777
}'

### 1.6 Get meals with filter by startDate, startTime, endDate, endTime:
curl 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&startTime=09%3A00%3A00&endDate=2020-01-30&endTime=14%3A00%3A00'

# 2. Tests for Users

### 2.1 Get user with meals usig Admin URL
curl 'http://localhost:8080/topjava/rest/admin/users/100001/with-meals'

### 2.2 Get user with meals usig non-admin URL
curl 'http://localhost:8080/topjava/rest/profile/with-meals'