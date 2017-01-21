package com.bgj.autojobs;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

public class BgjAutoQuartzServer {
	private static Logger logger = Logger.getLogger(BgjAutoQuartzServer.class);

	private static BgjAutoQuartzServer instance = new BgjAutoQuartzServer();

	public static BgjAutoQuartzServer getInstance() {
		return instance;
	}

	private Scheduler scheduler = null;

	private void scheduleJob(int hour, int mins, String jobName, Class jobClass) {
		try {
			JobDetail job = new JobDetail(jobName + "Job",
					Scheduler.DEFAULT_GROUP, jobClass);
			Trigger trigger = TriggerUtils.makeDailyTrigger(jobName + "Triger",
					hour, mins);
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException ex) {
			logger.error("Make auto job : " + jobName + "throw: ", ex);
		}
	}

	private BgjAutoQuartzServer() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduleJob(15, 1, "AutoCollectingAfterCloseJob",
					AutoCollectingAfterCloseJob.class);
			for(int i=30; i < 59; i++){
				scheduleJob(9, i, "AutoCollectingPanzhongJob" + i,
						AutoCollectingPanzhongJob.class);
			}
			for(int i=0; i < 59; i++){
				scheduleJob(10, i, "AutoCollectingPanzhongJob" + i,
						AutoCollectingPanzhongJob.class);
			}
		} catch (SchedulerException ex) {
			logger.error("Make auto trade scheduler throw: ", ex);
		}
	}

	public void startJob() {
		try {
			System.out.println("start the quartz job");
			scheduler.start();
			System.out.println("start the quartz job done");

		} catch (SchedulerException ex) {
			ex.printStackTrace();
			logger.error("Start AutoJob throw: ", ex);
		}
	}

	public void stopJob() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException ex) {
			logger.error("Stop AutoJob throw: ", ex);
		}
	}
	
	public static void main(String[] args){
		BgjAutoQuartzServer.getInstance().startJob();
		BgjAutoQuartzServer.getInstance().stopJob();
		
	}
}