package edu.mit.dos.handle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for handle service. Could be used for injecting mocks.
 * See HandleServiceIT for integration test (actually starts a web container).
 */
public class HandleServiceTest {

    @InjectMocks
    private HandleService controller;

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
        mockMvc.perform(get("/handle").param("objectId", "123877664"))
                .andExpect(status().isOk())
                .andReturn();
    }

}