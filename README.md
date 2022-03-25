# PlanIt
School project created for course Development of Applications With Multilayer Architecture, that I improved in my free time afterwards. It's a calendar that provides basic features, 
but there are also some none trivial functionalities like a PDF export of calendar or support of repeated events. 

This project was my first app with client-server architecture and it was also my first experience with backend framework.

## Technologies
- JavaFX 1.8
- Spring Boot
- JWT auth
- PostgreSQL
- external APIs
  * ipify API: to get public IP of client device
  * IP API: to get location by public IP
  * OpenWeatherMap API: to get weather forecast for location

## Instalation
Installation guide for local development:

### Backend
1. Go to `ServerProject\` folder.
1. Create and fill configuration file `src\main\resources\application.properties`. Example of file is in `src\main\resources\application.properties.example`.
2. Create and fill configuration file `src\main\resources\uri.properties` (used for storing API keys for external APIs). Example of file is in `src\main\resources\uri.properties.example`.
3. Create database and then create schema from `src\main\resources\schema.sql` file, e.g. via `psql -h <hostname> -d <databasename> -U <username> -f schema.sql` command.
4. Open project in some IDE (I used IntelliJ IDEA) and use its tools to build and run application or start application via command `mvn spring-boot:run`.

### Frontend
1. Go to `ClientProject\` folder.
1. If needed change values in `src\main\resources\uri.properties`.
2. Open project in some IDE (I used IntelliJ IDEA) and use its tools to build and run application.


## Functionality
- registration and logging in
- add event
- update event
- delete event
- display events by month in calendar
- show event detail
- events alerts
- repeated events support
- PDF export
- weather forecast for one week
