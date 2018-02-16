package com.hemanthworks.jenkinsreport;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class Jobs
{
	private String description;

	private List<Job> jobs;

	public String getDescription ()
	{
		return description;
	}

	public void setDescription ( String description )
	{
		this.description = description;
	}

	public List<Job> getJobs ()
	{
		return jobs;
	}

	public void setJobs ( List<Job> jobs )
	{
		this.jobs = jobs;
	}

}
