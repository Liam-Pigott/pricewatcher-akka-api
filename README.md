# pricewatcher-akka-api

A simple REST application using Akka HTTP, Slick and MySQL to act as an extension to expose data already collected by [The Python Price Watcher](https://github.com/Liam-Pigott/pricewatcher).

### Setup and usage
This example uses MySQL and assumes you already have a database created. The current implementation uses the following environment variables out of the box:
- `PRICEWATCH_MYSQL_HOST` - MySQL instance host name
- `PRICEWATCH_MYSQL_DATABASE` - MySQL database name
- `PRICEWATCH_MYSQL_USER` - Authorised user name
- `PRICEWATCH_MYSQL_PASS` - User password

Modify `application.conf` to change this setup. By default the application runs on `localhost:8080`.

**Service**: `sbt run` <br>
**Tests**: `sbt test`

### API endpoints
**Prices**
- `GET /api/prices`
- `GET /api/prices/:id`

### Future Developments
- Migrate watchers to database and add the associated CRUD operations
- Add swagger docs
- Security
