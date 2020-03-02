package edu.mit.dos.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InfoService {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void shouldNotRequireAuth(){
        final String getBody = this.restTemplate.getForObject("/", String.class);
        assert (getBody.equalsIgnoreCase("DOS"));
    }
}
