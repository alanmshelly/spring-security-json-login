# spring-security-json-login
Two implementations of how to login by POSTing a JSON body.

```bash
curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"username":"hoge","password":"foobar"}' localhost:8080/api/login
```

## Using a controller
https://github.com/alanmshelly/spring-security-json-login/tree/master

## Using authenticationFilter
https://github.com/alanmshelly/spring-security-json-login/tree/using-filter
