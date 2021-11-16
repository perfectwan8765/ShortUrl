package com.jsw.app;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ShortUrlApplicationTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));
    }

    @Test
    void jasypt() {
        String url = "jdbc:postgresql://localhost:5432/shorturl?currentSchema=shorturl";
        String username = "shorturl";

        System.out.format("url: %s", jasyptEncoding(url));
        System.out.println();
        System.out.format("username: %s", jasyptEncoding(username));
    }
    
    public String jasyptEncoding (String message) {
        String key = "shorturl";
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();

        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);

        return pbeEnc.encrypt(message);
    }

}