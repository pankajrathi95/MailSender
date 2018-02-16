package com.hemanthworks.jenkinsreport;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BuildStreetReport implements HtmlElement
{
	private List<TestJob> passedTestJobs;
	private List<TestJob> unstableTestJobs;
	
	private List<Job> failedJobs;
	private List<Job> disabledJobs;
	private List<Job> abortedJobs;
	private List<Job> allJobs;
	
	private JenkinsClient client;
	
	public BuildStreetReport(List<Job> allJobs, JenkinsClient client)
	{
				
		this.failedJobs = getFilteredJobs(allJobs, job -> {
			return job.getStatus().equals(JobStatus.FAILED);
		});
		
		this.disabledJobs = getFilteredJobs(allJobs, job -> {
			return job.getStatus().equals(JobStatus.DISABLED);
		});
		
		this.abortedJobs = getFilteredJobs(allJobs, job -> {
			return job.getStatus().equals(JobStatus.ABORTED);
		});	
		
		this.client = client;
		this.allJobs = allJobs;
	}
	
	public void prepareReport()
	{
		List<Job> passedJobs = getFilteredJobs(this.allJobs, job -> {
			return job.getStatus().equals(JobStatus.PASSED);
		});
		
		this.passedTestJobs = client.getTestJobs(passedJobs);
		
		List<Job> unstableJobs = getFilteredJobs(this.allJobs, job -> {
			return job.getStatus().equals(JobStatus.UNSTABLE);
		});
		
		this.unstableTestJobs = client.getTestJobs(unstableJobs);
		
		
	}
	
	/**
	 * @return the passedJobs
	 */
	public List<TestJob> getPassedJobs ()
	{
		return passedTestJobs;
	}

	/**
	 * @return the failedJobs
	 */
	public List<Job> getFailedJobs ()
	{
		return failedJobs;
	}

	/**
	 * @return the unstableJobs
	 */
	public List<TestJob> getUnstableJobs ()
	{
		return unstableTestJobs;
	}

	/**
	 * @return the abortedJobs
	 */
	public List<Job> getAbortedJobs ()
	{
		return disabledJobs;
	}

	public List<Job> getFilteredJobs(List<Job> allJobs , Predicate<Job> predicate)
	{
		return allJobs.stream().filter(predicate).collect(Collectors.toList());
	}

	@Override
    public String toHtml ()
    {
	   StringBuilder builder = new StringBuilder();
	   builder.append("<style> h2 {font-family: arial, sans-serif} table { font-family: arial, sans-serif;border-collapse: collapse;width: 100%;}td, th {border: 1px solid #dddddd;text-align: left;padding: 8px;} .failed {background-color: #FA8072;} .unstable {background-color: #F3F322;} .disabled {background-color: #CFCFC8;} .aborted {background-color: #CFCFC8;} .passed {background-color: #11A964;}</style>");
	   builder.append("<p> Good Morning. </p> <br />");
	   if(this.failedJobs.size() > 0)
	   {
		   builder.append("<h2> Failed jobs </h2>");
		   builder.append("<table>");
		   builder.append(getTableHeaderForUnsuccessFulJobs());
		   this.failedJobs.stream().forEach( failedjob -> {
			   builder.append(failedjob.toHtml());
		   });
		   builder.append("</table>");
	   }
	   
	   if(this.unstableTestJobs.size() > 0)
	   {
		   builder.append("<h2> Unstable jobs </h2>");
		   builder.append("<table>");
		   builder.append(getTableHeader());
		   this.unstableTestJobs.stream().forEach( unstableJob -> {
			   builder.append(unstableJob.toHtml());
		   });
		   builder.append("</table>");
	   }
	   
	   if(this.passedTestJobs.size() > 0)
	   {
		   builder.append("<h2> Passed jobs </h2>");
		   builder.append("<table>");
		   builder.append(getTableHeader());
		   this.passedTestJobs.stream().forEach( passedJob -> {
			   builder.append(passedJob.toHtml());
		   });
		   builder.append("</table>");
	   }
	  
	   if(this.disabledJobs.size() > 0)
	   {
		   builder.append("<h2> Disabled jobs </h2>");
		   builder.append("<table>");
		   builder.append(getTableHeaderForUnsuccessFulJobs());
		   this.disabledJobs.stream().forEach( disabledJob -> {
			   builder.append(disabledJob.toHtml());
		   });
		   builder.append("</table>");
	   }
	   
	   if(this.abortedJobs.size() > 0)
	   {
		   builder.append("<h2> Aborted jobs </h2>");
		   builder.append("<table>");
		   builder.append(getTableHeaderForUnsuccessFulJobs());
		   this.abortedJobs.stream().forEach( abortedJob -> {
			   builder.append(abortedJob.toHtml());
		   });
		   builder.append("</table>");
	   }
	   
	   return builder.toString();
    }
	
	
	private String getTableHeader()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("<tr>");
		builder.append("<th>").append("Name").append("</th>");
		builder.append("<th>").append("Passed").append("</th>");
	    builder.append("<th>").append("Failed").append("</th>");
		builder.append("<th>").append("Skiped").append("</th>");
		builder.append("<th>").append("Last Successful").append("</th>");
		builder.append("<th>").append("Last Failure").append("</th>");
		builder.append("<th>").append("Owner").append("</th>");
		builder.append("</tr>");
		return builder.toString();
	}
	
	private String getTableHeaderForUnsuccessFulJobs()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("<tr>");
		builder.append("<th>").append("Name").append("</th>");
		builder.append("<th>").append("Last Successful").append("</th>");
		builder.append("<th>").append("Last Failure").append("</th>");
		builder.append("<th>").append("Owner").append("</th>");
		builder.append("</tr>");
		return builder.toString();
	}
}
