package com.nrby.ui.regions;

import java.io.InputStream;
import java.lang.reflect.Method;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
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

		webDriver.findElement(By.xpath("//*[@name='user[email]']")).clear();
		Thread.sleep(500);
		webDriver.findElement(By.xpath("//*[@name='user[email]']")).sendKeys(
				this.userName);
		Thread.sleep(500);
		webDriver.findElement(By.xpath("//*[@name='user[password]']")).clear();
		Thread.sleep(500);
		webDriver.findElement(By.xpath("//*[@name='user[password]']"))
				.sendKeys(this.password);
		Thread.sleep(500);
		webDriver.findElement(By.xpath("//*[@class='btn btn-primary']"))
				.click();

	}

	// Creating a zone folder and creating a zone under a sub folder
	@Test(dataProvider = "getData")
	public void testCreateRegion(String ContName, String Tags, String RegName,
			String Location, String Address, String Radius) throws Exception {
		webDriver.findElement(By.xpath("//*[@id='new_region_container']"))
				.click(); // Create new Parent folder
		Thread.sleep(2000);
		webDriver
				.findElement(
						By.xpath("/html/body/div[1]/div/div/div[2]/div/div/form/div[3]/div[1]/div/input"))
				.sendKeys(ContName); // Provide name for new folder
		Thread.sleep(500);
		webDriver.findElement(
				By.xpath("//*[@id='region_container_tags_as_strings']"))
				.sendKeys(Tags); // Provide name for Tag
		Thread.sleep(500);
		webDriver
				.findElement(
						By.xpath("/html/body/div[1]/div/div/div[2]/div/div/form/div[4]/input"))
				.click(); // Click Create button
		Thread.sleep(4000);
		webDriver.findElement(By.xpath("//*[@id='new_region_container']"))
				.click(); // Again Create new folder, but this will be child
							// folder
		Thread.sleep(2000);
		webDriver
				.findElement(
						By.xpath("/html/body/div[1]/div/div/div[2]/div/div/form/div[3]/div[1]/div/input"))
				.sendKeys(RegName); // Provide name for new folder
		Thread.sleep(500);
		WebElement mySelectElm = webDriver.findElement(By
				.xpath("//*[@id='region_container_region_container_id']")); // Select
																			// the
																			// Parent
																			// folder
																			// name
																			// from
																			// Dropdown
		Select mySelect = new Select(mySelectElm);
		mySelect.selectByVisibleText(ContName);
		Thread.sleep(1000);
		webDriver
				.findElement(
						By.xpath("/html/body/div[1]/div/div/div[2]/div/div/form/div[4]/input"))
				.click(); // Create folder
		Thread.sleep(5000);
		webDriver.findElement(By.xpath("//*[@id='new_region']")).click(); // Create
																			// new
																			// region
		Thread.sleep(3000);
		webDriver.findElement(By.xpath("//*[@id='address_to_find']")).sendKeys(
				Location); // Provide location name to lookup
		Thread.sleep(500);
		webDriver
				.findElement(
						By.xpath("/html/body/div/div/div/section[2]/div/div[2]/div/input[2]"))
				.click(); // Click lookup button
		Thread.sleep(1000);
		webDriver.findElement(By.xpath("//*[@id='region_name']")).sendKeys(
				RegName); // Provide zone name
		Thread.sleep(500);
		WebElement mySelectElm1 = webDriver.findElement(By
				.xpath("//*[@id='region_region_container_id']")); // Select any folder name that we created earlier
		Select mySelect1 = new Select(mySelectElm1);
		mySelect1.selectByVisibleText(ContName);
		webDriver
				.findElement(
						By.xpath("/html/body/div/div/div/section[2]/div/div[1]/div/form/div[10]/input"))
				.click(); // Create button
		Thread.sleep(1000);
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