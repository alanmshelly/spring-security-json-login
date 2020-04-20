package com.springboot.restauth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(
            request: HttpServletRequest,
            response: HttpServletResponse
    ): Authentication {
        val principal = jacksonObjectMapper().readValue(request.inputStream, LoginCredentials::class.java)
        val authRequest = UsernamePasswordAuthenticationToken(
                principal.username,
                principal.password
        )
        setDetails(request, authRequest)
        return authenticationManager.authenticate(authRequest)
    }

    override fun successfulAuthentication(
            req: HttpServletRequest?,
            res: HttpServletResponse?,
            chain: FilterChain?,
            auth: Authentication?
    ) {
        SecurityContextHolder.getContext().authentication = auth
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        super.unsuccessfulAuthentication(request, response, failed)
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.outputStream.close()
    }
}

data class LoginCredentials(val username: String, val password: String)