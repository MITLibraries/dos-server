package edu.mit.dos.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * Responsible for generating and returning persistent identifiers (handles)
 */
@Controller
public class HandleService {

    private static final Logger logger = LoggerFactory.getLogger(HandleService.class);

    public static final String HANDLE_PREFIX = "https://hdl.net/1721.3/";

    @RequestMapping(value ="/handle", method = RequestMethod.GET)
    public @ResponseBody String getHandle(@RequestParam String objectId)  {
        return HANDLE_PREFIX.concat(objectId); // TODO look up and return the actual handle
    }
}