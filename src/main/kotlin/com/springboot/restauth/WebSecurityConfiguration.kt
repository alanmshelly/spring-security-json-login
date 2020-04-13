package com.springboot.restauth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
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
                        "/api/**"
                )
                .authenticated()

        http.formLogin()
                .successHandler { _, response, _ ->
                    response.status = HttpStatus.OK.value()
                }
                .failureHandler { _, response, _ ->
                    response.status = HttpStatus.UNAUTHORIZED.value()
                }
                .loginProcessingUrl("/api/login")
                .permitAll()

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler { _, response, _ ->
                    response.status = HttpStatus.OK.value()
                }

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

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
                .withUser("hoge")
                .password(passwordEncoder().encode("foobar"))
                .roles("USER")
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
