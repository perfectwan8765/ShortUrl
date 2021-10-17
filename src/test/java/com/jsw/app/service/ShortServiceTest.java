package com.jsw.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

@RestClientTest(ShortService.class)
public class ShortServiceTest {
    
//    @Autowired
//    private RestTemplate template;

    private MockRestServiceServer server;
    
    @MockBean
    private ShortService shortService;
    
    @BeforeEach
    public void setUp() {
        this.server = MockRestServiceServer.createServer(new RestTemplate());
    }
    
    @Test
    public void serviceEncodeCheck() throws Exception {
        this.server.expect(MockRestRequestMatchers.anything())
                   .andRespond(MockRestResponseCreators.withSuccess("abc", MediaType.APPLICATION_JSON));
        
        when(shortService.encodeUrl("abcd")).thenReturn("abc");
        //When
        String encodeUrl = shortService.encodeUrl("abcd");
        //Then
//        assertNotNull(encodeUrl);
        assertEquals(encodeUrl, "e");
    }

}