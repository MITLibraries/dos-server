package edu.mit.dos;

import edu.mit.dos.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private UserService userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);


    @Autowired
    public DataLoader(UserService userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        logger.debug("Okay here");

    }
}