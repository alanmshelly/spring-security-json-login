package com.springboot.restauth

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
class WebSecurityConfig(
        private val authenticationProvider: AuthenticationProvider
) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        val filter = CustomAuthenticationFilter()
        filter.setRequiresAuthenticationRequestMatcher(
                AntPathRequestMatcher("/api/login", "POST"))
        filter.setAuthenticationManager(authenticationManagerBean())
        http.addFilter(filter)

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler { _, response, _ ->
                    response.status = HttpStatus.OK.value()
                }
                .permitAll()

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
        auth.authenticationProvider(authenticationProvider)
    }
}
