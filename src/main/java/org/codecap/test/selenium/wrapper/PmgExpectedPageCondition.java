package org.codecap.test.selenium.wrapper;


import com.google.common.base.Function;

import org.codecap.test.selenium.element.Page;
import org.openqa.selenium.WebDriver;

public abstract class PmgExpectedPageCondition<T, P extends Page> implements Function<P, T> {

	private final PmgWebDriver pmgWebDriver;
	
	public PmgExpectedPageCondition(P pmgWebDriver) {
		super();
		this.pmgWebDriver = pmgWebDriver;
	}
	
	public WebDriver getWebDriver() {
		return this.pmgWebDriver.webDriver;
	}
	
	@SuppressWarnings("unchecked")
	public P getPage() {
		return (P) this.pmgWebDriver;
	}

}
