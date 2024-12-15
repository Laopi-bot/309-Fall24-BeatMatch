package database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import database.EventsHub.EventsHub;
import database.EventsHub.EventsHubRepository;

import database.User.User;
import database.User.UserRepository;
import database.com.example.forgotpassword.ForgotPassword;
import database.com.example.forgotpassword.ForgotPasswordRepository;
import io.restassured.http.ContentType;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class OmSystemTest {

    @LocalServerPort
    int port;

    @Autowired
    private EventsHubRepository eventsHubRepository;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void createEventTest() {
        EventsHub event = new EventsHub("Sample Event", "Host Name", "2024-12-15", "Sample Location", 1, 50);

        // Send POST request to create an event
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(event)
                .when()
                .post("/events");

        // Validate response status and content
        assertEquals(201, response.getStatusCode());
        String responseBody = response.getBody().asString();
        assertEquals(true, responseBody.contains("Sample Event"));

        // Clean up
        eventsHubRepository.deleteById(Long.parseLong(response.jsonPath().get("id").toString()));
    }

    @Test
    public void getAllEventsTest() {
        // Send GET request to fetch all events
        Response response = RestAssured.given()
                .when()
                .get("/events");

        // Validate response status
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void getEventByIdTest() {
        // Create a sample event
        EventsHub event = new EventsHub("Test Event", "Test Host", "2024-12-15", "Test Location", 1, 100);
        EventsHub savedEvent = eventsHubRepository.save(event);

        // Send GET request to fetch the event by ID
        Response response = RestAssured.given()
                .when()
                .get("/events/" + savedEvent.getId());

        // Validate response status and content
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        assertEquals(true, responseBody.contains("Test Event"));

        // Clean up
        eventsHubRepository.deleteById(savedEvent.getId());
    }

    @Test
    public void updateEventTest() {
        // Create a sample event
        EventsHub event = new EventsHub("Old Event", "Old Host", "2024-12-10", "Old Location", 1, 100);
        EventsHub savedEvent = eventsHubRepository.save(event);

        // Update the event
        EventsHub updatedEvent = new EventsHub("Updated Event", "Updated Host", "2024-12-20", "Updated Location", 2, 200);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedEvent)
                .when()
                .put("/events/" + savedEvent.getId());

        // Validate response status and content
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        assertEquals(true, responseBody.contains("Updated Event"));

        // Clean up
        eventsHubRepository.deleteById(savedEvent.getId());
    }

    @Test
    public void deleteEventTest() {
        // Create a sample event
        EventsHub event = new EventsHub("To Delete", "Delete Host", "2024-12-25", "Delete Location", 3, 300);
        EventsHub savedEvent = eventsHubRepository.save(event);

        // Send DELETE request
        Response response = RestAssured.given()
                .when()
                .delete("/events/" + savedEvent.getId());

        // Validate response status
        assertEquals(204, response.getStatusCode());

        // Ensure the event is deleted
        boolean exists = eventsHubRepository.existsById(savedEvent.getId());
        assertEquals(false, exists);
    }

    @Test
    public void createForgotPasswordTest() {
        ForgotPassword forgotPassword = new ForgotPassword("testuser@exampless.com", "Answer1", "Answer2");

        // Send POST request to create a forgot password record
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(forgotPassword)
                .when()
                .post("/forgetPassword");

        // Validate response status and content
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        assertEquals(true, responseBody.contains("success"));


    }

    @Test
    public void getAllForgotPasswordTest() {
        // Send GET request to fetch all forgot password records
        Response response = RestAssured.given()
                .when()
                .get("/forgetPassword");

        // Validate response status
        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void getForgotPasswordByEmailTest() {
        // Create a sample forgot password record
        ForgotPassword forgotPassword = new ForgotPassword("sampleuser@exampless.com", "Answer1", "Answer2");
        forgotPasswordRepository.save(forgotPassword);

        // Send GET request to fetch the record by email
        Response response = RestAssured.given()
                .when()
                .get("/forgetPassword/sampleuser@example.com");

        // Validate response status and content
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        assertEquals(true, responseBody.contains("sampleuser@example.com"));

        // Clean up
    //    forgotPasswordRepository.deleteById(forgotPassword.getEmail());
    }

    @Test
    public void validateSecurityQuestionsTest() {
        // Create a sample forgot password record
        ForgotPassword forgotPassword = new ForgotPassword("validateuserss@example.com", "CorrectAnswer1", "CorrectAnswer2");
        forgotPasswordRepository.save(forgotPassword);

        // Create a matching user
        User user = new User("First", "Last", "validateuserss@example.com", "validateusers", "password", 1, 1);
        userRepository.save(user);

        // Send POST request to validate security questions
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(forgotPassword)
                .when()
                .post("/forgetPassword/validate");

        // Validate response status and content
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        assertEquals(true, responseBody.contains("password"));

    }

    @Test
    public void deleteForgotPasswordTest() {
        // Create a sample forgot password record
        ForgotPassword forgotPassword = new ForgotPassword("deleteuser@example.com", "Answer1", "Answer2");
        forgotPasswordRepository.save(forgotPassword);

        // Send DELETE request
        Response response = RestAssured.given()
                .when()
                .delete("/forgetPassword/deleteuser@example.com");

        // Validate response status
        assertEquals(200, response.getStatusCode());
        String responseBody = response.getBody().asString();
        assertEquals(true, responseBody.contains("success"));


    }
}

