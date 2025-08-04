package org.veeva.utilities;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.bytebuddy.agent.builder.AgentBuilder;

public class LinkValidationUtil {
    static Response response;
    public static boolean isUrlWorking(String url) {
        try {
            RestAssured.useRelaxedHTTPSValidation();
            response = RestAssured
                    .given()
                    .relaxedHTTPSValidation()
                    .when()
                    .get(url)
                    .then()
                    .extract()
                    .response();

            return response.getStatusCode() == 200;

        } catch (Exception e) {
            if(!(response.statusCode() ==200)){
                return false;
            }else {
                return true;
            }
        }
    }
}
