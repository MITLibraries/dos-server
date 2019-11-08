package edu.mit.dos.service;

import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.persistence.DigitalObjectJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DigitalObjectJpaServiceImpl implements DigitalObjectJpaService {

    @Resource
    private DigitalObjectJpaRepository repo;

    @Override
    public DigitalObject findById(final int departmentid) {
        return repo.findByOid(departmentid);
    }

    @Override
    public DigitalObject findByHandle(final String handle) {
        return repo.findByHandle(handle);
    }
}
