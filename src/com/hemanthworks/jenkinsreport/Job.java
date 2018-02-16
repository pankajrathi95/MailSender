package com.hemanthworks.jenkinsreport;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job implements HtmlElement
{
	protected String name;

	protected String color;

	protected String url;

	protected String type;
	
	protected String status;
	
	protected TimeDuration lastStableTimeDuration;
	
	protected TimeDuration lastFailedTimeDuration;
	
	/**
	 * @return the lastStableTimeDuration
	 */
	public TimeDuration getLastStableTimeDuration ()
	{
		return lastStableTimeDuration;
	}

	/**
	 * @param lastStableTimeDuration the lastStableTimeDuration to set
	 */
	public void setLastStableTimeDuration ( TimeDuration lastStableTimeDuration )
	{
		this.lastStableTimeDuration = lastStableTimeDuration;
	}

	/**
	 * @return the lastFailedTimeDuration
	 */
	public TimeDuration getLastFailedTimeDuration ()
	{
		return lastFailedTimeDuration;
	}

	/**
	 * @param lastFailedTimeDuration the lastFailedTimeDuration to set
	 */
	public void setLastFailedTimeDuration ( TimeDuration lastFailedTimeDuration )
	{
		this.lastFailedTimeDuration = lastFailedTimeDuration;
	}


	public Job ()
	{
		// empty constructor
	}

	public String getType ()
	{
		return type;
	}

	public void setType ( String type )
	{
		this.type = type;
	}

	public String getName ()
	{
		return name;
	}

	public void setName ( String name )
	{
		this.name = name;
	}

	public String getColor ()
	{
		return color;
	}

	public void setColor ( String color )
	{
		this.color = color;
	}

	public String getUrl ()
	{
		return url;
	}

	public void setUrl ( String url )
	{
		this.url = url;
	}
	
	public String getStatus()
	{
		if (this.color.startsWith("red"))
		{
			return JobStatus.FAILED;
		}
		else if (this.color.startsWith("blue"))
		{
			return JobStatus.PASSED;
		}
		else if (this.color.startsWith("yellow"))
		{
			return JobStatus.UNSTABLE;
		}
		else if (this.color.startsWith("disabled"))
		{
			return JobStatus.DISABLED;
		}
		else
			return JobStatus.ABORTED;
	}

	
	@Override
	public boolean equals ( Object other )
	{
		if (other instanceof Job)
		{
			Job job = (Job) other;
			if (job.getName().equals(this.getName()))
				return true;
			else
				return false;
		}
		else
		{
			return false;
		}
	}

	@Override
	public int hashCode ()
	{
		return getUrl().hashCode();
	}

	@Override
    public String toHtml ()
    {
	    StringBuilder builder = new StringBuilder();
	    builder.append("<tr class='"+ this.getStatus() +"'>");
	    builder.append("<td> <a href='"+this.getUrl()+"'>" + this.getName() + "</a> </td>");
	    builder.append(this.lastStableTimeDuration.toHtml());
	    builder.append(this.lastFailedTimeDuration.toHtml());
	    builder.append("<td>"+ this.type + "</td>");
	    builder.append("</tr>");
	    return builder.toString();
    }

}
