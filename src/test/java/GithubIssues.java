import java.util.ArrayList;
import java.util.List;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static io.restassured.RestAssured.*;

import org.testng.annotations.*;
import static org.hamcrest.Matchers.*;

public class GithubIssues {
    //I did no check if environment variables are defined - in case if they are not there would be no REST call, and the error would be quite clear
    private static final String BASE_URL = System.getenv("BASE_URL");
    private static final String token = System.getenv("TOKEN");
    //Here the PROJECTID could be not parsable as int, but in this case the error would be quite informative
    private static final int projectid = Integer.parseInt(System.getenv("PROJECTID"));

    //list of created issues
    private final List<Integer> issueIds = new ArrayList<>();

    /**
     * creates 4 issues and saves Ids
     */
    @BeforeClass
    public void populateTestData() {
        for(int i=0; i<4; i++) {
            var issue = given()
                    .header("PRIVATE-TOKEN", token)
                    .queryParam("confidential", false)
                    .queryParam("description", "Test issue number " + i)
                    .queryParam("id", projectid)
                    .queryParam("title", "Test issue number " + i)
                    .when()
                    .post(BASE_URL + "/projects/{projectid}/issues", projectid)//TODO param project
                    .then()
                    .statusCode(201)
                    .extract().response();
            int issueId = issue.jsonPath().getInt("iid");
            issueIds.add(issueId);
        }
    }

    /**
     * after test is done, we are clearing up all the issues we've created
     */
    @AfterClass
    public void cleanUpTestData() {
        while (issueIds.size()>0) {
            int issueId = issueIds.remove(0);
            given()
                    .header("PRIVATE-TOKEN", token)
                    .when()
                    .delete(BASE_URL + "/projects/{projectid}/issues/{issue_iid}", projectid, issueId)
                    .then()
                    .statusCode(204);
        }
    }

    /**
     * simple test to create 1 new issue
     */
    @Test
    public void createNewIssue() {
        var issue = given()
                .header("PRIVATE-TOKEN", token)
                .queryParam("confidential", false)
                .queryParam("description", "Test issue created within test")
                .queryParam("id", projectid)
                .queryParam("title", "Test issue created within test")
                .when()
                .post(BASE_URL + "/projects/{projectid}/issues", projectid)//TODO param project
                .then()
                .statusCode(201)
                .extract().response();
        int issueId = issue.jsonPath().getInt("iid");
        issueIds.add(issueId);
    }

    /**
     * getting list of issues, and checking that issues we've created during this run is on this list
     */
    @Test
    public void getListOfIssues() {
        int issueToFind = issueIds.get(0);
        given()
                .header("PRIVATE-TOKEN", token)
                .when()
                .get(BASE_URL + "/issues")
                .then()
                .statusCode(200)
                .body("iid", hasItem(issueToFind));
    }

    /**
     * simple check that update issue call returns correct statusCode
     */
    @Test
    public void updateIssue() {
        int issueToUpdate = issueIds.get(0);
        given()
                .header("PRIVATE-TOKEN", token)
                .queryParam("add_labels", "test")
                .when()
                .put(BASE_URL + "/projects/{projectid}/issues/{issue_iid}", projectid, issueToUpdate)
                .then()
                .statusCode(200);
    }

    /**
     * simple check of delete issue call
     */
    @Test
    public void deleteIssue() {
        int issueId = issueIds.remove(0);
        given()
                .header("PRIVATE-TOKEN", token)
                .when()
                .delete(BASE_URL + "/projects/{projectid}/issues/{issue_iid}", projectid, issueId)
                .then()
                .statusCode(204);
    }
}
