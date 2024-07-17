package utility;

public class Constant {

    // ENV
    public static final String ENV_DEV = "dev";
    public static final String ENV_STAGE = "stage";
    public static final String ENV_PROD = "prod";


    // TUNNEL
    protected static final String TUNNEL_DIRECTORY = "./src/main/resources/tunnel/";
    protected static final String TUNNEL_LOG_DIRECTORY = "/logs/tunnel/";

    protected static final String TUNNEL_DIRECTORY_MAC = TUNNEL_DIRECTORY + "LT_Mac";
    protected static final String TUNNEL_DIRECTORY_WINDOWS = TUNNEL_DIRECTORY + "LT_Win";
    protected static final String TUNNEL_DIRECTORY_LINUX = TUNNEL_DIRECTORY + "LT_Linux";
    protected static final String[] TUNNEL_MODES = new String[] { "tcp", "ssh" };
    public static final String TUNNEL_STAGE_DOMAIN = " --server-domain stage-ts.lambdatestinternal.com";


    // STRING && BOOLEAN
    public static final String NEW_LINE = "\n";
    protected static final String OS_NAME = "os.name";
    protected static final String USER_DIR = "user.dir";
    protected static final String TRUE_STRING = "true";
    protected static final String FALSE_STRING = "true";


    // HTTP REQUEST TYPE
    public static final String HTTPS = "https://";
    public static final String HTTP = "http://";
    protected static final String GET = "GET";
    protected static final String GET_REDIRECT = "GET_REDIRECT";
    protected static final String GET_WITHOUT_STATUS_CODE_VERIFICATION = "GET_WITHOUT_STATUS_CODE_VERIFICATION";
    protected static final String POST = "POST";
    protected static final String PUT = "PUT";
    protected static final String PATCH = "PATCH";
    protected static final String DELETE = "DELETE";


    // URLs
    protected static String localHostUrl = "http://localhost:8000/";
    protected static String ltStageLambdaDevopsUrl = "https://stage-lambda-devops-use-only.lambdatestinternal.com/To-do-app/index.html";
    protected static String rttLambdaTestInternalUrl = "https://rtt.lambdatestinternal.com/api/getinfo";
    protected static String bhPhotoVideoUrl = "https://www.bhphotovideo.com/";
    protected static String iHerbUrl = "https://in.iherb.com/";
    protected static String toySRusUrl = "https://www.toysrus.com/";
    protected static String yoOxUrl = "https://www.yoox.com/us/";
    protected static String pmUrl = "https://www.6pm.com/";
    protected static String marksAndSpencerUrl = "https://www.marksandspencer.in/";
    protected static String backCountryUrl = "https://backcountry.com/";
    protected static String mangoUrl = "https://www.mango.com/";
    protected static String abeBooksUrl = "https://www.abebooks.com/";
    protected static String shopLegoUrl = "http://shop.lego.com/";
    protected static String rakuTenUrl = "https://www.rakuten.com/";
    protected static String saksFifthAvenueUrl = "https://www.saksfifthavenue.com/";
    protected static String[] networkUrls = { rttLambdaTestInternalUrl, bhPhotoVideoUrl, iHerbUrl, toySRusUrl, yoOxUrl, pmUrl, marksAndSpencerUrl, backCountryUrl, mangoUrl, abeBooksUrl, shopLegoUrl, rakuTenUrl, saksFifthAvenueUrl };
    protected static String selfSignedUrl = "https://self-signed.badssl.com/";
    protected static String expiredUrl = "https://expired.badssl.com/";
    protected static String wrongHostUrl = "https://wrong.host.badssl.com/";
    protected static String untrustedRootUrl = "https://untrusted-root.badssl.com/";
    protected static String[] badSslUrls = { selfSignedUrl, expiredUrl, wrongHostUrl, untrustedRootUrl };
    protected static String internetHerokuAppBasicAuthUrl = "https://admin:admin@the-internet.herokuapp.com/basic_auth";
    protected static String authenticationTestHttpAuthUrl = "https://user:pass@authenticationtest.com/HTTPAuth/";

    // CONSTANTS
    protected static String consoleMsg = "javascript console log is working fine";
    protected static String consoleLogs = "consoleLogs";
    protected static String rttLambdaTestInternalTitle = "RTT Verifier";
    protected static String bhPhotoVideoTitle = "B&H Photo Video Digital Cameras, Photography, Computers";
    protected static String iHerbTitle = "iHerb - Vitamins, Supplements, Natural Health Products";
    protected static String toySRusTitle = "Toysrus.com, The Official Toys”R”Us Site - Toys, Games, & More";
    protected static String yoOxTitle = "YOOX | Designer Brands and Home Decor ";
    protected static String pmTitle = "Discount Shoes, Clothing & Accessories | 6pm";
    protected static String marksAndSpencerTitle = "Marks & Spencer India | Buy Women, Men & Kids Clothing";
    protected static String backCountryTitle = "Backcountry - Outdoor Gear & Clothing for Ski, Snowboard, Camp, & More";
    protected static String mangoTitle = "Mango";
    protected static String abeBooksTitle = "AbeBooks | Shop for Books, Art & Collectibles";
    protected static String shopLegoTitle = "Home | Official LEGO® IN";
    protected static String rakuTenTitle = "Online Coupons & Cash Back | Shop 3,500+ Stores! | Rakuten";
    protected static String saksFifthAvenueTitle = "Luxury Fashion & Designer Clothing, Shoes, Handbags & More | Saks Fifth Avenue";
    protected static String[] networkUrlsTitle = { rttLambdaTestInternalTitle, bhPhotoVideoTitle, iHerbTitle, toySRusTitle, yoOxTitle, pmTitle, marksAndSpencerTitle, backCountryTitle, mangoTitle, abeBooksTitle, shopLegoTitle, rakuTenTitle, saksFifthAvenueTitle };
    protected static String selfSignedH1 = "self-signed.badssl.com";
    protected static String expiredH1 = "expired.badssl.com";
    protected static String wrongHostH1 = "wrong.host.badssl.com";
    protected static String untrustedRootH1 = "untrusted-root.badssl.com";
    protected static String[] badSslUrlsTitle = { selfSignedH1, expiredH1, wrongHostH1, untrustedRootH1 };
    protected static String internetHerokuAppBasicAuthUrlH3 = "Basic Auth";
    protected static String authenticationTestHttpAuthUrlH1 = "Login Success";
}
