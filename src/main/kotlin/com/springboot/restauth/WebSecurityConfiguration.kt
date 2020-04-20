package com.springboot.restauth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf()
                .disable()

        http.authorizeRequests()
                /*
                 * You can either redirect for react page requests here or in the react router.
                 * If serving the react files from Spring, you need to allow everything in the react build.
                 */
                .antMatchers(
                        HttpMethod.POST,
                        "/api/login",
                        "/api/logout"
                )
                .permitAll()
                .antMatchers(
                        "/api/**"
                )
                .authenticated()

        http.exceptionHandling()
                .authenticationEntryPoint { request, response, _ ->
                    if (request.requestURI.startsWith("/api")) {
                        // returns Forbidden for all unauthorised API requests
                        response.status = HttpStatus.FORBIDDEN.value()
                    } else {
                        // redirects to top page for other unauthorised requests
                        response.sendRedirect("/")
                    }
                }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
