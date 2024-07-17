package helper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.*;
import utility.Base;

import java.util.Map;

public class APIHelper extends Base {
    private final Logger ltLogger = LogManager.getLogger(APIHelper.class);

    public Response httpMethod(String method,
                               String uri,
                               String body,
                               ContentType contentType,
                               Map<String, Object> headers,
                               Map<String, Object> queryParam,
                               int expectedStatus) {

        ltLogger.info("Hitting Method :- {} on URI :- {} with Body :- {}, Headers :- {}, Query Param :- {} and Content Type :- {} and Expected Status is :- {}", method, uri, body, headers, queryParam, contentType, expectedStatus);

        RequestSpecification req = RestAssured.given();
        if (headers != null)
            req.headers(headers);
        if (queryParam != null)
            req.queryParams(queryParam);
        if (body != null) {
            req.body(body);
        }

        if (contentType != null) {
            req.contentType(contentType);
        }

        return switch (method) {
            case GET -> req.get(uri).then().statusCode(expectedStatus).extract().response();
            case GET_REDIRECT -> req.redirects().follow(false).get(uri).then().extract().response();
            case GET_WITHOUT_STATUS_CODE_VERIFICATION -> req.get(uri);
            case POST -> req.post(uri).then().statusCode(expectedStatus).extract().response();
            case PUT -> req.put(uri).then().statusCode(expectedStatus).extract().response();
            case DELETE -> req.delete(uri).then().statusCode(expectedStatus).extract().response();
            case PATCH -> req.patch(uri).then().statusCode(expectedStatus).extract().response();
            default -> null;
        };
    }
}
