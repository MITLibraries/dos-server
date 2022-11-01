package edu.mit.dos.persistence;

import net.handle.hdllib.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.security.PrivateKey;

public abstract class BaseHandleService {

	private static final Logger logger = LoggerFactory.getLogger(BaseHandleService.class);

	private String handleType = "URL";

    private String stringEncoding = "UTF8";

    private String handle_prefix = "";

    private String auth_handle = "";

    private String user_handle = "";

    private String group_handle = "";

    private String auth_index = "";

    private String privateKeyLocation = "";

    private String secretKey = "";

    private AuthenticationInfo auth = null;

    private HandleResolver resolver = null;

    private int timestamp = (int) (System.currentTimeMillis() / 1000);

    private AbstractResponse response = null;

    public BaseHandleService() {
        this.configure();
    }

    protected void configure() {

        try {
            this.handle_prefix = "handle_prefix";
            this.auth_handle = "0.NA/" + this.handle_prefix;
            this.auth_index = "auth_index"; //TODO check
            this.privateKeyLocation = System.getenv("privateKey"); //read from AWS
            this.secretKey = System.getenv("secretKey"); //read from AWS
            auth = getAuthenticationInfo();
            ClientSessionTracker sessionTracker = new ClientSessionTracker();
            sessionTracker.setSessionSetupInfo(new SessionSetupInfo(auth));
            resolver = new HandleResolver();
            resolver.traceMessages = true;
            resolver.setSessionTracker(sessionTracker);

        } catch (Exception e) {
			logger.error("Error", e);
        }
    }

    protected boolean checkauth(String handle) throws Exception {

        try {
            handle = normalizeHandle(handle);
            String[] types = {getHandleType()};
            int[] indexes = new int[0];
            HandleValue[] handles = getResolver().resolveHandle(handle, types, indexes);
            StringBuilder handleString = new StringBuilder();
            for (int x = 0; x < handles.length; x++) {
                handleString.append(handles[x].getDataAsString());
                if (x < handles.length - 1) {
                    handleString.append(",");
                }
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected PublicKeyAuthenticationInfo getAuthenticationInfo() throws Exception {
        byte[] key = getPrivateKey(this.privateKeyLocation);
        PrivateKey privkey = decryptPrivateKey(this.privateKeyLocation, this.secretKey, key);
        PublicKeyAuthenticationInfo auth = new PublicKeyAuthenticationInfo(this.auth_handle.getBytes("UTF8"),
                Integer.valueOf(this.auth_index).intValue(),
                privkey);
        return auth;
    }


    private PrivateKey decryptPrivateKey(String privateKeyLocation, String secretKey, byte[] key) throws Exception {
        PrivateKey privkey = null;
        byte secKey[] = null;
        try {
            if (Util.requiresSecretKey(key)) {
                secKey = secretKey.getBytes(stringEncoding);
            }
            key = Util.decrypt(key, secKey);
            privkey = Util.getPrivateKeyFromBytes(key, 0);
        } catch (Throwable t) {
            throw new Exception("Error" + privateKeyLocation + ": " + t);
        }
        return privkey;
    }

    private byte[] getPrivateKey(String privateKeyLocation) throws Exception {
        byte[] key = null;
        try {
            File f = new File(privateKeyLocation);
            FileInputStream fs = new FileInputStream(f);
            key = new byte[(int) f.length()];
            int n = 0;
            while (n < key.length) key[n++] = (byte) fs.read();
            fs.read(key);
        } catch (Throwable t) {
            throw new Exception("Error reading key" + privateKeyLocation + ": " + t);
        }
        return key;
    }


    protected String normalizeHandle(String handle) {
        if (!handle.startsWith(getHandle_prefix() + "/")) {
            handle = getHandle_prefix() + "/" + handle;
        }
        return handle;
    }

    public AuthenticationInfo getAuth() {
        return auth;
    }

    public String getUser_handle() {
        return user_handle;
    }

    public void setUser_handle(String user_handle) {
        this.user_handle = user_handle;
    }

    public String getGroup_handle() {
        return group_handle;
    }

    public void setGroup_handle(String group_handle) {
        this.group_handle = group_handle;
    }

    protected abstract void requestSetup() throws Exception;


    public String getAuth_handle() {
        return auth_handle;
    }

    public String getAuth_index() {
        return auth_index;
    }

    public String getHandle_prefix() {
        return handle_prefix;
    }

    public String getHandleType() {
        return handleType;
    }

    public String getStringEncoding() {
        return stringEncoding;
    }

    public void setAuth(AuthenticationInfo auth) {
        this.auth = auth;
    }

    public String getPrivateKeyLocation() {
        return privateKeyLocation;
    }

    public void setPrivateKeyLocation(String privateKeyLocation) {
        this.privateKeyLocation = privateKeyLocation;
    }

    public HandleResolver getResolver() {
        return resolver;
    }

    public void setResolver(HandleResolver resolver) {
        this.resolver = resolver;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setAuth_handle(String auth_handle) {
        this.auth_handle = auth_handle;
    }

    public void setAuth_index(String auth_index) {
        this.auth_index = auth_index;
    }

    public void setHandle_prefix(String handle_prefix) {
        this.handle_prefix = handle_prefix;
    }

    public void setHandleType(String handleType) {
        this.handleType = handleType;
    }

    public void setStringEncoding(String stringEncoding) {
        this.stringEncoding = stringEncoding;
    }

    public AbstractResponse getResponse() {
        return response;
    }

    public void setResponse(AbstractResponse response) {
        this.response = response;

    }
}