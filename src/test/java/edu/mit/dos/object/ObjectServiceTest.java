package edu.mit.dos.object;

import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.persistence.DigitalObjectJpaRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mock tests. Does not start the server.
 */
public class ObjectServiceTest {

    @InjectMocks
    private ObjectService controller;

    @Mock
    private View view;

    private MockMvc mockMvc;

    @Mock
    private DigitalObjectJpaRepository objectJpaRepository;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setSingleView(view)
                .build();
    }

    @Test
    public void testGet() throws Exception {
        DigitalObject digitalObject = new DigitalObject();
        digitalObject.setOid(1222);
        when(objectJpaRepository.findByOid(1222)).thenReturn(digitalObject);
        mockMvc.perform(get("/object").param("oid", "1222"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'oid': 1222}"))
                .andReturn();
    }


}
