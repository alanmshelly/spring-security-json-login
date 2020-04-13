package com.springboot.restauth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping("/")
    fun index(): String {
        return """
            <html><body>
                <form action="/api/login" method="post">
                    <div><input name="username" value="hoge"></div>
                    <div><input name="password" value="foobar"></div>
                    <div><input type="submit"></div>
                 </form>
                 
                 
                 <div><a href="/authenticated">authenticated page</a></div>
                 <div><a href="/api/hello">api endpoint</a></div>
             </body></html>
        """.trimIndent()
    }

    @GetMapping("/authenticated")
    fun authenticated(): String {
        return """
            <html><body>
                <div>Hello HTML!</div>
                <a href="/logout">logout</a>
            </body></html>
        """.trimIndent()
    }

    @GetMapping("/api/hello")
    fun hello(): String {
        return "Hello API!"
    }
}