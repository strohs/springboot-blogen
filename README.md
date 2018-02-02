[![CircleCI](https://circleci.com/gh/strohs/springboot-blogen.svg?style=svg)](https://circleci.com/gh/strohs/springboot-blogen)
[![codecov](https://codecov.io/gh/strohs/springboot-blogen/branch/master/graph/badge.svg)](https://codecov.io/gh/strohs/springboot-blogen)


Spring Boot Blogen
==========================================================================================
Blogen is a fictional micro-blogging/message-board website powered by Spring Boot on the backend with Bootstrap 4 on
the frontend. This project was a means for me to integrate various Spring Framework projects with Bootstrap,
a technology I have been wanting to learn for some time.

## Overview
Blogen users login to the site and post their thoughts on a variety of categories (such as Technology, Web Development,
Health, etc..) Users can also link an image to their post, which will display on the Blogen main page.
Once a user has made a post, other Blogen users can then reply to the post, starting a thread of conversation.


![Blogen Main Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenMain.jpg)
![Blogen Posts Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenPosts.jpg)
![Blogen Profile Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenProfile.jpg)


## Running
* You will need to have Java 1.8 installed on your system (JDK or JRE) and have the **JAVA_HOME** environment variable set
* Clone the repository
* cd into the main project directory and run maven wrapper:
  1. ```./mvnw clean install```   (on windows systems run ```mvnw.cmd clean install```)
  2. ```./mvnw springboot:run```  (on windows systems run ```mvnw.cmd springboot:run```)
  3. open your web browser to ```http://localhost:8080```


## Using Blogen
From the Blogen main page, use the Login link (on the upper right) to log into Blogen (see Blogen Users below).


Once logged in you will be taken to the user posts page. This page presents a list of the most recent posts made and allows
you to create a new post, reply to a post, edit an existing post, or delete a post. Note that users can only edit or
delete posts they have made. Admins can edit or delete any post. Also note that for purposes of simplicity, replying to a
post only goes one level deep, on other words, you cannot reply to a reply.


Users can also filter posts by category or search post text and titles for a specific string. (The search is a brute f
orce SQL *LIKE* search. A production site would probably use a proper search engine like Apache Lucene.)

### Admin user
In addition to the regular functionality provided above, admin users can create new categories. The link to create new
 categories will be on the navbar.



### Blogen Users
The following users are provided out of the gate, including an admin user
* username: **johndoe** password: **password**
* username: **scotsman** password: **password**
* username: **mgill** password: **password**
* username: **lizreed** password: **password**
* username: **admin** password: **adminpassword**

## REST Api
Blogen also includes a REST Api that lets you perform CRUD operations on Posts,Categories, and Users.
You can view the REST Documentation here: 

## Software Used in the Project
* Spring Boot (1.5.9)
* Spring MVC
* Spring Data JPA
* REST Api
* Project Lombok
* Springfox (for documenting the REST Api in the OpenAPI format e.g. Swagger )
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




