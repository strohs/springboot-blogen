[![CircleCI](https://circleci.com/gh/strohs/springboot-blogen.svg?style=svg)](https://circleci.com/gh/strohs/springboot-blogen)
[![codecov](https://codecov.io/gh/strohs/springboot-blogen/branch/master/graph/badge.svg)](https://codecov.io/gh/strohs/springboot-blogen)


Spring Boot Blogen
==========================================================================================
Blogen is a fictional micro-blogging website powered by Spring Boot on the backend with Bootstrap 4 on the frontend.
This application was a way for me to integrate various spring framework projects I had been using into a single
end to end application.  Additionally it also enabled me to learn some Bootstrap, something I had been
wanting to do for some time now.

## Overview
Blogen lets users post their thoughts on a variety of categories (such as Technology, Web Development, Health, etc..).
It essentially a "mini" message board that lets users perform CRUD operations on posts. Users can also perform filter
operations on the posts as well as search the text/title of posts.
In addition to the text of a post, users can link an image to their post (hosted on the web) which will display on
the Blogen main page.

![Blogen Main Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenMain.jpg)
![Blogen Posts Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenPosts.jpg)
![Blogen User Profile Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenEditProfile.jpg)


## Running
* You will need to have Java 1.8 installed on your system (JDK or JRE) and have the **JAVA_HOME** environment variable set
* Clone the repository
* cd into the main project directory and use maven wrapper to run:
  1. ```./mvnw clean install```   (on windows systems run ```mvnw.cmd clean install```)
  2. ```./mvnw spring-boot:run```  (on windows systems run ```mvnw.cmd spring-boot:run```)
  3. open your web browser to ```http://localhost:8080```


## Using Blogen
From the Blogen main page, use the Login link (on the upper right) to log into Blogen (see Blogen Users below).


Once logged in you will be taken to the user posts page. This page presents a list of the most recent posts made and allows
you to create a new post, reply to a post, edit an existing post, or delete a post. Note that users can only edit or
delete posts they have made. Admins can edit or delete any post. Also note that for purposes of simplicity, replying to a
post only goes one level deep, in other words, you cannot reply to a reply.


Users can also filter posts by category or search post text and titles for a specific text string. (The search is a brute
 force SQL *LIKE* search. A production site would probably use a proper search engine like Apache Lucene.)

### Admin user
In addition to the regular functionality provided above, admin users can create new categories. The link to create new
 categories will be on the navbar.


### Blogen Users
There are four sample users plus one administrator provided out of the gate:
* username: **johndoe** password: **password**
* username: **scotsman** password: **password**
* username: **mgill** password: **password**
* username: **lizreed** password: **password**
* username: **admin** password: **adminpassword**


## REST Api
There is also a REST Api included with with project. It has been documented with OpenAPI (Swagger) and those docs
are available here:  http://http://localhost:8080/swagger-ui.html#/

The Api provides CRUD operations on Posts,Categories,and Users.



## Software Used in the Project
* Spring Boot (1.5.9)
* Spring MVC
* Spring Data JPA
* Project Lombok
* MapStruct
* Hibernate (as the JPA provider)
* Spring Security
* Thymeleaf (for the view layer)
* Thymeleaf Security Extras (to integrate with Spring Security)
* H2 embedded database
* JUnit4
* Mockito
* CircleCI to run integration tests
* Codecov.io to provide code coverage reports
* Bootstrap 4 (for the front-end web pages)




