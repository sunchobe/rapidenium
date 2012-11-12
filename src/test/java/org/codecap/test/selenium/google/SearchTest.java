package org.codecap.test.selenium.google;

import org.codecap.test.selenium.AbstractSeleniumTest;
import org.codecap.test.selenium.google.page.GooglePage;
import org.junit.Assert;
import org.junit.Test;

public class SearchTest extends AbstractSeleniumTest {

	@Override
	protected String getBaseResourcePath() {
		return "/";
	}

	@Test
	public void testSomething() {
		final GooglePage page = new GooglePage(goToPage(""));

		Assert.assertTrue(page.isValid());

		page.enterSearchString("Selenium");
		page.clickSearchButton();
	}
}
