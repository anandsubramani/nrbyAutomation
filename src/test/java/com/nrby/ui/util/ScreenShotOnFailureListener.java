package com.nrby.ui.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class ScreenShotOnFailureListener extends TestListenerAdapter {

	@Override
	public void onTestFailure(ITestResult result) {
		captureScreenshot(result);
	}

	@Override
	public void onConfigurationFailure(ITestResult result) {
		captureScreenshot(result);
	}
	
	private void captureScreenshot(ITestResult result) {
		File file = new File("");
		Reporter.setCurrentTestResult(result);
		String fileLocation = file.getAbsolutePath()
				+ File.separator + "screenshots" + File.separator +  result.getName() + ".jpg";
		System.out.println("screenshot saved at " + fileLocation);
		
		File scrFile = ((TakesScreenshot)AppContext.getWebDriver()).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(fileLocation));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reporter.setEscapeHtml(false);
		Reporter.log("<a href='" + fileLocation + "'> <img src='"
				+ fileLocation + "' height='100' width='100'/> </a>");
		Reporter.setCurrentTestResult(null);
	}
}
