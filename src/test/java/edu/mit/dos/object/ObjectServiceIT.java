package edu.mit.dos.object;

import edu.mit.dos.model.DigitalObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Java6Assertions.assertThat;


/**
 * Note: Test starts the server
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ObjectServiceIT {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void testGet() {
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("handle", "hdl.net");
        map.add("metadata_system", "dome");
        map.add("source_system", "archivesspace");
        map.add("uris", "https://dome.mit.edu/bitstream/handle/1721.3/176391/249794_cp.jpg?sequence=1");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        String body = this.restTemplate.postForObject("/object",  request,String.class);
        assertThat(body).isNotNull();
        DigitalObject body2 = this.restTemplate.getForObject("/object?oid="+body, DigitalObject.class);
        assertThat(body2.getHandle()).isEqualTo("hdl.net");
    }

    @Test
    public void testPost() {
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("handle", "hdl.net");
        map.add("metadata_system", "dome");
        map.add("source_system", "archivesspace");
        map.add("uris", "https://dome.mit.edu/bitstream/handle/1721.3/176391/249794_cp.jpg?sequence=1");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        String body = this.restTemplate.postForObject("/object",  request,String.class);
        assertThat(body).isNotNull();
        DigitalObject body2 = this.restTemplate.getForObject("/object?oid="+body, DigitalObject.class);
        assertThat(body2.getHandle()).isEqualTo("hdl.net");
    }


}
