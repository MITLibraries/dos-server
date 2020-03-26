package edu.mit.dos.persistence;

import edu.mit.dos.ServiceConfig;
import edu.mit.dos.model.DigitalFile;
import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.object.IdentifierFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ComponentScan({"edu.mit.dos, edu.mit.dos.object"})
@Service
public class ObjectFilePersistenceImpl implements ObjectFilePersistence {

    private static final Logger logger = LoggerFactory.getLogger(ObjectFilePersistenceImpl.class);

    @Autowired
    private DigitalObjectJpaRepository objectJpaRepository;

    @Autowired
    private FileJpaRepository fileJpaRepository;

    private IdentifierFactory identifierFactory;

    private ServiceConfig serviceConfig;

    @Transactional
    public DigitalObject save(final List<String> storagePaths, final DigitalObject object) {

        for (final String path : storagePaths) {
            final DigitalFile digitalFile = new DigitalFile();
            digitalFile.setPath(path);
            digitalFile.setOid(object.getOid());
            digitalFile.setHandle(getHandle(path));
            fileJpaRepository.save(digitalFile);
            object.getFiles().add(digitalFile);
        }

        final DigitalObject savedObject = objectJpaRepository.save(object);
        logger.debug("Saved:{}", savedObject);
        return savedObject;
    }

    public String getHandle(final String target) {
        final CloseableHttpClient client = HttpClients.createDefault();
        final HttpPut httpPut = new HttpPut(serviceConfig.getHandleServer());

        try {
            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            final String key = identifierFactory.getInstance().generate(); // + File.separator + item;

            params.add(new BasicNameValuePair("handle", key));
            httpPut.setEntity(new UrlEncodedFormEntity(params));

            final CloseableHttpResponse response = client.execute(httpPut);

            if (response.getStatusLine().getStatusCode() == 200) {

            } else {
                logger.error("Error obtaining handle");
                return target;
            }

        } catch (IOException e) {
            logger.error("Error obtaining handle:", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("Error", e);
            }
        }
        return target;
    }

    @Autowired
    public void setIdentifierFactory(IdentifierFactory identifierFactory) {
        this.identifierFactory = identifierFactory;
    }

    @Autowired
    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}
