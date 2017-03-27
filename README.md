## SpacecraftREST

[![Build Status](https://travis-ci.org/daklassen/spacecraftREST.svg?branch=master)](https://travis-ci.org/daklassen/spacecraftREST)

Project for learning to create a REST service with Spring.

### Quickstart Intellij

Clone this repo:
```
git clone https://github.com/daklassen/spacecraftREST
```
Change the directory:
```
cd spacecraftUI
```
Build the Intellij project with gradle:
```
gradlew cleanidea idea
```
Now you can open the generated Intellij project and start working on the projekt.

### Security 
 
This Application is secured by Spring Security with OAuth2. You can use this curl command to get an oauth token. 
 
```shell
curl -X POST -vu client-spacecrafts:123456 http://localhost:8080/oauth/token -H "Accept: application/json" -d "password=password&username=username&grant_type=password&scope=write&client_secret=123456&client_id=client-spacecrafts" 
```

And use this token in the request like:

```shell
curl -v POST http://127.0.0.1:8080/spacecrafts -H "Authorization: Bearer <oauth_token>
```

Preconfigured settings: 
 
|username|password|grant_type|client_secret|client_id         | 
|--------|--------|----------|-------------|------------------| 
|username|password|password  |123456       |client-spacecrafts| 
 

### REST documentation

You can reach the documentation (generated with swagger) with: 
> http://localhost:8080/swagger-ui.html