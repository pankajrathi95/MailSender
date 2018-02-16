package com.hemanthworks.jenkinsreport;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeDuration implements HtmlElement
{
	private String timestamp;
	
	public TimeDuration ()
    {
	    // TODO Auto-generated constructor stub
    }
	
	/**
	 * @return the timestamp
	 */
	public String getTimestamp ()
	{
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp ( String timestamp )
	{
		this.timestamp = timestamp;
	}
	
	public String getLastRunDate()
	{
		if(timestamp != null)
		{
			Date date = new Date(Long.valueOf(timestamp));
			Calendar calender = Calendar.getInstance();
			calender.setTime(date);
			LocalDate today = LocalDate.now();
			LocalDate lastRun = LocalDate.of(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH)+1, calender.get(Calendar.DATE));
			long days = ChronoUnit.DAYS.between(lastRun, today);
			if(days == 0)
			{
				return "Today";
			}
			else if(days == 1)
			{
				return "Yesterday";
			}
			else
			{
				return String.valueOf(days) + " days";
			}
		}
		else
		{
			return "N/A";
		}
		
	}
	
	@Override
	public String toHtml()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("<td>").append(this.getLastRunDate()).append("</td>");
		return builder.toString();
	}
	
	
}
