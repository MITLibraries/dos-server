package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.model.DigitalObjectBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan("edu.mit.dos.persistence")
public class DigitalObjectIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DigitalObjectJpaRepository digitalObjectJpaRepository;

    @Test
    public void testById() {
        DigitalObject digitalObject = new DigitalObjectBuilder()
                .setHandle("https://hdl.net")
                .createDigitalObject();
        entityManager.persist(digitalObject);
        entityManager.flush();
        DigitalObject digitalObject1= digitalObjectJpaRepository.findByHandle("https://hdl.net");
        assertThat(digitalObject.getHandle().equalsIgnoreCase(digitalObject1.getHandle()));
    }
}
