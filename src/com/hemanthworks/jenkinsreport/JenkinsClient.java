package com.hemanthworks.jenkinsreport;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class JenkinsClient
{

	private Client client;

	private WebTarget target;

	private Response response;

	private static String BASE_URL = " ";

	private static String API = "/api/json";
	
	private static String TEST_REPORT_URL = "/lastBuild/testReport/";
	
	private static String LAST_BUILD = "/lastBuild/";
	
	private static String LAST_SUCCESSFUL_BUILD = "/lastSuccessfulBuild/";
	
	private static String LAST_FAILED_BUILD = "/lastFailedBuild/";

	private static String QUERY_PARAM_TEST_REPORT = "passCount[*[*]],failCount[*[*]],skipCount[*[*]]";
	
	private static String QUERY_PARAM_TIME_DURATION = "timestamp,duration";
	
	private static String view;
	
	private Properties jobOwners;

	public JenkinsClient (String view, Properties jobOwners)
	{
		this.jobOwners = jobOwners;
		this.view = view;
		client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
	}

	public List<Job> getViewJobs ()
	{

		target = client.target(BASE_URL).path(this.view).path(API);
		response = target.request(MediaType.APPLICATION_JSON).get();
		Jobs jobs = response.readEntity(Jobs.class);
		for(Job job : jobs.getJobs())
		{
			TimeDuration lastStableTimeduration = getTimeDuration(job.getName(), LAST_SUCCESSFUL_BUILD);
			TimeDuration lastFailedTimeduration = getTimeDuration(job.getName(), LAST_FAILED_BUILD);
			job.setLastStableTimeDuration(lastStableTimeduration);
			job.setLastFailedTimeDuration(lastFailedTimeduration);
			job.setType(jobOwners.getProperty(job.getName(), "Not Available"));
		}
		
		return jobs.getJobs();
	}
	
	public TestReport getTestReport(String jobname)
	{
		try
		{
    		target = client.target(BASE_URL).path(this.view).path("/job/").path(jobname).path(TEST_REPORT_URL).path(API).queryParam("tree", QUERY_PARAM_TEST_REPORT);
    		response = target.request(MediaType.APPLICATION_JSON).get();
    		TestReport testReport = response.readEntity(TestReport.class);
    		return testReport;
		}
		catch(Exception e)
		{
			return new TestReport();
		}
	}
	
	public TimeDuration getTimeDuration(String jobname, String buildStatus)
	{
		try
		{
    		target = client.target(BASE_URL).path(view).path("/job/").path(jobname).path(buildStatus).path(API).queryParam("tree", QUERY_PARAM_TIME_DURATION);
    		response = target.request(MediaType.APPLICATION_JSON).get();
    		TimeDuration timeduration = response.readEntity(TimeDuration.class);
    		return timeduration;
		}
		catch(Exception e)
		{
			return new TimeDuration();
		}
	}
	
	public List<TestJob> getTestJobs(List<Job> jobs)
	{
		List<TestJob> testJobs = new ArrayList<>();
		for(Job job : jobs)
		{
			TestReport testReport = getTestReport(job.getName());
			TestJob testjob = new TestJob();
			testjob.setTestReport(testReport);
			testjob.setLastStableTimeDuration(job.getLastStableTimeDuration());
			testjob.setLastFailedTimeDuration(job.getLastFailedTimeDuration());
			testjob.setName(job.getName());
			testjob.setUrl(job.getUrl());
			testjob.setColor(job.getColor());
			testjob.setType(job.getType());
			testJobs.add(testjob);	
		}
		
		return testJobs;
	}

}

