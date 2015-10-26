package net.dryuf.text.markdown.test;

import net.dryuf.text.markdown.NewLineMarkdownService;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;


public class NewLineMarkdownServiceTest
{
	@Test
	public void			testConvertToXhtml()
	{
		NewLineMarkdownService markdownService = new NewLineMarkdownService();
		Assert.assertEquals("Hello<br/>\nworld", markdownService.convertToXhtml("Hello\nworld\n").trim());
	}
}
