[![CircleCI](https://circleci.com/gh/strohs/springboot-blogen.svg?style=svg)](https://circleci.com/gh/strohs/springboot-blogen)
[![codecov](https://codecov.io/gh/strohs/springboot-blogen/branch/master/graph/badge.svg)](https://codecov.io/gh/strohs/springboot-blogen)


Spring Boot Blogen
==========================================================================================
Blogen is a fictional micro-blogging website powered by Spring Boot on the backend with Bootstrap 4 on the frontend.
While not fully production ready, this project was a means for me to integrate various front-end and back-end technologies
I have been working with over the past couple of years, mainly Spring and Spring MVC.

## Overview
Blogen lets users posts their thoughts on a variety of categories (such as Technology, Web Development, Health, etc..)
Additionally users can link an image (hosted on the web) to their post, which will display on the Blogen main page.
Once a user has made a post, other users can then reply to the post, starting a thread of conversation.

![Blogen Main Page](https://github.com/strohs/springboot-blogen/blob/master/BlogenMain.jpg)

## Using Blogen


### Blogen Users
The following users are provided out of the gate, including an admin user
* username: **johndoe** password: **password**
* username: **scotsman** password: **password**
* username: **mgill** password: **password**
* username: **lizreed** password: **password**
* username: **admin** password: **adminpassword**

## Software Used in the Project
* Spring Boot (1.5.9)
* Spring MVC
* Spring Data JPA
* Hibernate (as the JPA provider)
* Spring Security
* Thymeleaf (for the view layer)
* Thymeleaf Security Extras (to integrate with Spring Security)
* H2 embedded database
* JUnit4
* Mockito
* CircleCI to run integration tests
* Codecov.io to provide code coverage reports

## Running


