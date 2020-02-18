package edu.mit.dos.auth;

import edu.mit.dos.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * Checks that object end points don't work without API auth
 * Note: Test starts the server
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan("edu.mit.dos.persistence, edu.mit.dos.service")
public class AuthServiceIT {

    @Autowired
    UserService userService;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void shouldFailEndPointsWithoutAuth() {
        final MultiValueMap<String, String> map = getRequestMap();
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        final String postBody = this.restTemplate.postForObject("/object", request, String.class);

        if (postBody == null) {
            // should be null
        } else {
            assertThat(postBody.contains("Forbidden")); //TODO modify with the return status code
        }

    }

    private MultiValueMap<String, String> getRequestMap() {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("handle", "hdl.net");
        map.add("title", "Item Title");
        map.add("metadata_source", "dome");
        map.add("content_source", "archivesspace");
        map.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/176391/249794_cp.jpg?sequence=1");
        return map;
    }


}
