package net.dryuf.text.markdown.pegdown.test;

import net.dryuf.text.markdown.pegdown.PegDownMarkdownService;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;

public class PegDownMarkdownServiceTest
{
	@Test
	public void			testConvertToXhtml()
	{
		PegDownMarkdownService markdownService = new PegDownMarkdownService();
		Assert.assertEquals("<h1>Hello</h1>", markdownService.convertToXhtml("# Hello\n").trim());
	}
}
