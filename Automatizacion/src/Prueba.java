import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Prueba {

    private WebDriver driver;

    @Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver", "C:/Users/aleja/OneDrive/Escritorio/Prueba/Automatizacion/src/Drivers/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://practice.automationtesting.in/my-account/");
	}

    @Test
	public void testLogin() {
		
	    WebElement Usuario = driver.findElement(By.id("username"));
		Usuario.clear();
		Usuario.sendKeys("alejandrochalu96@gmail.com");

        WebElement Contraseña = driver.findElement(By.id("password"));
		Contraseña.clear();
		Contraseña.sendKeys("Prueba_12345");

        WebElement Login = driver.findElement(By.xpath("//*[@id='customer_login']/div[1]/form/p[3]/input[3]"));
        Login.click();
		

		driver.manage().timeouts();
		assertEquals("My Account – Automation Practice Site",driver.getTitle());
	}

    @After
	public void tearDown() {
		driver.quit();
	}

}
