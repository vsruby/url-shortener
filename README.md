# URL Shortener

## Setup
1. Make sure that both Java 17, The Maven package manager, and Docker with Compose are installed.
2. Copy the `.env.sample` into a new file called `.env` and fill in the values. There are defaults setup but creating the `.env` file will allow for customization. Both the application and the docker container will use the values.
    1. The `POSTGRES_DB` is used for the name of the database. Default is `url_shortener`.
    2. The `POSTGRES_PASSWORD` is used for postgres user password. Default is `postgres`.
    3. The `POSTGRES_PORT` is used for port to bind postgres to. Default is `5432`.
    4. The `POSTGRES_USER` is used for the postgres user. Default is `postgres`.


## Build and Run
1. Make sure you have navigated to the root of the project
2. Run the command `docker compose up -d` to start docker.
3. Run the command `./mvnw spring-boot:run` to start the application. The application will listen on port `8080`.


## Run Tests
1. Run the command `./mvnw test` to run all tests.

#### NOTE: The application does not need to be running when running tests. The integration tests will run with an embedded database so the test data should not be reflected in the local database.

## Endpoints
### Create a Short Url
Make a `POST` request to `http://localhost:8080/short-urls` with body:
```json
{
    "destination": "<destination_url>"
}
```
Sample response:
```json
{
    "id": "037284f8-6f94-4a74-8476-f962c360e38f",
    "code": "WRRgU",
    "destination": "https://loremflickr.com/320/240",
    "createdAt": "2022-05-23T09:20:40.269425499-04:00",
    "updatedAt": "2022-05-23T09:20:40.269730937-04:00"
}
```

### Use the Short Url to be Redirected
Make a `GET` request to `http://localhost:8080/<code>`

### Get Usage Data
Make a `GET` request to `http://localhost:8080/clicks/<code>`
<br>
Sample Response:

```json
{
    "count": 2,
    "page": 0,
    "total": 2,
    "clicks": [
        {
            "id": "7c1548e5-02b6-4c19-95c2-7b01e7d6112d",
            "clickedAt": "2022-05-22T23:22:55.375493-04:00",
            "shortCodeClicked": "nsur7"
        },
        {
            "id": "661ade8e-dd30-4210-b85b-150ef6e50541",
            "clickedAt": "2022-05-22T23:22:57.780271-04:00",
            "shortCodeClicked": "nsur7"
        }
    ]
}
```

There are also two accepted query params `page` and `size` for the this paginated route.<br>
Sample Request: `http://localhost:8080/clicks/<code>?page=0&size=3`
Sample Response:
```json
{
    "count": 3,
    "page": 0,
    "total": 22,
    "clicks": [
        {
            "id": "ecf12758-7557-41d6-8c7f-6fa7a9de116d",
            "clickedAt": "2022-05-22T23:20:17.416597-04:00",
            "shortCodeClicked": "WRRgU"
        },
        {
            "id": "640b1300-505f-44e5-a4f7-c3717c522a40",
            "clickedAt": "2022-05-22T23:21:29.855056-04:00",
            "shortCodeClicked": "WRRgU"
        },
        {
            "id": "e5c2d403-07f3-4efe-81f3-3aa71cabaa80",
            "clickedAt": "2022-05-22T23:21:37.035355-04:00",
            "shortCodeClicked": "WRRgU"
        }
    ]
}
```