# Graduation project TopJava internship
## Technical requirements

Design and implement a REST API using Spring-Boot/Spring Data JPA **without frontend**.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users <br>
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price) <br>
Menu changes each day (admins do the updates) <br>
Users can vote for a restaurant they want to have lunch at today
Only one vote counted per user

If user votes again the same day:
 - If it is before 11:00 we assume that he changed his mind.
 - If it is after 11:00 then it is too late, vote can't be changed <br>

Each restaurant provides a new menu each day.

### Java application on the most modern and popular stack: Spring Boot 3.x, Spring Data Rest/HATEOAS, Lombok, JPA, H2, ....

-------------------------------------------------------------
- Stack: [JDK 21](https://jdk.java.net/21/), Spring Boot 3.x, Lombok, H2, Caffeine Cache, SpringDoc OpenApi 2.x
- Run: `mvn spring-boot:run` in root directory.
-----------------------------------------------------
[REST API documentation](http://localhost:8080/)

Credentials:<br>
User:  `user@yandex.ru` / `password`<br>
Admin: `admin@gmail.com` / `admin` <br>
Guest: `guest@gmail.com` / `guest`
