package edu.mit.dos.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

@TestPropertySource("classpath:application.properties")
@RunWith(SpringJUnit4ClassRunner.class)
public class FileConverterTest {

    @Value("${dome.target.link}")
    public String targetLink;

    @Value("${dome.target.link.size}")
    public String targetLinkSize;

    // TODO: Remove when file copying capability from Dome is no longer needed.
    @Test
    public void testCreate() throws Exception {
        final File f;
        try {
            f = FileConverter.toFile(targetLink);

            if (f == null) {
                fail("File was not copied");
            }


            assertNotNull(f.getPath());
            assertTrue(f.length() == Long.valueOf(targetLinkSize));

        } catch (IOException e) {

        }


    }
}
