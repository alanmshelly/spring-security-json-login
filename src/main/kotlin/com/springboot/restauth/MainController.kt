package com.springboot.restauth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping("/")
    fun index(): String {
        return """
            <html><body>
                <div>Hello Page!</div>
                <form action="/api/login" method="post">
                    <div><input name="username" value="hoge"></div>
                    <div><input name="password" value="foobar"></div>
                    <div><input type="submit"></div>
                 </form>
                <a href="/api/logout">logout</a>

                 <div><a href="/api/hello">api endpoint</a></div>
             </body></html>
        """.trimIndent()
    }

    @GetMapping("/api/hello")
    fun hello(): String {
        return "Hello API!"
    }
}