package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.model.DigitalObjectBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ComponentScan("edu.mit.dos.persistence")
public class ObjectFilePersistenceIT {

    @Autowired
    private ObjectFilePersistence persistence;

    // Tests that the associated file entity is saved with the digital object
    @Test
    public void testObjectAndFileSave() {
        final DigitalObject digitalObject = new DigitalObjectBuilder(new Date(), new Date(), "", "").createDigitalObject();
        digitalObject.setTitle("test");

        final List<String> paths = new ArrayList<>();
        paths.add("/tmp/test");

        final DigitalObject digitalObject1 = persistence.save(paths, digitalObject);
        assertThat(digitalObject1.getFiles().get(0).getPath().equalsIgnoreCase("/tmp/test"));
        assertThat(digitalObject1.getTitle().equalsIgnoreCase("Test"));
    }
}
