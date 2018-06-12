[![CircleCI](https://circleci.com/gh/strohs/springboot-blogen.svg?style=svg)](https://circleci.com/gh/strohs/springboot-blogen)
[![codecov](https://codecov.io/gh/strohs/springboot-blogen/branch/master/graph/badge.svg)](https://codecov.io/gh/strohs/springboot-blogen)


Spring Boot Blogen
==========================================================================================
Blogen is a fictional micro-blogging/message-board website I created that brings together various front/back end 
technologies I have been working with. It is powered by Spring Boot (on the backend) with Bootstrap 4
on the frontend. 


A variety of Spring projects have been used, mainly: spring-data-jpa, spring-mvc,
spring-test, plus many more. Unit Tests and Integration tests have also been written using Junit and Mockito.
In addition to the web-site, a REST Api has also been developed that will let you perform CRUD operations on
Users,Posts, and Categories.

## Overview
Blogen lets users post their thoughts on a variety of categories (such as Technology, Web Development, Health, etc..).
Along with the text of the post, users can optionally provide a link to an image, which will get displayed on the front
page of Blogen. Users can create new posts, reply to posts, plus edit and delete any existing posts they
have created. Additionally, users can also perform filtering operations on posts, as well as search posts for
a specific textual string.


![Blogen Main Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenMain.jpg)
![Blogen Posts Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenPosts.jpg)
![Blogen User Profile Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenEditProfile.jpg)


## Running
* You will need to have Java 1.8 installed on your system (JDK or JRE) and have the **JAVA_HOME** environment variable set
* Clone the *springboot-blogen* repository
* cd into the main project directory and use maven wrapper to run:
  1. ```./mvnw clean install```   (on windows systems run ```mvnw.cmd clean install```)
  2. ```./mvnw spring-boot:run```  (on windows systems run ```mvnw.cmd spring-boot:run```)
  3. open your web browser to ```http://localhost:8080```
  4. The main blogen home page should display (it will attempt to load images from the lorempixel website, which is slow)



## Using Blogen
From the Blogen main page, use the Login link (on the upper right) to log into Blogen (see Blogen Users below).


Once logged in you will be taken to the user posts page. This page presents a list of the most recent posts made and allows
you to create a new post, reply to a post, edit an existing post, or delete a post. Note that users can only edit or
delete posts they have made. Admins can edit or delete any post. Also note that for purposes of simplicity, replying to a
post only goes one level deep, in other words, you cannot reply to a reply.


### Admin user
In addition to the regular functionality provided above, admin users can create new categories. The link to create new
 categories will appear on the navbar if you are logged in as the admin.


### Blogen Users
There are four sample users plus one administrator provided out of the gate:
* username: **johndoe** password: **password**
* username: **scotsman** password: **password**
* username: **mgill** password: **password**
* username: **lizreed** password: **password**
* username: **admin** password: **adminpassword**

Each user has a public facing "home" page that lets you see all the posts they have made. These pages are not protected
 by Spring Security and can be reached at ```http://localhost:8080/users/{username}``` where {username} is the Blogen
 user name e.g. ```mgill```


## REST Api
There is also a REST Api included with with project. The API is not currently secured but it has been documented
with OpenAPI (Swagger). Once you have started the project, the docs are available
here:  http://localhost:8080/swagger-ui.html#/

The Api lets you perform CRUD operations on Posts,Categories,and Users.




## Software Used in the Project
* Spring Boot (1.5.9)
* Spring MVC
* Spring Data JPA
* H2 Database
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




