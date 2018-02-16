package com.hemanthworks.jenkinsreport;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestJob extends Job
{
	private TestReport testReport;
	
	public TestReport getTestReport()
	{
		return testReport;
	}
	
	public void setTestReport(TestReport testReport)
	{
		this.testReport = testReport;
	}
	
	@Override
	public String toHtml()
	{
		 StringBuilder builder = new StringBuilder();
		 builder.append("<tr class='"+ this.getStatus() +"'>");
		 builder.append("<td> <a href='"+this.getUrl()+"'>" + this.getName() + "</a> </td>");
		 builder.append(this.testReport.toHtml());
		 builder.append(super.getLastStableTimeDuration().toHtml());
		 builder.append(super.getLastFailedTimeDuration().toHtml());
		 builder.append("<td>"+ this.type + "</td>");
		 builder.append("</tr>");
		 return builder.toString();
	}
}