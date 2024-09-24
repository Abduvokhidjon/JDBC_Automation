package apiUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;

public class GetToken {


    public static String getToken () {
        String endPoint = "https://backend.cashwise.us/api/myaccount/auth/login";


        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "jamolov121997@gmail.com");
        requestBody.put("password", "PLm9V5c2tQrqAgu");

        Response response = RestAssured.given().contentType(ContentType.JSON).body(requestBody.toString()).post(endPoint);
        System.out.println(response.jsonPath().getString("jwt_token"));
        return response.jsonPath().getString("jwt_token");
    }
}
