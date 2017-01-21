package com.common.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.bgj.autojobs.BgjAutoQuartzServer;
import com.bgj.exception.KLineAppException;
import com.bgj.util.StrateFilePath;
import com.common.auth.AuthenticationResultBean;
import com.common.db.ConnectionPool;
import com.common.exception.CommonException;
import com.common.exception.DataFormatInvalidException;
import com.common.exception.ExceptionKeys;

public class KLineServlet extends HttpServlet {

	public static final int INPUT_BUF_SIZE = 512;
	private static Logger logger = Logger.getLogger(KLineServlet.class);

	public void init() throws ServletException {
		super.init();
		String db_url = this.getServletConfig().getInitParameter("DB_URL");
		ConnectionPool.setDBURL(db_url);
		String stockPath = this.getServletConfig().getInitParameter(
				"Stock_Info");
		StrateFilePath.getInstance().setRootPath(stockPath);
		logger.info("Kline start the QuartzJobs");
		BgjAutoQuartzServer.getInstance().startJob();
		logger.info("Kline start the QuartzJobs done");

	}

	private String readAll(InputStream in, String encoding) throws IOException {
		final byte[] buffer = new byte[INPUT_BUF_SIZE];
		ByteArrayOutputStream bos = null;
		while (true) {
			int count = in.read(buffer);
			if (count < 0) { // EOF
				break;
			}
			/*
			 * Let's create buffer lazily, to be able to create something that's
			 * not too small (many resizes) or too big (slower to allocate):
			 * mostly to speed up handling of tiny docs.
			 */
			if (bos == null) {
				int cap;
				if (count < 64) {
					cap = 64;
				} else if (count == INPUT_BUF_SIZE) {
					// Let's assume there's more coming, not just this chunk
					cap = INPUT_BUF_SIZE * 4;
				} else {
					cap = count;
				}
				bos = new ByteArrayOutputStream(cap);
			}
			bos.write(buffer, 0, count);
		}
		return (bos == null) ? "" : bos.toString(encoding);
	}

	private final static String TOKEN = "M2E0M2IzYzItODNiMi00YTc0LTliZDE";

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String signature = req.getParameter("signature");
		logger.info("signature = " + signature);

		if (signature != null) {
			String timestamp = req.getParameter("timestamp");
			logger.info("timestamp = " + timestamp);

			String nonce = req.getParameter("nonce");
			logger.info("nonce = " + nonce);

			String token = TOKEN;
			logger.info("token = " + token);

			String echostr = req.getParameter("echostr");
			logger.info("echostr = " + echostr);

			List<String> list = new ArrayList<String>();
			list.add(token);
			list.add(timestamp);
			list.add(nonce);
			Collections.sort(list);
			String verifyInfo = "";
			for (int i = 0; i < list.size(); i++) {
				verifyInfo = (verifyInfo + list.get(i));
			}
			String sha = "";
			try {
				sha = sha1(verifyInfo);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			logger.info("sha = " + sha);

			if(sha.equals(signature)){
				res.getWriter().write(echostr);
				res.getWriter().flush();
			}
		} else {
			doPost(req, res);
		}
	}

