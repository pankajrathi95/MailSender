package com.hemanthworks.jenkinsreport;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestReport implements HtmlElement
{
	private int failCount;

	private int passCount;

	private int skipCount;

	public int getFailCount ()
	{
		return failCount;
	}

	public void setFailCount ( int failCount )
	{
		this.failCount = failCount;
	}

	public int getPassCount ()
	{
		return passCount;
	}

	public void setPassCount ( int passCount )
	{
		this.passCount = passCount;
	}

	public int getSkipCount ()
	{
		return skipCount;
	}

	public void setSkipCount ( int skipCount )
	{
		this.skipCount = skipCount;
	}
	
	@Override
	public String toHtml()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("<td>").append(this.passCount).append("</td>");
		builder.append("<td>").append(this.failCount).append("</td>");
		builder.append("<td>").append(this.skipCount).append("</td>");
		return builder.toString();
	}
}
