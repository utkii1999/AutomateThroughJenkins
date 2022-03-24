package ProjectDemo;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ProjectClass {

	WebDriver driver;

	static Map<String, String> data;

	static String projectPath = System.getProperty("user.dir");

	@BeforeClass
	public static void getYaml() {
		try {

			InputStream inputstream = new FileInputStream(new File(projectPath + "/src/test/java/config/config.yaml"));
			Yaml yaml = new Yaml();
			data = yaml.load(inputstream);
//			System.out.println(data);

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.out.println(e.getStackTrace());
		}
	}

	
	@Test(priority = 1)
	@Parameters("Browser")
	
	public void testLandingPage(String browserNamE) {
//		String browserNamE=System.getProperty("bN");
//		String browserNamE="Chrome";
			System.out.println("Parameter value is "+browserNamE);
			driver=null;
			if (browserNamE.contains("Chrome")) {
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();

			}
			else if(browserNamE.contains("Edge"))
			{
				WebDriverManager.edgedriver().setup();
				driver=new EdgeDriver();
			}
		driver.get(data.get("website_link"));
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

		Assert.assertTrue(driver.getTitle().contains("Page"), "Title does not match");
		System.out.println("Landed successfully");

	}

	@Test(priority = 2)
	public void testRadioButton() {
		List<WebElement> radio = driver.findElements(By.xpath(data.get("radio_button")));
		radio.get(0).click();
		radio.get(1).click();
		boolean bval = radio.get(1).isSelected();
		if (bval) {
			Assert.assertFalse(radio.get(0).isSelected());
			System.out.println("radio buttons working fine");

		}
	}

	@Test(priority = 3)
	public void testDropDown() {
		Select select = new Select(driver.findElement(By.id(data.get("drop_down_id"))));
		select.selectByVisibleText("Honda");
		String actual = select.getFirstSelectedOption().getText();
		Assert.assertEquals(actual, "Honda");
		System.out.println("Drop down list works fine");

	}

	@Test(priority = 4)
	public void testCheckBox() {
		List<WebElement> checkbox = driver.findElements(By.xpath(data.get("check_box")));
		checkbox.get(0).click();
		checkbox.get(1).click();
		boolean bval = checkbox.get(0).isSelected();
		if (bval) {
			Assert.assertTrue(checkbox.get(1).isSelected());
			Assert.assertFalse(checkbox.get(2).isSelected());
			System.out.println("checkboxes working fine");

		}

	}

	@Test(priority = 5)
	public void openWindowButtonTest() {
		driver.findElement(By.id(data.get("open_window_id"))).click();

		// -----------------------------Switching window
		// logic-------------------------------------

		String parent = driver.getWindowHandle();

		Set<String> s = driver.getWindowHandles();

		// Now iterate using Iterator
		Iterator<String> I1 = s.iterator();

		while (I1.hasNext()) {

			String child_window = I1.next();
			if (!parent.equals(child_window)) {
				driver.switchTo().window(child_window);
				Assert.assertEquals(driver.switchTo().window(child_window).getTitle(), "All Courses");
				System.out.println("Button is working fine");
				driver.close();
			}

		}
		driver.switchTo().window(parent);
	}

	@Test(priority = 6)
	public void openTabButtonTest() {
		driver.findElement(By.id(data.get("open_tab_id"))).click();
		String parent = driver.getWindowHandle();

		Set<String> s = driver.getWindowHandles();

		// Now iterate using Iterator
		Iterator<String> I1 = s.iterator();

		while (I1.hasNext()) {

			String child_window = I1.next();
			if (!parent.equals(child_window)) {
				driver.switchTo().window(child_window);
				Assert.assertEquals(driver.switchTo().window(child_window).getTitle(), "All Courses");

				System.out.println("Tab is opened ");
				driver.close();

			}
			driver.switchTo().window(parent);

		}
	}

	@Test(priority = 7)
	public void switchToAlerttest() {
		driver.findElement(By.id(data.get("name_textbox"))).sendKeys("Utkarsh");
		driver.findElement(By.id(data.get("alert_btn"))).click();
		// Explicit wait condition for alert
		WebDriverWait w = new WebDriverWait(driver, 5);
		// alertIsPresent() condition applied
		if (w.until(ExpectedConditions.alertIsPresent()) == null)
			System.out.println("Alert not exists");
		else {
			System.out.println("Alert exists");
			driver.switchTo().alert().accept();
		}

	}
	
	@Test(priority=8)
	public void testIframe()
	{
		WebDriver wd = driver.switchTo().frame("courses-iframe");
		System.out.println("iframe exists");
		driver.quit();
		
////		String iframe_text=driver.findElement(By.xpath("//button[text()='Enroll in Course']")).getText();
////		System.out.println(iframe_text);
//		String text=driver.findElement(By.xpath("//legend[text()='iFrame Example']")).getText();
//		System.out.println(text);
//		driver.switchTo().defaultContent();
//		driver.quit();
//		
	}


	
	
}