	public static String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		BasicResponse basicResponse = new BasicResponse();
		try {
			InputStream is = req.getInputStream();
			String json = readAll(is, "UTF-8");
			
			logger.info("KLince receive POST request message = " + json);

			JSONObject obj1 = null;
			try {
				obj1 = new JSONObject(json);
			} catch (JSONException je) {
				je.printStackTrace();
				basicResponse.setStatus(false);
				basicResponse.setErrorKey(ExceptionKeys.JSON_FORMAT_ERROR);
				basicResponse.setMsg("Json Format Error : " + je.getMessage());
				sendResponse(res, basicResponse);
				return;
			}
			JSONObject rqstJO = obj1.getJSONObject("Rqst");
			if (rqstJO == null) {
				basicResponse.setStatus(false);
				basicResponse.setErrorKey(ExceptionKeys.JSON_NO_RQST);
				basicResponse.setMsg("No Rqst in request");
				sendResponse(res, basicResponse);
				return;
			}
			String job = rqstJO.getString("Job");
			if (job == null || job.trim().equals("")) {
				basicResponse.setStatus(false);
				basicResponse.setErrorKey(ExceptionKeys.JSON_NO_JOB);
				basicResponse.setMsg("No Job in request");
				sendResponse(res, basicResponse);
				return;
			}
			String needAuthentication = (String) BeanConfiguration
					.getInstance().getProperty(job + ".authentication");
			if (needAuthentication == null) {
				basicResponse.setStatus(false);
				basicResponse.setErrorKey(ExceptionKeys.INVALID_JOB);
				basicResponse.setMsg("Invalid job in request");
				sendResponse(res, basicResponse);
				return;
			} else if (needAuthentication.equals("true")) {
				String ticket = rqstJO.getString("KLineTicket");
				if (ticket == null || ticket.equals("")) {
					basicResponse.setStatus(false);
					basicResponse.setErrorKey(ExceptionKeys.NO_KLINE_TICKET);
					basicResponse.setMsg("KLineTicket is null");
					sendResponse(res, basicResponse);
					return;
				}
				Token token = null;
				try {
					token = TokenUtil.getToken(ticket);
					RequestContext.putToken(token);
				} catch (KLineAppException ex) {
					// ex.printStackTrace();
					basicResponse.setStatus(false);
					basicResponse
							.setErrorKey(ExceptionKeys.INVALID_KLINE_TICKET);
					basicResponse.setMsg("KLineTicket is invalid");
					sendResponse(res, basicResponse);
					return;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (!job.equals("UserLogout")) {
				String beanClassName = BeanConfiguration.getInstance()
						.getProperty(job);
				String mgrClassName = BeanConfiguration.getInstance()
						.getProperty(job + ".mgr");
				String jobMethod = BeanConfiguration.getInstance().getProperty(
						job + ".api");
				Class mgr = Class.forName(mgrClassName);

				Method getInstance = mgr.getMethod("getInstance", null);

				String inputClass = BeanConfiguration.getInstance()
						.getProperty(job);
				Object input = null;
				Class[] inputClasses = null;
				Object[] inputObjects = null;
				if (inputClass != null) {
					JSONObject dataJO = rqstJO.getJSONObject("Data");
					input = JsonUtil.converJsonToBean(dataJO, beanClassName);
					inputClasses = new Class[] { input.getClass() };
					inputObjects = new Object[] { input };
				} else {
					inputClasses = new Class[] {};
					inputObjects = new Object[] {};
				}

				Method jobApi = mgr.getMethod(jobMethod, inputClasses);
				Object mgrImpl = getInstance.invoke(null, null);
				Object result = jobApi.invoke(mgrImpl, inputObjects);
				if (result instanceof String || result instanceof Integer
						|| result instanceof Long || result instanceof Boolean
						|| result instanceof Byte || result instanceof Double
						|| result instanceof Date) {
					SingleResult temp = new SingleResult();
					temp.setResult(result);
					result = temp;
				}
				basicResponse.setData(result);
				if (result instanceof AuthenticationResultBean) {
					String credential = ((AuthenticationResultBean) result)
							.getkLineTicket();
					((AuthenticationResultBean) result).setkLineTicket(null);
					basicResponse.setKLineTicket(credential);
				} else {
					if ("true".equals(needAuthentication)) {
						Token token = RequestContext.getToken();
						RequestContext.cleanToken();
						String ticket = TokenUtil.getTicket(token);
						basicResponse.setKLineTicket(ticket);
					}
				}
			}
			basicResponse.setJob(job);
		} catch (Exception ex) {
			if (ex instanceof InvocationTargetException) {
				ex = (Exception) ((InvocationTargetException) ex)
						.getTargetException();
			}
			if (ex instanceof CommonException) {
				if (ex instanceof DataFormatInvalidException) {
					String field = ((DataFormatInvalidException) ex).getField();
					String type = ((DataFormatInvalidException) ex).getType();
					basicResponse.setAdditionalInfo("The field [" + field
							+ "] should be " + type);
				}
				basicResponse.setErrorKey(ex.getMessage());
			} else if (ex instanceof KLineAppException) {
				basicResponse.setErrorKey(ex.getMessage());
			} else {
				StackTraceElement[] traces = ex.getStackTrace();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < traces.length; i++) {
					sb.append("at " + traces[i].getClassName() + "."
							+ traces[i].getMethodName() + "("
							+ traces[i].getFileName()
							+ traces[i].getLineNumber() + ")");
					sb.append('\n');
				}
				basicResponse.setMsg(ex.getClass().getName() + " : "
						+ ex.getMessage() + " \n " + " , Exception trace : "
						+ sb.toString());
			}
			basicResponse.setStatus(false);
		}
		sendResponse(res, basicResponse);
	}

	private void sendResponse(HttpServletResponse res,
			BasicResponse basicResponse) {
		try {
			Response response = new Response();
			response.setRsps(basicResponse);
			JSONObject obj = new JSONObject(response, true);
			res.setCharacterEncoding("UTF-8");
			Writer writer = res.getWriter();
			writer.write(obj.toString(1));
			writer.flush();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void destroy() {
		super.destroy();
		BgjAutoQuartzServer.getInstance().stopJob();
	}

}