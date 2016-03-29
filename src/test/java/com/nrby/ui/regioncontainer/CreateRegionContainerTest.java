package com.nrby.ui.regioncontainer;

import java.io.InputStream;
import java.lang.reflect.Method;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nrby.ui.util.AppContext;

public class CreateRegionContainerTest {
	private static Logger logger = Logger
			.getLogger("CreateRegionContainerTest");
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
		login();
		Thread.sleep(1000);
	}

	public void login() throws Exception {
		webDriver.get(nrbyUrl + "/");
		Thread.sleep(1000);
		webDriver.findElement(By.xpath("//div/input")).clear();
		webDriver.findElement(By.xpath("//div/input")).sendKeys(this.userName);
		webDriver.findElement(By.xpath("//section[2]/div/input")).clear();
		webDriver.findElement(By.xpath("//section[2]/div/input")).sendKeys(
				this.password);
		webDriver.findElement(By.xpath("//section/input")).click();
		Thread.sleep(2000);
	}

	// Creating Region Container
	@Test(dataProvider = "getData")
	public void testCreateRegionContainer(String regionContainerName)
			throws Exception {
		Thread.sleep(5000);
		try {
			String firstVerification = webDriver.findElement(
					By.xpath("//div[2]/div/div")).getText();
			int firstCount = 0;
			while (firstVerification.contains(regionContainerName)) {
				firstCount++;
				firstVerification = firstVerification
						.substring(firstVerification
								.indexOf(regionContainerName)
								+ regionContainerName.length());
			}
			// System.out.println("Initial reg count" + firstCount);
			webDriver.findElement(By.xpath("//button")).click();
			Thread.sleep(1000);
			webDriver.findElement(By.xpath("//input")).clear();
			webDriver.findElement(By.xpath("//input")).sendKeys(
					regionContainerName);
			webDriver.findElement(By.xpath("//div/span[2]")).click();
			Thread.sleep(5000);
			webDriver.navigate().refresh();
			Thread.sleep(5000);
			String we = webDriver.findElement(By.xpath("//div[2]/div/div"))
					.getText();
			int secondCount = 0;
			while (we.contains(regionContainerName)) {
				secondCount++;
				we = we.substring(we.indexOf(regionContainerName)
						+ regionContainerName.length());
			}
			// System.out.println("Second Count of final:" + secondCount);
			if (secondCount > firstCount) {
				logger.info("Region Container creation passed");
			} else {
				logger.info("Region Container creation Failed");
				Assert.assertTrue(false);
			}
		} catch (Exception e) {
			logger.info("Region Container creation Failed");
			Assert.assertTrue(false);
		}

	}

	// Create Region Container(New folder) button exists or not
	@Test()
	public void testNewFolderButton() throws Exception {
		try {
			boolean b1 = webDriver.findElement(By.xpath("//button"))
					.isDisplayed();
			if (b1 == false) {
				logger.info("New Folder button display Failed");
				Assert.assertTrue(false);
			} else {
				logger.info("New Folder button display passed");
			}
		} catch (Exception e) {
			logger.info("New Folder button display Failed");
			Assert.assertTrue(false);
		}
	}

	// Save button exists or not
	@Test()
	public void testSaveButton() throws Exception {
		Thread.sleep(5000);
		try {
			webDriver.findElement(By.xpath("//button")).click();
			Thread.sleep(2000);
			boolean b1 = webDriver.findElement(By.xpath("//div/span[2]"))
					.isDisplayed();
			if (b1 == false) {
				logger.info("Save button display Failed");
				Assert.assertTrue(false);
			} else {
				logger.info("Save button display passed");
			}
		} catch (Exception e) {
			logger.info("Save button display Failed");
			Assert.assertTrue(false);
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