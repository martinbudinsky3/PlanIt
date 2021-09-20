# PlanIt
School project created for course Development of Applications With Multilayer Architecture, that I improved in my free time afterwards. It's a calendar that provides basic features, 
but there are also some none trivial functionalities like a PDF export of calendar or support of repeated events. 

This project was my first app with client-server architecture and it was also my first experience with backend framework.

## Technologies
- Java, JavaFX 1.8
- Spring Boot
- JWT auth
- PostgreSQL
- external APIs
  * ipify API: to get public IP of client device
  * IP API: to get location by public IP
  * OpenWeatherMap API: to get weather forecast for location

## Instalation
Just clone repository and open it in some IDE (I used IntelliJ IDEA 2020 1.1).
SQL commands to create and alter database are in database folder. Server project
has application.properties where database is configured. First build and run server
project and then client project.

## Functionality
- registration and logging in
- add event
- update event
- delete event
- display events by month
- show event detail
- events alerts
- repeated events support
- PDF export
- weather forecast for week
