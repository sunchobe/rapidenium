package org.codecap.test.selenium.google.page;

import java.util.ArrayList;
import java.util.List;

import org.codecap.test.selenium.element.Page;
import org.codecap.test.selenium.element.PageElement;
import org.codecap.test.selenium.wrapper.PmgWebDriver;

public class GooglePage extends Page {

	private PageElement searchField;
	private PageElement searchButton;

	public GooglePage(final PmgWebDriver pageInformation) {
		super(pageInformation);
	}

	@Override
	protected List<PageElement> getDefinedPageElements() {
		final List<PageElement> pageElementList = new ArrayList<PageElement>();

		pageElementList.add(this.searchField);
		pageElementList.add(this.searchButton);

		return pageElementList;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	protected void definePageElements() {
		this.searchField = new PageElement("gbqfq");
		this.searchButton = new PageElement("gbqfb");
	}

	public void enterSearchString(final String searchString) {
		input(searchString, this.searchField);
	}

	public GooglePage clickSearchButton() {
		click(this.searchButton);
		return new GooglePage(this);
	}
}