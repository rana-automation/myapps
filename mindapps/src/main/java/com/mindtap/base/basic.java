package com.mindtap.base;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class basic {

    Properties P;
    protected File file;
    protected String Tier;
    protected String EnvURL;
    protected String browser;
    protected String instructorEmail;
    protected String instructorPassword;
    protected String fulfillmenturl;
    protected String coursekey;
    protected String token;
    protected String titleIsbn;
    protected RemoteWebDriver driver;
    protected String Userurl;
    protected String path = System.getProperty("user.dir");


    public void selectEnvTestData()
    {
        Tier = readTier();
        if (Tier.equalsIgnoreCase("QA"))
        {
            readPropertiesFiles("QA_Env.properties");
        }
        else if (Tier.equalsIgnoreCase("INT"))
        {
            readPropertiesFiles("INT_Env.properties");
        }
        else if (Tier.equalsIgnoreCase("Stage"))
        {
            readPropertiesFiles("Stage_Env.properties");
        }

    }
    public String readTier () {
        return (System.getProperty("Tier"));
    }

    public String readBrowserDetails () {
        return (System.getProperty("Browser"));
    }

    public void generateTokenForStudentAndInstructor () {

        Response resp = RestAssured
                .given()
                .body(" {\"uid\": \"" + instructorEmail + "\" ,"
                        + " \"password\": \"" + instructorPassword + "\"} ")
                .when()
                .contentType(ContentType.JSON)
                .and()
                .header("cengage-sso-guid", "sso-guid")
                .post(fulfillmenturl);
        token = resp.jsonPath().getString("token");
    }

    public void mindtapURL ()
    {
        Userurl = EnvURL + "=" + token + "&eISBN=" + titleIsbn + "&courseKey=" + coursekey;
        System.out.println("mindtap URL " + Userurl);
    }

    public void readPropertiesFiles (String FilePath)
    {
        try {

            String filepath = path + "\\src\\main\\java\\com\\mindtap\\testdata\\"+ FilePath;
            file = new File(filepath);
            FileInputStream fileInput = new FileInputStream(file);
            P = new Properties();
            P.load(fileInput);
            EnvURL = P.getProperty("URL");
            instructorEmail = P.getProperty("InstructorUser");
            instructorPassword = P.getProperty("InstructorPassword");
            fulfillmenturl = P.getProperty("TokenURL");
            coursekey = P.getProperty("Coursekey");
            titleIsbn = P.getProperty("titleIsbn");
            System.out.println("****** Test Data ****** " + EnvURL);
            System.out.println("InstructorUser: " + instructorEmail);
            System.out.println("InstructorPassword: " + instructorPassword);
            System.out.println("Coursekey: " + coursekey);
            generateTokenForStudentAndInstructor();
            mindtapURL();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public WebDriver selectBrowserForExecution() {

        String Browser = readBrowserDetails();

        if (Browser.equalsIgnoreCase("Chrome")) {
            try {
                DesiredCapabilities caps = new DesiredCapabilities().chrome();

                caps.setPlatform(Platform.WINDOWS);
                driver = new RemoteWebDriver(new URL("http://seleniumw8-v7.corp.local:4444/wd/hub"), caps);
                return driver;
            } catch (MalformedURLException e){
                e.printStackTrace();
            }
        } else if (Browser.equalsIgnoreCase("Firefox")) {

            try {
                DesiredCapabilities caps = new DesiredCapabilities().firefox();
                caps.setVersion("46.0.1");
                caps.setPlatform(Platform.WINDOWS);
                driver = new RemoteWebDriver(new URL("http://seleniumw8-v7.corp.local:4444/wd/hub"), caps);
                return driver;
            }
            catch (MalformedURLException e)

            {
                e.printStackTrace();
            }
        } else if (Browser.equalsIgnoreCase("local")) {

            String driverPath = path + "\\BrowserDrivers\\";
            System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
            driver = new ChromeDriver();
            return driver;
        }
        return  driver;
    }

    public  void launchBrowser()
    {
        driver.get(Userurl);

    }
}


