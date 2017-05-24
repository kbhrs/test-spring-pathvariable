package com.github.kbhrs.urldecode;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class SampleController {
    private static final String UTF8 = StandardCharsets.UTF_8.toString();

    @GetMapping(value = "echo/{pathVariable}")
    public String raw(@PathVariable String pathVariable) {
        return pathVariable;
    }

    @GetMapping(value = "decode/{pathVariable}")
    public String dencode(@PathVariable String pathVariable) throws UnsupportedEncodingException {
        return URLDecoder.decode(pathVariable, UTF8);
    }
}
