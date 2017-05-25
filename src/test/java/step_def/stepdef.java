package step_def;

import com.jayway.restassured.response.Response;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Map;
import java.util.Set;

import static com.jayway.restassured.RestAssured.get;

/**
 * Created by mitchell on 13/04/17.
 */
public class stepdef {

    public Response resp;
    public Response respCalc;
    public Response consent;
    String jsonAsString;
    String fromDate = "";
    String toDate = "";
    String accountNumber = "";
    String sortCode = "";
    String minimum = "";
    String dob = "";
    String balanceCheckUrlRegex = "/financialstatus/v1.*";
    String consentCheckUrkRegex = "/financialstatus/v1.*";



    testing ts = new testing();

    public void getTableData(DataTable arg) {
        Map<String, String> entries = arg.asMap(String.class, String.class);
        Set<String> tableKey = entries.keySet();

        for (String s : tableKey) {


            if (s.equalsIgnoreCase("Account Number")) {
                accountNumber = entries.get(s);
            }
            if (s.equalsIgnoreCase("Minimum")) {
                minimum = entries.get(s);
            }
            if (s.equalsIgnoreCase("From Date")) {
                fromDate = entries.get(s);
            }
            if (s.equalsIgnoreCase("Sort Code")) {
                sortCode = entries.get(s);
            }
            if (s.equalsIgnoreCase("To Date")) {
                toDate = entries.get(s);
            }

            if (s.equalsIgnoreCase("Date of Birth")) {
                dob = entries.get(s);
            }

        }
    }

    @Given("^the loaded data is (\\d+)$")
    public void the_loaded_data_is(String arg) {
        ts.setup(arg);
        try {
            ts.exactUrlOnly(balanceCheckUrlRegex);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Given("^the endpoint is ready$")
    public void the_endpoint_is_ready() {

    }

    @Given("^consent is granted for the following$")
    public void consent_is_granted_for_the_following(DataTable arg1) throws Throwable {
        this.getTableData(arg1);
        ts.setup(accountNumber);
        ts.consenttUrlOnly(balanceCheckUrlRegex);
        consent = get("http://localhost:8080/pttg/financialstatus/v1/accounts/{sortCode}/{accountNumber}/consent?dob={dob}", sortCode, accountNumber,dob);
        jsonAsString = consent.asString();

        System.out.println("Family Case Worker API: " + jsonAsString);
    }

    @When("^the endpoint is called$")
    public void the_endpoint_is_called(DataTable arg) {
        this.getTableData(arg);
        ts.setup(accountNumber);
        resp = get("http://localhost:8080/pttg/financialstatus/v1/accounts/{sortCode}/{accountNumber}/dailybalancestatus?fromDate={fromDate}&toDate={toDate}&minimum={minimum}&dob={dob}", sortCode, accountNumber, fromDate, toDate, minimum, dob);
        jsonAsString = resp.asString();

        System.out.println("Family Case Worker API: " + jsonAsString);
    }

    @Then("^stuff happens$")
    public void stuff_happens() throws Throwable {
        ts.endWireMock();
    }






}
