package org.veeva.utilities;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.bytebuddy.agent.builder.AgentBuilder;

public class LinkValidationUtil {
    static Response response;
    public static boolean isUrlWorking(String url) {
        System.out.println(url);
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
            System.out.println(response.getStatusCode());
            return (response.getStatusCode() == 200) || (response.getStatusCode() == 403);

        } catch (Exception e) {
            return false;
        }
    }
}
