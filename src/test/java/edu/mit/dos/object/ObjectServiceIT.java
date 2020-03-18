package edu.mit.dos.object;

import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.model.Role;
import edu.mit.dos.model.User;
import edu.mit.dos.service.UserService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.*;


/**
 * Note: Test starts the server
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan("edu.mit.dos.persistence, edu.mit.dos.service")
public class ObjectServiceIT {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private static final String USER_ADMIN = "admin1";
    private static final String USER_PASSWORD = "admin1";
    private static final String USER_EMAIL = "admin1@email.com";
    private static final String CLIENT_USERNAME = "client1";
    private static final String PASSWORD = "client1";
    private static final String CLIENT_EMAIL_COM = "client1@email.com";

    @Autowired
    UserService userService;

    @Autowired
    TestRestTemplate restTemplate;

    /**
     * Sets up authentication, tokens, etc.
     */
    @Before
    public void setupAuth() {

        if (userService == null) {
            fail("Autowired user service null.");
        }

        if (userService.search(USER_ADMIN) != null) {
            return; // user already exists; do not recreate the user
        }

        final User admin = new User();
        admin.setUsername(USER_ADMIN);
        admin.setPassword(USER_PASSWORD);
        admin.setEmail(USER_EMAIL);
        admin.setRole(Role.ROLE_ADMIN);
        userService.signup(admin);
        final User client = new User();
        client.setUsername(CLIENT_USERNAME);
        client.setPassword(PASSWORD);
        client.setEmail(CLIENT_EMAIL_COM);
        client.setRole(Role.ROLE_CLIENT);
        userService.signup(client);

        // Get the token:

        // Pattern: curl -X POST 'http://localhost:8080/users/signin?username=admin&password=admin'

        final MultiValueMap<String, String> map1 = new LinkedMultiValueMap<>();
        map1.add("username", USER_ADMIN);
        map1.add("password", USER_PASSWORD);
        final HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<>(map1, new HttpHeaders());
        final String auth = this.restTemplate.postForObject("/users/signin", request1, String.class);
        final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization: Bearer ", auth));
        this.restTemplate.getRestTemplate().setInterceptors(interceptors);

    }

    @Test
    public void testGet() {
        // first post the object
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        final MultiValueMap<String, Object> map = getRequestParameters2();
        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        final String body = this.restTemplate.postForObject("/object", request, String.class);
        //logger.debug("Test Results:" + body);
        assertThat(body).isNotNull();
        JSONObject object = (JSONObject) JSONValue.parse(body);
        String oid = object.getAsString("oid");
        final DigitalObject body2 = this.restTemplate.getForObject("/object?oid=" + oid, DigitalObject.class);
        assertThat(body2.getHandle()).isEqualTo("hdl.net");
        assertThat(body2.getTitle()).isEqualTo("Item Title");
    }

    @Test
    public void testPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final MultiValueMap<String, Object> map = getRequestParameters2();
        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        final String body = this.restTemplate.postForObject("/object", request, String.class);
        logger.debug("body" + body);

        final JSONObject object = (JSONObject) JSONValue.parse(body);
        final String oid = object.getAsString("oid");
        assertThat(body).isNotNull(); // TODO change this when we are returning more info from endpoint
        final DigitalObject body2 = this.restTemplate.getForObject("/object?oid=" + oid, DigitalObject.class);
        assertThat(body2.getHandle()).isEqualTo("hdl.net");
        assertThat(body2.getTitle()).isEqualTo("Item Title");
    }

    @Test
    public void testDelete() {
        final MultiValueMap<String, String> map = getRequestParameters();
        final HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, new HttpHeaders());
        String body = this.restTemplate.postForObject("/object", request, String.class);
        assertThat(body).isNotNull();
        JSONObject object = (JSONObject) JSONValue.parse(body);
        String oid = object.getAsString("oid");
        this.restTemplate.delete("/object?oid=" + oid, DigitalObject.class);
        final DigitalObject body2 = this.restTemplate.getForObject("/object?oid=" + oid, DigitalObject.class);
        assertThat(body2.getOid()).isEqualTo(0);
    }

    @Test
    public void testPatch() {
        // first post the object:

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final MultiValueMap<String, Object> map = getRequestParameters2();
        final HttpEntity    <MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        final String body = this.restTemplate.postForObject("/object", request, String.class);
        JSONObject object = (JSONObject) JSONValue.parse(body);
        String oid = object.getAsString("oid");
        assertThat(oid).isNotNull();

        // now update it:

        final MultiValueMap<String, Object> updatedMap = new LinkedMultiValueMap<>();
        updatedMap.add("oid", oid); // the object id to update
        updatedMap.add("handle", "test.update.hdl.net");
        updatedMap.add("title", "Item Title Updated");
        updatedMap.add("metadata_source", "dome");
        updatedMap.add("content_source", "archivesspace");
        try {
            updatedMap.add("file", getTestFile());
        } catch (IOException e) {
            logger.debug("Error:{}", e);
        }
        final HttpEntity<MultiValueMap<String, Object>> updateRequest = new HttpEntity<>(updatedMap, new HttpHeaders());
        final String body2 = this.restTemplate.patchForObject("/object", updateRequest, String.class);
        assertThat(body2).isNotNull();
        final DigitalObject body3 = this.restTemplate.getForObject("/object?oid=" + oid, DigitalObject.class);
        assertThat(body3.getHandle()).isEqualTo("test.update.hdl.net");
        assertThat(body3.getTitle()).isEqualTo("Item Title Updated");
    }

    @Test
    public void testPatchUnsucessful() {
        // first post the object:

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        final MultiValueMap<String, Object> map = getRequestParameters2();
        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        final String body = this.restTemplate.postForObject("/object", request, String.class);
        JSONObject object = (JSONObject) JSONValue.parse(body);
        String oid = object.getAsString("oid");
        assertThat(oid).isNotNull();


        // now update it:

        final MultiValueMap<String, String> updatedMap = new LinkedMultiValueMap<>();
        updatedMap.add("oid", oid); // the object id to update
        updatedMap.add("handle", "test.update.hdl.net");
        updatedMap.add("title", "");
        updatedMap.add("metadata_source", "dome");
        updatedMap.add("content_source", "archivesspace");
        updatedMap.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/21882/112045_sv.jpg?sequence=2");
        final HttpEntity<MultiValueMap<String, String>> updateRequest = new HttpEntity<>(updatedMap, new HttpHeaders());
        final String body2 = this.restTemplate.patchForObject("/object", updateRequest, String.class);
        assertThat(body2).isNotNull();
        final DigitalObject body3 = this.restTemplate.getForObject("/object?oid=" + oid, DigitalObject.class);
        assertThat(body3.getTitle()).isEqualTo("Item Title"); // the original title should remain as is since we didn't supply it
    }

    @Test
    public void testGetFile() throws IOException {

        // first post the object:

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        final MultiValueMap<String, Object> map = getRequestParameters2();
        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        final String body = this.restTemplate.postForObject("/object", request, String.class);
        JSONObject object = (JSONObject) JSONValue.parse(body);
        List<Integer> files = (List<Integer>) object.get("files");
        assertThat(files).isNotNull();
        Integer fid = files.get(0);
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF));

        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<byte[]> response = restTemplate.exchange(
                "/file?fid=" + fid,
                HttpMethod.GET, entity, byte[].class, "1");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is2xxSuccessful());

        // logger.debug("Response:" + response);

        if (response.getBody() != null) { // TODO remove this when the logic in LocalFileSystemImpl is added
            assertThat(response.getHeaders().get("Content-Type").get(0).equals("application/pdf")); // TODO change and tie it to the file when we externalize
            assertThat(response.getHeaders().get("Content-Length").get(0).equals("1021167")); // TODO change and tie it to the file when we externalize
        } else {
            fail("Response body null");
        }
    }

    private MultiValueMap<String, String> getRequestParameters() {
        final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("handle", "hdl.net");
        map.add("title", "Item Title");
        map.add("metadata_source", "dome");
        map.add("content_source", "archivesspace");
        map.add("target_links", "https://dome.mit.edu/bitstream/handle/1721.3/46021/MC665_r14_6M-4371.pdf?sequence=1");
        return map;
    }

    private MultiValueMap<String, Object> getRequestParameters2() {
        final MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("handle", "hdl.net");
        map.add("title", "Item Title");
        map.add("metadata_source", "dome");
        map.add("content_source", "archivesspace");
        try {
            map.add("file", getTestFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static Resource getTestFile() throws IOException {
        Path testFile = Files.createTempFile("test-file", ".txt");
        logger.debug("Creating and Uploading Test File: " + testFile);
        Files.write(testFile, "Hello World !!, This is a test file.".getBytes());
        return new FileSystemResource(testFile.toFile());
    }

}
