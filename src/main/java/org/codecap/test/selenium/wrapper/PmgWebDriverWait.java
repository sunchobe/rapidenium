package org.codecap.test.selenium.wrapper;


import com.google.common.base.Function;

import org.codecap.test.selenium.element.Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class PmgWebDriverWait<P extends Page> {

	private final WebDriverWait webDriverWait;

	public PmgWebDriverWait(PmgWebDriver webDriver, int timeOutInSeconds) {
		this.webDriverWait = new WebDriverWait(webDriver.webDriver, timeOutInSeconds);
	}

	public FluentWait<WebDriver> withTimeout(long duration, TimeUnit unit) {
		return this.webDriverWait.withTimeout(duration, unit);
	}

	public FluentWait<WebDriver> pollingEvery(long duration, TimeUnit unit) {
		return this.webDriverWait.pollingEvery(duration, unit);
	}

	public FluentWait<WebDriver> ignoring(Class<? extends RuntimeException> types) {
		return this.webDriverWait.ignoring(types);
	}

	public <V> V until(final PmgExpectedPageCondition<V, P> isTrue) {

		return this.webDriverWait.until(new Function<WebDriver, V>() {

			public V apply(WebDriver input) {
				return isTrue.apply(isTrue.getPage());
			}

		});
	}
}
