package resources;

import org.junit.Test;

import java.util.ResourceBundle;

public class FileTest {
    private static final String RESOURCE_DATABASE = "resources.database";
    private static final String RESOURCE_CONFIG = "resources.config";
    private static final String RESOURCE_MESSAGES = "resources.messages";
    private static final String RESOURCE_PAGECONTENT = "resources.pagecontent";
    private static final String RESOURCE_PAGECONTENT_EN = "resources.pagecontent_en";
    private static final String RESOURCE_PAGECONTENT_RU = "resources.pagecontent_ru";
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String AUTORECONNECT = "autoReconnect";
    private static final String CHARENCODING = "characterEncoding";
    private static final String USEUNICODE = "useUnicode";
    private static final String POOLSIZE = "poolsize";

    @Test
    public void databasePropertiesTest() {
        ResourceBundle resource = ResourceBundle.getBundle(RESOURCE_DATABASE);
        String url = resource.getString(URL);
        String user = resource.getString(USER);
        String pass = resource.getString(PASSWORD);
        String autoReconnect = resource.getString(AUTORECONNECT);
        String charEncoding = resource.getString(CHARENCODING);
        String useUnicode = resource.getString(USEUNICODE);
        int poolSize = Integer.parseInt(resource.getString(POOLSIZE));
    }

    @Test
    public void otherPropertiesTest() {
        ResourceBundle resource = ResourceBundle.getBundle(RESOURCE_CONFIG);
        resource = ResourceBundle.getBundle(RESOURCE_MESSAGES);
        resource = ResourceBundle.getBundle(RESOURCE_PAGECONTENT);
        resource = ResourceBundle.getBundle(RESOURCE_PAGECONTENT_EN);
        resource = ResourceBundle.getBundle(RESOURCE_PAGECONTENT_RU);
    }
}
