package edu.mit.dos.service;

import edu.mit.dos.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    String signup(User user);

    String signin(String username, String password);

    void delete(String username);

    User search(String username);

    User whoami(HttpServletRequest req);

    String refresh(String remoteUser);
}
