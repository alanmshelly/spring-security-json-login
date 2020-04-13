package com.springboot.restauth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpHeaders.SET_COOKIE
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringbootApplicationTests {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `login with correct credentials returns 200 and sets session cookie`() {
        val responseEntity = restTemplate.exchange("/api/login", POST, correctLoginCredentialsEntity(), Unit.javaClass)


        assertThat(responseEntity.statusCode).isEqualTo(OK)
        assertThat(responseEntity.headers.getFirst(SET_COOKIE))
                .containsPattern("^JSESSIONID=\\w+;")
    }

    @Test
    fun `login with wrong credentials returns 401 and doesn't set session cookie`() {
        val responseEntity = restTemplate.exchange("/api/login", POST, incorrectLoginCredentialsEntity(), Unit.javaClass)


        assertThat(responseEntity.statusCode).isEqualTo(UNAUTHORIZED)
        assertThat(responseEntity.headers.getFirst(SET_COOKIE)).isNull()
    }

    @Test
    fun `non-api page always returns OK and body`() {
        val responseEntity = restTemplate.getForEntity("/", String::class.java)

        assertThat(responseEntity.statusCode).isEqualTo(OK)
        assertThat(responseEntity.body).contains("Hello Page!")
    }

    @Test
    fun `api endpoint returns OK and body when logged in`() {
        val loginResponseEntity = restTemplate.exchange("/api/login", POST, correctLoginCredentialsEntity(), Unit.javaClass)
        val headers = HttpHeaders()
        headers.add("Cookie", loginResponseEntity.headers.getFirst(SET_COOKIE))


        val responseEntity = restTemplate.exchange("/api/hello", GET, HttpEntity(null, headers), String::class.java)


        assertThat(responseEntity.statusCode).isEqualTo(OK)
        assertThat(responseEntity.body).isEqualTo("Hello API!")
    }

    @Test
    fun `api endpoint returns FORBIDDEN when not logged in`() {
        val responseEntity = restTemplate.getForEntity("/api/hello", String::class.java)

        assertThat(responseEntity.statusCode).isEqualTo(FORBIDDEN)
        assertThat(responseEntity.body).isNullOrEmpty()
    }

    private fun correctLoginCredentialsEntity(): HttpEntity<LinkedMultiValueMap<String, String>> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val credentials = LinkedMultiValueMap<String, String>()
        credentials.set("username", "hoge")
        credentials.set("password", "foobar")

        return HttpEntity(credentials, headers)
    }

    private fun incorrectLoginCredentialsEntity(): HttpEntity<LinkedMultiValueMap<String, String>> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val credentials = LinkedMultiValueMap<String, String>()
        credentials.set("username", "hoge")
        credentials.set("password", "wrongpassword")

        return HttpEntity(credentials, headers)
    }
}
