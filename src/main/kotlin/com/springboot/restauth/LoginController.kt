package com.springboot.restauth

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
class LoginController {
    // Overwrite the login form created by spring security
    @GetMapping("/login")
    fun loginPage() {
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}
