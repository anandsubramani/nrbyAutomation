package com.nrby.ui.login;

import java.io.InputStream;
import java.lang.reflect.Method;

import junit.framework.Assert;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nrby.ui.util.AppContext;

@SuppressWarnings("deprecation")
public class LoginTest {
	private static Logger logger = Logger.getLogger("LoginTest");
	private String nrbyUrl;
	private String userName;
	private String password;
	private WebDriver webDriver;
	private String dataFileLocation;
	private static final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";

	@BeforeClass
	@Parameters({ "nrbyUrl", "userName", "password", "dataFile" })
	public void beforeClass(String nrbyUrl, String userName, String password,
			String dataFile) throws Exception {
		this.nrbyUrl = nrbyUrl;
		this.userName = userName;
		this.password = password;
		this.dataFileLocation = dataFile;
		webDriver = new FirefoxDriver();
		webDriver.manage().window().maximize();
		AppContext.setWebDriver(webDriver);
		System.setProperty(ESCAPE_PROPERTY, "false");
		Thread.sleep(1000);
	}

	// Login Testing
	@Test()
	public void testLogin() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			webDriver.findElement(By.xpath("//div/input")).clear();
			webDriver.findElement(By.xpath("//div/input")).sendKeys(
					this.userName);
			webDriver.findElement(By.xpath("//section[2]/div/input")).clear();
			webDriver.findElement(By.xpath("//section[2]/div/input")).sendKeys(
					this.password);
			webDriver.findElement(By.xpath("//section/input")).click();
			Thread.sleep(1000);
			boolean b1 = webDriver.findElement(By.xpath("//li[5]")).getText()
					.contains("logged in as");
			if (b1 == false) {
				logger.info("Login functionality Failed");
				Assert.assertEquals("Login functionality", true, b1);
			} else {
				logger.info("Login functionality passed");
			}
		} catch (Exception e) {
			logger.info("Login Failed");
			Assert.assertEquals("Login functionality Failed", true, false);
		}
		webDriver.findElement(By.xpath("//span[2]/a")).click();
		Thread.sleep(1000);
	}

	// Signed in Successfully message validation
	@Test()
	public void testLoginSignedInMessage() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			webDriver.findElement(By.xpath("//div/input")).clear();
			webDriver.findElement(By.xpath("//div/input")).sendKeys(
					this.userName);
			webDriver.findElement(By.xpath("//section[2]/div/input")).clear();
			webDriver.findElement(By.xpath("//section[2]/div/input")).sendKeys(
					this.password);
			webDriver.findElement(By.xpath("//section/input")).click();
			Thread.sleep(1000);
			boolean b1 = webDriver.getPageSource().contains(
					"Signed in successfully.");
			if (b1 == false) {
				logger.info("Signed in successfully message display Failed");
				Assert.assertEquals("Signed in successfully message display",
						true, b1);
			} else {
				logger.info("Signed in successfully message display passed");
			}
		} catch (Exception e) {
			logger.info("Signed in successfully message display Failed");
			Assert.assertEquals(
					"Signed in successfully message display Failed", true,
					false);
		}
	}

	// Invalid email message validation
	@Test(dataProvider = "getData")
	public void testInvalidEmailValidation(String loginEmail,
			String loginPassword) throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			webDriver.findElement(By.xpath("//div/input")).clear();
			webDriver.findElement(By.xpath("//div/input")).sendKeys(loginEmail);
			webDriver.findElement(By.xpath("//section[2]/div/input")).clear();
			webDriver.findElement(By.xpath("//section[2]/div/input")).sendKeys(
					loginPassword);
			webDriver.findElement(By.xpath("//section/input")).click();
			Thread.sleep(1000);
			boolean b1 = webDriver.getPageSource().contains(
					("Invalid email or password."));
			boolean b2 = webDriver.getPageSource().contains(
					("Invalid email address or password."));
			boolean b3 = b1 || b2;
			if (b3 == false) {
				logger.info("Invalid Email Validation Failed");
				Assert.assertEquals("Invalid Email Validation", true, b3);
			} else {
				logger.info("Invalid Email Validation passed");
			}
		} catch (Exception e) {
			logger.info("Invalid Email Validation Failed");
			Assert.assertEquals("Invalid Email Validation Failed", true, false);
		}
	}

	// Invalid Password message validation
	@Test(dataProvider = "getData")
	public void testInvalidPasswordValidation(String loginEmail,
			String loginPassword) throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			webDriver.findElement(By.xpath("//div/input")).clear();
			webDriver.findElement(By.xpath("//div/input")).sendKeys(loginEmail);
			webDriver.findElement(By.xpath("//section[2]/div/input")).clear();
			webDriver.findElement(By.xpath("//section[2]/div/input")).sendKeys(
					loginPassword);
			webDriver.findElement(By.xpath("//section/input")).click();
			Thread.sleep(1000);
			boolean b1 = webDriver.getPageSource().contains(
					("Invalid email or password."));
			boolean b2 = webDriver.getPageSource().contains(
					("Invalid email address or password."));
			boolean b3 = b1 || b2;
			if (b3 == false) {
				logger.info("Invalid Password Validation Failed");
				Assert.assertEquals("Invalid Password Validation", true, b3);
			} else {
				logger.info("Invalid Password Validation passed");
			}
		} catch (Exception e) {
			logger.info("Invalid Password Validation Failed");
			Assert.assertEquals("Invalid Password Validation Failed", true,
					false);
		}
	}

	@DataProvider
	public Object[][] getData(Method method) {
		return getTableArray(this.dataFileLocation, this.getClass()
				.getSimpleName(), method.getName());
	}

	@SuppressWarnings("static-access")
	public String[][] getTableArray(String xlFilePath, String sheetName,
			String tableName) {
		String[][] tabArray = null;
		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getSystemResourceAsStream(xlFilePath);
			Workbook workbook = Workbook.getWorkbook(is);
			Sheet sheet = workbook.getSheet(sheetName);
			int startRow, startCol, endRow, endCol, ci, cj;
			Cell tableStart = sheet.findCell(tableName);
			startRow = tableStart.getRow();
			startCol = tableStart.getColumn();

			Cell tableEnd = sheet.findCell(tableName, startCol + 1,
					startRow + 1, 100, 64000, false);

			endRow = tableEnd.getRow();
			endCol = tableEnd.getColumn();
			System.out.println("startRow=" + startRow + ", endRow=" + endRow
					+ ", " + "startCol=" + startCol + ", endCol=" + endCol);
			tabArray = new String[endRow - startRow - 1][endCol - startCol - 1];
			ci = 0;

			for (int i = startRow + 1; i < endRow; i++, ci++) {
				cj = 0;
				for (int j = startCol + 1; j < endCol; j++, cj++) {
					tabArray[ci][cj] = sheet.getCell(j, i).getContents();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tabArray;
	}

	@AfterClass
	public void afterClass() {
		webDriver.close();
		webDriver.quit();
	}

}