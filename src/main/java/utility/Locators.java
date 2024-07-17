package utility;

public class Locators {
    private static final String CLASS = "class";
    private static final String ID = "id";
    private static final String CSS = "css";
    private static final String XPATH = "xpath";
    private static final String NAME = "name";
    private static final String TAGNAME = "tagname";

    static final String[] LOCAL_SITE_HEADING = { CSS, "body h1" };
    static final String[] BAD_SSL_H1 = { CSS, "#content > h1:nth-child(1)" };
    static final String[] BASIC_AUTH_HEADING = { CSS, "h3" };
    static final String[] AUTHENTICATION_TEST_HEADING = { CSS, "h1" };
}
