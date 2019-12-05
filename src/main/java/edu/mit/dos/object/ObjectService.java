package edu.mit.dos.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.mit.dos.model.DigitalObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ObjectService {

    private static final Logger logger = LoggerFactory.getLogger(ObjectService.class);

    @RequestMapping(value ="/object", method = RequestMethod.POST)
    public void setObject(@RequestParam("object") DigitalObject object)  {

    }

    @RequestMapping(value ="/object", method = RequestMethod.GET)
    public @ResponseBody DigitalObject getObject(@RequestParam("object") DigitalObject object) {
        return object;
    }


}
