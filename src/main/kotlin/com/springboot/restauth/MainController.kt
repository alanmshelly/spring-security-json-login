package com.springboot.restauth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.security.Principal


@RestController
class MainController {
    @GetMapping("/")
    fun index(principal: Principal?): ModelAndView {
        val mav = ModelAndView("top")
        mav.addObject("principal", principal?.name)
        return mav
    }

    @GetMapping("/api/hello")
    fun hello(): String {
        return "Hello API!"
    }
}