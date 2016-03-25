package com.nrby.ui.login;

import java.io.InputStream;

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
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nrby.ui.util.AppContext;

@SuppressWarnings("deprecation")
public class HomePageTest {
	private static Logger logger = Logger.getLogger("HomePageTest");
	private String nrbyUrl;
	private WebDriver webDriver;
	private static final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";

	@BeforeClass
	@Parameters({ "nrbyUrl" })
	public void beforeClass(String nrbyUrl) throws Exception {
		logger.info("Automation Nrby Started");
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
		try {
			boolean b1 = webDriver.findElement(By.xpath("//label")).getText()
					.contentEquals("email");
			if (b1 == false) {
				logger.info("Label Email exists Failed");
				Assert.assertEquals("Label Email exists", true, b1);
			} else {
				logger.info("Label Email exists passed");
			}
		} catch (Exception e) {
			logger.info("Label Email exists Failed");
			Assert.assertEquals("Label Email exists", true, false);
		}
	}

	@Test()
	public void testPasswordLabel() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b2 = webDriver.findElement(By.xpath("//section[2]/label"))
					.getText().contentEquals("password");
			if (b2 == false) {
				logger.info("Label Password exists Failed");
				Assert.assertEquals("Label Password exists", true, b2);
			} else {
				logger.info("Label Password exists passed");
			}
		} catch (Exception e) {
			logger.info("Label Password exists Failed");
			Assert.assertEquals("Label Password exists", true, false);
		}
	}

	@Test()
	public void testRememberMeText() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b3 = webDriver.findElement(By.xpath("//div/label"))
					.getText().contentEquals("remember me");
			if (b3 == false) {
				logger.info("Label Remember me exists Failed");
				Assert.assertEquals("Label Remember me exists", true, b3);
			} else {
				logger.info("Label Remember me exists passed");
			}
		} catch (Exception e) {
			logger.info("Label Remember me exists Failed");
			Assert.assertEquals("Label Remember me exists", true, false);
		}
	}

	@Test()
	public void testDisplayRememberMeCheckbox() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b4 = webDriver.findElement(By.xpath("//input[2]"))
					.isDisplayed();
			if (b4 == false) {
				logger.info("Remember me checkbox displayed Failed");
				Assert.assertEquals("Remember me checkbox displayed", true, b4);
			} else {
				logger.info("Remember me checkbox displayed passed");
			}
		} catch (Exception e) {
			logger.info("Remember me checkbox displayed Failed");
			Assert.assertEquals("Remember me checkbox displayed", true, false);
		}
	}

	@Test()
	public void testLoginButton() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b5 = webDriver.findElement(By.xpath("//section/input"))
					.isDisplayed();
			if (b5 == false) {
				logger.info("Login Button displayed Failed");
				Assert.assertEquals("Login Button display", true, b5);
			} else {
				logger.info("Login Button displayed passed");
			}
		} catch (Exception e) {
			logger.info("Login Button displayed Failed");
			Assert.assertEquals("Login Button display", true, false);
		}
	}

	@Test()
	public void testForgotPasswordText() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b6 = webDriver.findElement(By.xpath("//a")).getText()
					.contentEquals("Forgot your password?");
			if (b6 == false) {
				logger.info("Forgot your password: text display Failed");
				Assert.assertEquals("Forgot your password: text display", true,
						b6);
			} else {
				logger.info("Forgot your password: text display passed");
			}
		} catch (Exception e) {
			logger.info("Forgot your password: text display Failed");
			Assert.assertEquals("Forgot your password: text display", true,
					false);
		}
	}

	@Test()
	public void testDidntRcvConfirmationText() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b7 = webDriver.findElement(By.xpath("//a[2]")).getText()
					.contentEquals("Didn't receive confirmation instructions?");
			if (b7 == false) {
				logger.info("Didn't receive confirmation instructions: text display Failed");
				Assert.assertEquals(
						"Didn't receive confirmation instructions: text display",
						true, b7);
			} else {
				logger.info("Didn't receive confirmation instructions: text display passed");
			}
		} catch (Exception e) {
			logger.info("Didn't receive confirmation instructions: text display Failed");
			Assert.assertEquals(
					"Didn't receive confirmation instructions: text display",
					true, false);
		}
	}

	@Test()
	public void testEmailTextField() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b8 = (webDriver.findElements(By.xpath("//div/input"))
					.size() != 0);
			if (b8 == false) {
				logger.info("Email Text Field display Failed");
				Assert.assertEquals("Email Text Field display", true, b8);
			} else {
				logger.info("Email Text Field display passed");
			}
		} catch (Exception e) {
			logger.info("Email Text Field display Failed");
			Assert.assertEquals("Email Text Field display", true, false);
		}
	}

	@Test()
	public void testPasswordTextField() throws Exception {
		webDriver.get(nrbyUrl + "/");
		try {
			boolean b9 = (webDriver.findElements(
					By.xpath("//section[2]/div/input")).size() != 0);
			if (b9 == false) {
				logger.info("Password Text Field display Failed");
				Assert.assertEquals("Password Text Field display", true, b9);
			} else {
				logger.info("Password Text Field display passed");
			}
		} catch (Exception e) {
			logger.info("Password Text Field display Failed");
			Assert.assertEquals("Password Text Field display", true, false);
		}
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