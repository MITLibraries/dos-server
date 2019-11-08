package edu.mit.dos.service;

import edu.mit.dos.model.DigitalObject;


public interface DigitalObjectJpaService {

    DigitalObject findById(int departmentid);

    DigitalObject findByHandle(String handle);
}