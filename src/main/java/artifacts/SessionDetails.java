package artifacts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.EnvSetup;

import static utility.Constant.HTTPS;

public class SessionDetails {
    private final Logger ltLogger = LogManager.getLogger(SessionDetails.class);
    protected static final String SESSION_DETAILS_API = "/api/v1/test/";
    public String getSessionDetailUri(String sessionID) {
        ltLogger.info("Session ID is :- {}", sessionID);
        String uri = HTTPS + EnvSetup.USER_NAME + ":" + EnvSetup.USER_ACCESS_KEY + "@" + EnvSetup.HOST_API_URL + SESSION_DETAILS_API + sessionID;
        ltLogger.info("Session URI :- {} ", uri);
        return uri;
    }
}
