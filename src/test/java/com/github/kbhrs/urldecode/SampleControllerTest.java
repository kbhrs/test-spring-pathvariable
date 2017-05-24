package com.github.kbhrs.urldecode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleControllerTest {
    @LocalServerPort
    private int port;

    final RestTemplate template = new RestTemplate();

    private static final String UTF8 = StandardCharsets.UTF_8.toString();
    private static final String BUSINESS_VALUE = "aa/bb";

    /**
     * このテストはエラー(HttpClientErrorException 404)になる。
     * 仕方がないと思っている
     */
    @Test
    public void testEcho() throws UnsupportedEncodingException {
        String url = "http://localhost:" + port + "/echo/" + BUSINESS_VALUE;
        String message = template.getForObject(url, String.class);
        assertThat(message, is(BUSINESS_VALUE));
    }

    /**
     * このテストもエラー(AssertionError: Expected: is "aa/bb" but: was "aa%2Fbb")になる。
     * できればこの実装がエラーにならないようにしたい
     * （各コントローラメソッドでdecodeしたくないから）
     */
    @Test
    public void testEncode() throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(BUSINESS_VALUE, UTF8);
        String url = "http://localhost:" + port + "/echo/" + encoded;
        String message = template.getForObject(url, String.class);
        assertThat(message, is(BUSINESS_VALUE));
    }

    /**
     * このテストは通る。
     */
    @Test
    public void testEncodeDecode() throws UnsupportedEncodingException {
        String encoded = URLEncoder.encode(BUSINESS_VALUE, UTF8);
        String decodeUrl = "http://localhost:" + port + "/decode/" + encoded;
        String decoded = template.getForObject(decodeUrl, String.class);
        System.out.println("URL:" + decodeUrl);
        System.out.println("Response:" + decoded);
        assertThat(decoded, is(BUSINESS_VALUE));
    }
}