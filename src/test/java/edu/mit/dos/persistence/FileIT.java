package edu.mit.dos.persistence;

import edu.mit.dos.model.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class FileIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FileJpaRepository fileJpaRepository;

    @Test
    public void testGetChecksum() {
        final File file = new File();
        //file.setOid(100);
        file.setChecksum("abcd1234");
        File f = entityManager.persist(file);
        entityManager.flush();

        File file1= fileJpaRepository.findByOid(f.getOid());
        assertThat(file.getChecksum().equalsIgnoreCase(file1.getChecksum()));
    }
}
