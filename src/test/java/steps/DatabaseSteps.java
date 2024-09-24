package steps;

import apiUtils.GetToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import entities.CustomResponse;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Assert;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseSteps {
    Logger logger = LogManager.getLogger(DatabaseSteps.class);
    RequestSpecification request;
    JSONObject requestBody = new JSONObject();
    Response response;
    String id;
    ObjectMapper mapper = new ObjectMapper();


    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    CustomResponse customResponse = new CustomResponse();
    List<Object> tags;
    CustomResponse[] customResponses;
    Faker faker = new Faker();

    @Given("base url {string}")
    public void base_url(String string) {

        request = RestAssured.given().baseUri(string)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);

    }
    @When("user provides valid token")
    public void user_provides_valid_token() {
        request = request.auth().oauth2(GetToken.getToken());
    }
    @When("user provides request body with {string} and {string}")
    public void user_provides_request_body_with_and(String key, String value) {
        requestBody.put(key,  value);

        request = request.body(requestBody.toString());
    }
    @Then("user hits POST endpoint {string}")
    public void user_hits_post_endpoint(String endpoint) {
        response = request.post(endpoint);
        id = response.jsonPath().getString("id");

    }
    @Then("verify status code is {int}")
    public void verify_status_code_is(Integer int1) {

        Assert.assertEquals(201, response.getStatusCode());
    }

    @Given("user set up connection to database")
    public void user_set_up_connection_to_database() throws SQLException {
        String url = "jdbc:postgresql://18.159.52.24:5434/postgres";
        String username = "cashwiseuser";
        String password = "cashwisepass";
      connection = DriverManager.getConnection(url, username, password);
      logger.info("User successful; setup connection");
    }
    @When("user sends the query {string}")
    public void user_sends_the_query(String query) throws SQLException {


            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(id));
            resultSet = preparedStatement.executeQuery();
            System.out.println(query);

    }
    @Then("verify result set contains {string} with {string}")
    public void verify_result_set_contains_with(String columnName, String value) throws SQLException, JsonProcessingException {

        Map<String, Object> row = new HashMap<>();
         while (resultSet.next()) {

             if(resultSet.getString("name_tag").equalsIgnoreCase(value)) {
                 row.put("name_tag", resultSet.getString("name_tag"));
                 row.put("tag_id", resultSet.getString("tag_id"));
                 row.put("creation_date", resultSet.getString("creation_date"));
                 row.put("company_name_id", resultSet.getString("company_name_id"));
                 customResponse.setName_tag("name_tag");
                 customResponse.setTag_id("tag_id");


                 break;
             }
         }
        System.out.println(customResponse.getName_tag());
        System.out.println(customResponse.getTag_id());
        System.out.println(row);
    }



    @When("user hits get all endpoint {string}")
    public void user_hits_get_all_endpoint(String endpoint) throws JsonProcessingException {
       response = request.get(endpoint);
      customResponses = mapper.readValue(response.asString(), CustomResponse[].class);
        System.out.println(customResponses[2].getId());

    }
    @When("user get the third tag and store it")
    public void user_get_the_third_tag_and_store_it() {
       id = customResponses[2].getId();
    }
    @When("user hits PUT api {string}")
    public void user_hits_put_api(String endpoint) {
        response = request.put(endpoint + id);
        response.prettyPrint();
    }
    @When("user changes the tag_name to {string}")
    public void user_changes_the_tag_name_to(String newTag) {
        requestBody.put("name_tag", newTag);
        requestBody.put("description", "changedTag");
    }
    @When("user verifies that the status code is {int}")
    public void user_verifies_that_the_status_code_is(Integer statusCode) {
//      Assert.assertEquals((int) statusCode, response.getStatusCode());

        response.prettyPrint();
    }







}
