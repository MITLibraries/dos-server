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
        map.add("title", "Item Title");
        map.add("metadata_system", "dome");
        map.add("source_system", "archivesspace");
        map.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/176391/249794_cp.jpg?sequence=1");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        String body = this.restTemplate.postForObject("/object",  request,String.class);
        assertThat(body).isNotNull();
        DigitalObject body2 = this.restTemplate.getForObject("/object?oid="+body, DigitalObject.class);
        assertThat(body2.getHandle()).isEqualTo("hdl.net");
        assertThat(body2.getTitle()).isEqualTo("Item Title");
    }

    @Test
    public void testPost() {
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("handle", "hdl.net");
        map.add("title", "Item Title");
        map.add("metadata_system", "dome");
        map.add("source_system", "archivesspace");
        map.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/176391/249794_cp.jpg?sequence=1");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        String body = this.restTemplate.postForObject("/object",  request,String.class);
        assertThat(body).isNotNull();
        DigitalObject body2 = this.restTemplate.getForObject("/object?oid="+body, DigitalObject.class);
        assertThat(body2.getHandle()).isEqualTo("hdl.net");
        assertThat(body2.getTitle()).isEqualTo("Item Title");
    }


    @Test
    public void testPatch() {
        // first post the object:

        final MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("handle", "test.post.hdl.net");
        map.add("title", "Item Title");
        map.add("metadata_system", "dome");
        map.add("source_system", "archivesspace");
        map.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/176391/249794_cp.jpg?sequence=1");
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        final String oid = this.restTemplate.postForObject("/object",  request,String.class);
        assertThat(oid).isNotNull();


        // now update it:

        final MultiValueMap<String, String> updatedMap= new LinkedMultiValueMap<>();
        updatedMap.add("oid", oid); // the object id to update
        updatedMap.add("handle", "test.update.hdl.net");
        updatedMap.add("title", "Item Title Updated");
        updatedMap.add("metadata_system", "dome");
        updatedMap.add("source_system", "archivesspace");
        updatedMap.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/21882/112045_sv.jpg?sequence=2");
        final HttpEntity<MultiValueMap<String, String>> updateRequest = new HttpEntity<>(updatedMap, new HttpHeaders());
        final String body2 = this.restTemplate.patchForObject("/object",  updateRequest,String.class);
        assertThat(body2).isNotNull();
        final DigitalObject body3 = this.restTemplate.getForObject("/object?oid=" + oid, DigitalObject.class);
        assertThat(body3.getHandle()).isEqualTo("test.update.hdl.net");
        assertThat(body3.getTitle()).isEqualTo("Item Title Updated");
    }


    @Test
    public void testPatchUnsucessful() {
        // first post the object:

        final MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("handle", "test.post.hdl.net");
        map.add("title", "Item Title");
        map.add("metadata_system", "dome");
        map.add("source_system", "archivesspace");
        map.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/176391/249794_cp.jpg?sequence=1");
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        final String oid = this.restTemplate.postForObject("/object",  request,String.class);
        assertThat(oid).isNotNull();


        // now update it:

        final MultiValueMap<String, String> updatedMap= new LinkedMultiValueMap<>();
        updatedMap.add("oid", oid); // the object id to update
        updatedMap.add("handle", "test.update.hdl.net");
        updatedMap.add("title", "");
        updatedMap.add("metadata_system", "dome");
        updatedMap.add("source_system", "archivesspace");
        updatedMap.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/21882/112045_sv.jpg?sequence=2");
        final HttpEntity<MultiValueMap<String, String>> updateRequest = new HttpEntity<>(updatedMap, new HttpHeaders());
        final String body2 = this.restTemplate.patchForObject("/object",  updateRequest,String.class);
        assertThat(body2).isNotNull();
        final DigitalObject body3 = this.restTemplate.getForObject("/object?oid=" + oid, DigitalObject.class);
        assertThat(body3.getTitle()).isEqualTo("Item Title"); // the original title should remain as is since we didn't supply it
    }


}
