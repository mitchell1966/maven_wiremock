package step_def;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Created by mitchell on 11/04/17.
 */
public class testing {
    Response resp;
    String jsonAsString = "";
    String jsonData = "";
    String filePath = "";
    JSONObject fileName;
    WireMockServer wireMockRun;
    String balanceCheckUrlRegex = "/pttg/financialstatus/v1/accounts/\\d{6}/\\d{8}/dailybalancestatus*";
    String consentCheckUrkRegex = "/pttg/financialstatus/v1.*";
    WireMockServer wm = new WireMockServer(8082);
    Logger LOGGER = LoggerFactory.getLogger(testing.class);

    public void setup(String jsonData) {
        try {
          this.jsonData =  loadData(jsonData).toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        wireMockRun = wm;
        wireMockRun.start();
        WireMock.configureFor("localhost", 8082);
        LOGGER.debug("Started Wiremock Server");
        if (wireMockRun.isRunning() == true) {
            System.out.println("Wiremock is running");
        }
    }

    public void endWireMock() {
        wireMockRun.stop();
    }

    public JSONObject loadData(String file_Name) throws Exception {
        File dataFolder = new File("src");
        filePath = dataFolder.getAbsolutePath();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filePath+"/resources/account_data/" + file_Name + ".json"));
        JSONObject js = (JSONObject) obj;
        fileName = js;
        return fileName;

    }

    public void exactUrlOnly(String url) throws Exception {

        stubFor(get(urlMatching(url))
                .willReturn(aResponse()
                        .withBody(jsonData)
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));
        System.out.println("Printing " + jsonData);

    }

    public void consenttUrlOnly(String url) throws Exception {

        stubFor(WireMock.get(urlPathMatching(url))
                .willReturn(aResponse()
                        .withBody(jsonData)
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));
        System.out.println("Printing " + jsonData);


    }


}
