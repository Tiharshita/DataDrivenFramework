package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import extentlisteners.ExtentListeners;
import utilities.DbManager;
import utilities.ExcelReader;
import utilities.MonitoringMail;

public class BaseTest {
	
	/*
	 * 
	 * WebDriver
	 * TestNG
	 * Listeners
	 * Extent Reports
	 * Log4j
	 * javamail
	 * jdbc
	 * screenshots
	 * keywords
	 * properties
	 * Excel Reading 
	 */
	
	
	public static WebDriver driver;
	public static Properties OR = new Properties();
	public static Properties config = new Properties();
	public static FileInputStream fis;
	public static ExcelReader excel = new ExcelReader("./src/test/resources/excel/testdata.xlsx");
	public static Logger log = Logger.getLogger(BaseTest.class);
	public static MonitoringMail mail = new MonitoringMail();
	public static WebDriverWait wait;
	public static WebElement dropdown;
	
	
	public void click(String locatorKey) {

		if (locatorKey.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(locatorKey))).click();

		} else if (locatorKey.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(locatorKey))).click();

		}
		if (locatorKey.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(locatorKey))).click();

		}

		log.info("Clicking on an Element : " + locatorKey);
		ExtentListeners.test.info("Clicking on an Element : " + locatorKey);
	
	}
	
	
	
	public boolean isElementPresent(String locatorKey) {

		try {
		if (locatorKey.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(locatorKey)));

		} else if (locatorKey.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(locatorKey)));

		}
		if (locatorKey.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(locatorKey)));

		}}catch(Throwable t) {
			
			
			log.info("Error while finding an Element : " + locatorKey);
			ExtentListeners.test.info("Error while finding an Element : " + locatorKey);
			
			return false;
			
		}

		log.info("Finding Presence of an Element : " + locatorKey);
		ExtentListeners.test.info("Finding Presence of an Element : " + locatorKey);
		
		return true;
	
	}
	
	public void type(String locatorKey, String value) {

		if (locatorKey.endsWith("_XPATH")) {

			driver.findElement(By.xpath(OR.getProperty(locatorKey))).sendKeys(value);

		} else if (locatorKey.endsWith("_CSS")) {

			driver.findElement(By.cssSelector(OR.getProperty(locatorKey))).sendKeys(value);

		}
		if (locatorKey.endsWith("_ID")) {

			driver.findElement(By.id(OR.getProperty(locatorKey))).sendKeys(value);

		}
		log.info("Typing in an Element : " + locatorKey + "  entered the value as : " + value);
		ExtentListeners.test.info("Typing in an Element : " + locatorKey + "  entered the value as : " + value);
		
	}

	
	
	
	public void select(String locatorKey, String value) {
		
		
		
		if (locatorKey.endsWith("_XPATH")) {

			dropdown = driver.findElement(By.xpath(OR.getProperty(locatorKey)));

		} else if (locatorKey.endsWith("_CSS")) {

			dropdown = driver.findElement(By.cssSelector(OR.getProperty(locatorKey)));

		}
		if (locatorKey.endsWith("_ID")) {

			dropdown = driver.findElement(By.id(OR.getProperty(locatorKey)));

		}
		
		Select select = new Select(dropdown);
		select.selectByVisibleText(value);
		
		
		log.info("Selecting an Element : " + locatorKey + "  and selected the value as : " + value);
		ExtentListeners.test.info("Selecting an Element : " + locatorKey + "  and selected the value as : " + value);
	
	}
	
	
	
	
	
	@BeforeSuite
	public void setUp() {
		
		
		if(driver == null) {
			
			PropertyConfigurator.configure("./src/test/resources/properties/log4j.properties");
			
			log.info("Test Execution started !!!");
			
			try {
				fis = new FileInputStream("./src/test/resources/properties/config.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				config.load(fis);
				log.info("Config properties file loaded !!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				fis = new FileInputStream("./src/test/resources/properties/OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
				log.info("OR properties file loaded !!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			if(config.getProperty("browser").equalsIgnoreCase("chrome")) {
				
				
				driver = new ChromeDriver();
				log.info("chrome browser launched");
			}else if(config.getProperty("browser").equalsIgnoreCase("firefox")) {
				
				
				driver = new FirefoxDriver();
				log.info("Firefox browser launched");
			}
			
			
			driver.get(config.getProperty("testsiteurl"));
			log.info("Navigated to : "+config.getProperty("testsiteurl"));
			
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(config.getProperty("implicit.wait"))));
			
			wait = new WebDriverWait(driver,Duration.ofSeconds(Integer.parseInt(config.getProperty("explicit.wait"))));
			
			
			try {
				DbManager.setMysqlDbConnection();
				log.info("Database connection established !!!");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	
	@AfterSuite
	public void tearDown() {
		
		//driver.quit();
		driver.close();
		log.info("Test execution completed !!!");
		
	}


}
