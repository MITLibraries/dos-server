package edu.mit.dos.object;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ObjectServiceTest {

    @InjectMocks
    private ObjectService controller;

    @Mock
    private View view;

    private MockMvc mockMvc;

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
        // Clarify what we are testing
        mockMvc.perform(get("/object").param("objectId", "123877664"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testPost() throws Exception {
      // Clarify what we are testing
        mockMvc.perform(post("/object")// how to pass file object .param("objectId", "123877664"))
                // .andExpect(status().isOk())
                // .andReturn();
    }

}
