package com.nrby.ui.regions;

import java.io.InputStream;
import java.lang.reflect.Method;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.nrby.ui.util.AppContext;

public class CreateRegionTest {

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

	// Creating zone
	@Test(dataProvider = "getData")
	public void testCreateRegion(CharSequence zoneName,
			CharSequence zoneAddress, CharSequence zoneRadius,
			CharSequence zoneLatitude, CharSequence zoneLongitude,
			CharSequence zoneDescription, CharSequence zoneTag)
			throws Exception {
		webDriver.findElement(By.xpath("//button[3]")).click();
		Thread.sleep(500);
		webDriver.findElement(By.xpath("//button[2]")).click();
		Thread.sleep(1000);
		webDriver.findElement(By.xpath("//div/input")).clear();
		webDriver.findElement(By.xpath("//div/input")).sendKeys(zoneName);
		webDriver.findElement(By.xpath("//div[2]/input")).clear();
		webDriver.findElement(By.xpath("//div[2]/input")).sendKeys(zoneAddress);
		webDriver.findElement(By.xpath("//div[2]/div[2]/input")).clear();
		webDriver.findElement(By.xpath("//div[2]/div[2]/input")).sendKeys(
				zoneRadius);
		webDriver.findElement(By.xpath("//div[3]/div[2]/input")).clear();
		webDriver.findElement(By.xpath("//div[3]/div[2]/input")).sendKeys(
				zoneLatitude);
		webDriver.findElement(By.xpath("//div[4]/div[2]/input")).clear();
		webDriver.findElement(By.xpath("//div[4]/div[2]/input")).sendKeys(
				zoneLongitude);
		webDriver.findElement(By.xpath("//textarea")).clear();
		webDriver.findElement(By.xpath("//textarea")).sendKeys(zoneDescription);
		webDriver.findElement(By.xpath("//div[7]/div[2]/input")).clear();
		webDriver.findElement(By.xpath("//div[7]/div[2]/input")).sendKeys(
				zoneTag);
		webDriver.findElement(By.xpath("//div[8]/div/button")).click();
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