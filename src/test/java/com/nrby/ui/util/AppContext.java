package com.nrby.ui.util;

import org.openqa.selenium.WebDriver;

public final class AppContext {
	
	private static WebDriver webDriver;

	public static WebDriver getWebDriver() {
		return webDriver;
	}

	public static void setWebDriver(WebDriver webDriver) {
		AppContext.webDriver = webDriver;
	}
	
	
}
