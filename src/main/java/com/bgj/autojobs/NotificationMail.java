package com.bgj.autojobs;

import java.util.Date;

import com.bgj.util.DateUtil;

public class NotificationMail {
	private boolean successful;
	private Exception failedCause;
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	public Exception getFailedCause() {
		return failedCause;
	}
	public void setFailedCause(Exception failedCause) {
		this.failedCause = failedCause;
	}
	
	public String getSubject(){
		String result = "KLineService Daily Report("+DateUtil.formatDate(new Date())+")";
		if(this.successful){
			result += "-Successfully";
		}else{
			result += "-Failed";
		}
		return result;
	}
	
	public String getBody(){
		if(this.successful){
			return "Please access http://121.40.35.139/ for more APIs";
		}else{
			String result = this.failedCause.getClass().getSimpleName() + " : " + this.failedCause.getMessage();
			StackTraceElement[] traces = failedCause.getStackTrace();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < traces.length; i++) {
				sb.append("at " + traces[i].getClassName() + "."
						+ traces[i].getMethodName() + "("
						+ traces[i].getFileName()
						+ traces[i].getLineNumber() + ")");
				sb.append('\n');
			}
			result += '\n';
			result += sb.toString();
			return result;
		}
	}

}
