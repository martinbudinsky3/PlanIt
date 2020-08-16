# PlanIt

School project, that I improved in my free time afterwards. It's just a simple calendar.

# Technologies

- Java, JavaFX 1.8
- Spring Boot
- PostgreSQL
- external APIs
  * ipify API: to get public IP of client device
  * IP API: to get location by public IP
  * OpenWeatherMap API: to get weather forecast for location

# Instalation

Just clone repository and open it in some IDE (I used IntelliJ IDEA 2020 1.1).
SQL commands to create and alter database are in database folder. Server project
has application.properties where database is configured. First build and run server
project and then client project.

# Functionality
- registration and logging in
- add event
- update event
- delete event
- display events by month
- show event detail
- events alerts
- PDF generation
- weather forecast for week

## TODO
- multiuser events
