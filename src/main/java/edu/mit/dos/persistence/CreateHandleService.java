package edu.mit.dos.persistence;

import net.handle.hdllib.AdminRecord;
import net.handle.hdllib.CreateHandleRequest;
import net.handle.hdllib.HandleException;
import net.handle.hdllib.HandleValue;

import java.io.UnsupportedEncodingException;

public class CreateHandleService extends BaseHandleService {

    private CreateHandleRequest request = null;

    private AdminRecord admin = null;

    private AdminRecord user = null;

    private AdminRecord group = null;

    private HandleValue adminValue = null;

    private HandleValue userValue = null;

    private HandleValue groupValue = null;

    private String createHandle(String handle, String location, String group) throws UnsupportedEncodingException, HandleException {

        CreateHandleRequest request = null;

        HandleValue[] values = {adminValue, groupValue, userValue, new HandleValue(1, getHandleType().getBytes(getStringEncoding()), location.getBytes(getStringEncoding()))};
        request.authInfo = getAuth();
        request.handle = handle.getBytes(getStringEncoding());
        request.values = values;
        request.clearBuffers();
        setResponse(getResolver().processRequest(request));

        if(this.getResponse().responseCode >1) {
            return this.getResponse().getResponseCodeMessage(this.getResponse().responseCode);
        } else {
            return handle;
        }
    }

    @Override
    protected void requestSetup() throws Exception {

    }
}
