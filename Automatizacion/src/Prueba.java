import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Prueba {

	private WebDriver driver;
	private String DatoUsuario = "";
	private String DatoContraseña = "";

	// Configuración inicial para levantar el sitio
	@Before
	public void setUp() throws Exception {
		try {

			//Recorro el archivo excel para cargar los datos de login a utilizar
			File Archivo = new File("C:/Users/aleja/OneDrive/Escritorio/Prueba/Automatizacion/src/Excel/Datos.xlsx");

			FileInputStream fis = new FileInputStream(Archivo);

			Workbook libroExcel = new XSSFWorkbook(fis);

			Sheet hoja = libroExcel.getSheetAt(0);

			for (Row fila : hoja) {
				for (Cell columna : fila) {
					if (fila.getRowNum() > 0) {
						switch (columna.getColumnIndex()) {
							case 0:
								DatoUsuario = columna.getStringCellValue();
								break;
							case 1:
								DatoContraseña = columna.getStringCellValue();
								break;
							default:
								break;
						}
					}
				}
			}

			libroExcel.close();

			//configuración inicial
			System.setProperty("webdriver.chrome.driver", "C:/Users/aleja/OneDrive/Escritorio/Prueba/Automatizacion/src/Drivers/chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get("https://practice.automationtesting.in/my-account/");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Se realiza el login, se validan errores al iniciar sesión y se valida dato correspondiente al usuario
	@Test
	public void testLogin() {
		try {

			WebElement Usuario = driver.findElement(By.id("username"));
			Usuario.clear();
			Usuario.sendKeys(DatoUsuario);

			WebElement Contraseña = driver.findElement(By.id("password"));
			Contraseña.clear();
			Contraseña.sendKeys(DatoContraseña);

			WebElement Login = driver.findElement(By.xpath("//*[@id='customer_login']/div[1]/form/p[3]/input[3]"));
			Login.click();

			// Valido si existe algún error al iniciar sesión
			Boolean MensajeErrorVisible = driver.findElements(By.xpath("//*[@id='page-36']/div/div[1]/ul/li"))
					.size() > 0;
			if (MensajeErrorVisible == true) {
				assertFalse(
						"Error al iniciar sesión, el motivo es: "
								+ driver.findElement(By.xpath("//*[@id='page-36']/div/div[1]/ul/li")).getText(),
						MensajeErrorVisible);
			} else {

				WebElement TextoCorreo = driver.findElement(By.xpath("//*[@id='page-36']/div/div[1]/div/p[1]"));

				assertThat(TextoCorreo.getText(), containsString(DatoUsuario.substring(0, 3)));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Se realiza la prueba de navegación
	@Ignore
	@Test
	public void testNavegar() {
		try {
			//me logueo
			testLogin();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

			WebElement Direcciones = driver.findElement(By.xpath("//*[@id='page-36']/div/div[1]/nav/ul/li[4]/a"));
			Direcciones.click();

			// Me cambio a la ventana del ads y la cierro
			wait.until(ExpectedConditions.elementToBeClickable(By.id("aswift_3")));
			driver.switchTo().frame(driver.findElement(By.id("aswift_3")));

			// Esto es porque depende del ads el botón de cerrar se encuentra en otro iframe
			Boolean BotonCerrarVisible = driver.findElements(By.id("dismiss-button")).size() > 0;
			if (BotonCerrarVisible == false) {
				driver.switchTo().frame(driver.findElement(By.id("ad_iframe")));
			}

			WebElement CerrarVentana = driver.findElement(By.id("dismiss-button"));
			CerrarVentana.click();

			WebElement EditarDireccion = driver
					.findElement(By.xpath("//*[@id='page-36']/div/div[1]/div/div/div[2]/header/a"));
			EditarDireccion.click();

			WebElement Nombre = driver.findElement(By.id("shipping_first_name"));
			Nombre.clear();
			Nombre.sendKeys("Alejandro");

			WebElement Apellido = driver.findElement(By.id("shipping_last_name"));
			Apellido.clear();
			Apellido.sendKeys("Chaves");

			WebElement BotonPais = driver.findElement(By.xpath("//*[@id='s2id_shipping_country']/a/span[2]/b"));
			BotonPais.click();

			WebElement InputPais = driver.findElement(By.id("s2id_autogen1_search"));
			InputPais.clear();
			InputPais.sendKeys("Costa Rica");

			//Busco entre los resultados y selecciono la opción correspondiente
			List<WebElement> ResultadoPaises = driver.findElements(By.id("select2-results-1"));
			for (WebElement Resultado : ResultadoPaises) {
				Resultado.click();
			}

			WebElement Direccion = driver.findElement(By.id("shipping_address_1"));
			Direccion.clear();
			Direccion.sendKeys("25 mts noreste");

			WebElement Ciudad = driver.findElement(By.id("shipping_city"));
			Ciudad.clear();
			Ciudad.sendKeys("Carrillos Alto");

			WebElement Estado = driver.findElement(By.id("shipping_state"));
			Estado.clear();
			Estado.sendKeys("Alajuela");

			WebElement CodigoPostal = driver.findElement(By.id("shipping_postcode"));
			CodigoPostal.clear();
			CodigoPostal.sendKeys("20804");

			WebElement BotonGuardar = driver
					.findElement(By.xpath("//*[@id='page-36']/div/div[1]/div/form/p[10]/input[1]"));
			BotonGuardar.click();

			WebElement DireccionGuardada = driver.findElement(By.xpath("//*[@id='page-36']/div/div[1]/div[1]"));

			assertEquals(DireccionGuardada.getText(), "Address changed successfully.");

			//Thread.sleep(1000);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Se realiza la prueba para cerrar sesión
	@Ignore
	@Test
	public void testCerrarSesion() {
		try {
			testLogin();

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

			// intenta cerrar sesión hasta que ya no se pueda acceder al botón de cerrar
			// sesión
			do {

				WebElement CerrarSesion = driver.findElement(By.xpath("//*[@id='page-36']/div/div[1]/nav/ul/li[6]/a"));
				CerrarSesion.click();

			} while (driver.findElements(By.xpath("//*[@id='page-36']/div/div[1]/nav/ul/li[6]/a")).size() > 0);

			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//*[@id='customer_login']/div[1]/form/p[3]/input[3]")));

			WebElement TextoLogin = driver.findElement(By.xpath("//*[@id='customer_login']/div[1]/h2"));

			assertEquals(TextoLogin.getText(), "Login");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Se realiza la prueba para intentar acceder a una pagina sin haber logueado
	@Ignore
	@Test
	public void testPaginaProtegida() {
		try {
			driver.get("https://practice.automationtesting.in/my-account/orders/");

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

			wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//*[@id='customer_login']/div[1]/form/p[3]/input[3]")));
			WebElement TextoLogin = driver.findElement(By.xpath("//*[@id='customer_login']/div[1]/h2"));

			assertEquals(TextoLogin.getText(), "Login");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Metodo final para limpiar el driver
	@After
	public void tearDown() {
		driver.quit();
	}

}
