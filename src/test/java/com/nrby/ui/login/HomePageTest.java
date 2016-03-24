package com.nrby.ui.login;

import java.io.InputStream;
import org.apache.log4j.Logger;

import junit.framework.Assert;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nrby.ui.util.AppContext;

@SuppressWarnings("deprecation")
public class HomePageTest {

	private static Logger logger = Logger.getRootLogger();

	private String nrbyUrl;
	private WebDriver webDriver;
	private static final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";

	@BeforeClass
	@Parameters({ "nrbyUrl" })
	public void beforeClass(String nrbyUrl) throws Exception {
		this.nrbyUrl = nrbyUrl;
		webDriver = new FirefoxDriver();
		webDriver.manage().window().maximize();
		AppContext.setWebDriver(webDriver);
		System.setProperty(ESCAPE_PROPERTY, "false");
		Thread.sleep(1000);
	}

	// Home page verification
	@Test()
	public void testEmailLabel() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("testEmailLabel starts...");
		boolean b1 = webDriver.findElement(By.xpath("//label")).getText()
				.contentEquals("email");
		Assert.assertEquals("Label Email exists", true, b1);
		logger.info("testEmailLabel ends...");
	}

	@Test()
	public void testPasswordLabel() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b2 = webDriver.findElement(By.xpath("//section[2]/label"))
				.getText().contentEquals("password");
		Assert.assertEquals("Label Password exists", true, b2);
	}

	@Test()
	public void testRememberMeText() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b3 = webDriver.findElement(By.xpath("//div/label")).getText()
				.contentEquals("remember me");
		Assert.assertEquals("Label Remember me exists", true, b3);
	}

	@Test()
	public void testDisplayRememberMeCheckbox() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b4 = webDriver.findElement(By.xpath("//input[2]"))
				.isDisplayed();
		Assert.assertEquals("Remember me checkbox displayed", true, b4);
	}

	@Test()
	public void testLoginButton() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b5 = webDriver.findElement(By.xpath("//section/input"))
				.isDisplayed();
		Assert.assertEquals("Login Button displayed", true, b5);
	}

	@Test()
	public void testForgotPasswordText() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b6 = webDriver.findElement(By.xpath("//a")).getText()
				.contentEquals("Forgot your password?");
		Assert.assertEquals("Forgot your password: text display", true, b6);
	}

	@Test()
	public void testDidntRcvConfirmationText() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b7 = webDriver.findElement(By.xpath("//a[2]")).getText()
				.contentEquals("Didn't receive confirmation instructions?");
		Assert.assertEquals(
				"Didn't receive confirmation instructions: text display", true,
				b7);
	}

	@Test()
	public void testEmailTextField() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b8 = (webDriver.findElements(By.xpath("//div/input")).size() != 0);
		Assert.assertEquals("Email Text Field display", true, b8);
	}

	@Test()
	public void testPasswordTextField() throws Exception {
		webDriver.get(nrbyUrl + "/");
		logger.info("Test case starts...");
		boolean b9 = (webDriver
				.findElements(By.xpath("//section[2]/div/input")).size() != 0);
		Assert.assertEquals("Password Text Field display", true, b9);
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