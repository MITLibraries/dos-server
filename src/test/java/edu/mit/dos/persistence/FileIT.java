package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalFile;
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
public class FileIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileJpaRepository fileJpaRepository;

    @Test
    public void testGetChecksum() {
        final DigitalFile file = new DigitalFile();
        //file.setOid(100);
        file.setChecksum("abcd1234");
        DigitalFile f = entityManager.persist(file);
        entityManager.flush();

        DigitalFile file1= fileJpaRepository.findByFid(f.getFid());
        assertThat(file.getChecksum().equalsIgnoreCase(file1.getChecksum()));
    }
}
