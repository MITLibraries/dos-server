package edu.mit.dos.handle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for Handle Service
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HandleServiceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void testGET() throws Exception {
        final String objectId = "939299";
        assertThat(this.restTemplate.getForObject(getPrefix() + "?objectId={oid}",
                String.class, objectId)).matches(HandleService.HANDLE_PREFIX + objectId);
    }

    private String getPrefix() {
        return "http://localhost:" + port + "/handle";
    }

    @Before
    public void setup() throws Exception {
    }

}
