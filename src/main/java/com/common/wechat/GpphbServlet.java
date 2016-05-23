package com.common.wechat;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.apache.log4j.Logger;

import com.bgj.exception.KLineException;
import com.bgj.strategy.StrategyMgr;
import com.bgj.strategy.StrategyQueryStockBean;
import com.bgj.util.DateUtil;
import com.bgj.util.MathUtil;
import com.bgj.util.StockMarketUtil;

public class GpphbServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int INPUT_BUF_SIZE = 512;
	public static Logger logger = Logger.getLogger(GpphbServlet.class);

	protected WxMpConfigStorage wxMpConfigStorage;
	protected WxMpService wxMpService;
	protected WxMpMessageRouter wxMpMessageRouter;

	public void init() throws ServletException {
		super.init();

		wxMpConfigStorage = new GpphbConfig();
		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage);

	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");

		if (!verifySignature(nonce, timestamp, signature)) {
			response.getWriter().println("非法请求");
			return;
		}

		String echostr = request.getParameter("echostr");

		if (StringUtils.isNotBlank(echostr)) {
			response.getWriter().println(echostr);
			return;
		}

		String encryptType = StringUtils.isBlank(request
				.getParameter("encrypt_type")) ? "raw" : request
				.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request
					.getInputStream());
			logger.info("inMessage = " + inMessage);
			String outM = "";
			if (inMessage.getMsgType().equals("text")) {
				
				outM = getStockMessage(inMessage);

			} else if (inMessage.getMsgType().equals("event")
					&& inMessage.getEvent().equals("CLICK")) {
				outM = getEventMessage(inMessage);
			} else if (inMessage.getMsgType().equals("event")
					&& inMessage.getEvent().equals("subscribe")) {
				String welcome = "欢迎订阅股票榜中榜!\n\n"
						+ "用户可以点击本公众账号的菜单获取常见股票榜单，如涨跌停榜，一字榜，历史新高等。\n\n"
						+ "更为重要重要的是，用户可以随时查阅历史榜单。";
				outM = welcome;
			}

			WxMpXmlOutMessage outMessage = WxMpXmlOutMessage.TEXT()
					.content(outM).fromUser(inMessage.getToUserName())
					.toUser(inMessage.getFromUserName()).build();
			response.getWriter().write(outMessage.toXml());
			return;
		}
		response.getWriter().println("不可识别的加密类型");
		return;
	}

	private String getEventMessage(WxMpXmlMessage inMessage) {
		String eventKey = inMessage.getEventKey();
		String result = "Button Testing";
		if (eventKey.indexOf("MRZT") == 0) {
			if (eventKey.indexOf("ZJJYRZT") > 0) {
				return getStockMessage(
						"MRZT "
								+ DateUtil.formatDay(DateUtil
										.getLatestMarketCloseDay()),
						inMessage.getFromUserName());
			}
			if (eventKey.indexOf("ZJQYJYRZT") > 0) {
				return getStockMessage(
						"MRZT "
								+ DateUtil.formatDay(DateUtil
										.getPreviousMarketOpenDay(DateUtil
												.getLatestMarketCloseDay(), 1)),
						inMessage.getFromUserName());
			}
			if (eventKey.indexOf("GDZT") > 0) {
				return "请输入 \"MRZT 日期\"请求更多涨停数据， 日期格式yyyy-MM-dd";
			}
		}
		if (eventKey.indexOf("GDBD") == 0) {
			String abbre = eventKey.substring(eventKey.indexOf("_") + 1);
			return "请输入 \""
					+ abbre
					+ " 日期\"请求"
					+ StrategyConfiguration.getInstance()
							.getStrategyBean(abbre).getName()
					+ "数据， 日期格式yyyy-MM-dd";
		}

		return result;
	}

	private String getStockMessage(WxMpXmlMessage inMessage) {
		return getStockMessage(inMessage.getContent(),
				inMessage.getFromUserName());
	}

	private String getStockMessage(String inM, String fromUserName) {

		logger.info("inM = " + inM);

		String result = "";
		if(inM.toLowerCase().startsWith("page")){
			String currentCMD = WebChatSession.getInstance().getSession(fromUserName);
			if(currentCMD == null){
				return "没有输过命令，会话未建立，请重新输入";
			}else{
				logger.info(inM);
				String sNo = inM.substring(4);
				try{
					int pageNo = Integer.parseInt(sNo);
					logger.info("pageNo = " + pageNo);
					logger.info("currentCMD = " + currentCMD);
					
					inM = currentCMD + " " + pageNo;
				}catch(NumberFormatException ex){
					return "页码格式不对， 请重新输入";
				}
			}
		}
			
		if (StrategyConfiguration.getInstance().isSupportStrategy(inM)) {
			String abbre = StrategyConfiguration.getInstance()
					.getSupportStrategyAbbre(inM);
			Date date = new Date();
			int page = 0;
			if (inM.length() > abbre.length()) {
				String sDate = inM.substring(abbre.length()).trim();
				if (sDate.indexOf(" ") > 0) {
					String sPage = sDate.substring(sDate.indexOf(" ")).trim();
					try {
						page = Integer.parseInt(sPage);
					} catch (NumberFormatException ex) {
						return "页码格式不对， 请重输";
					}
					sDate = sDate.substring(0, sDate.indexOf(" "));
				}

				try {
					date = DateUtil.parseDay(sDate);
				} catch (RuntimeException ex) {
					return "日期格式不对， 请输入：yyyy-MM-dd";
				}
			}
			String cause = StockMarketUtil.getMarketRestCause(date);
			if (cause != null) {
				return cause;
			}
			if (DateUtil.isToday(date)
					&& StockMarketUtil.isBeforeMarketAfternoonClosing()) {
				return "还未收市，本账号只提供收市后数据";
			}
			try {
				StrategyMgr strategyMgr = StrategyConfiguration.getInstance()
						.getStrategyMgr(abbre);
				List<StrategyQueryStockBean> list = strategyMgr.queryStocks(
						date, abbre);
				if (list.size() == 0) {
					return "今天没有"
							+ StrategyConfiguration.getInstance()
									.getStrategyBean(abbre).getName() + "数据";
				}
				result = convertWetChatMessage(list, page, abbre, date);
				WebChatSession.getInstance().putSession(fromUserName, inM);
			} catch (KLineException e) {
				logger.error("Read Stock throw exception:", e);
			}
		} else {
			result = "伙计,不知道你要干嘛,请确认你输的的是\"策略拼音缩写 日期\",当前支持如下策略(策略名称:拼音缩写)\n"
					+ StrategyConfiguration.getInstance().getStrategyDes();
		}
		return result;
	}

	private final static int WEB_CHAT_LIMITIATION = 20;

	private String convertWetChatMessage(List<StrategyQueryStockBean> list,
			int page, String strategy, Date date) {
		String outM = "";
		int pageCount = 1;
		if (list.size() > WEB_CHAT_LIMITIATION) {
			pageCount = list.size() / WEB_CHAT_LIMITIATION;
			if (list.size() % WEB_CHAT_LIMITIATION > 0) {
				pageCount++;
			}
		}
		if (page >= pageCount) {
			return "这个策略在" + DateUtil.formatDay(date) + "没有第" + page + "页数据";
		}

		int currentNumber = list.size();
		if (pageCount > 1) {
			if ((page + 1) < pageCount) {
				currentNumber = WEB_CHAT_LIMITIATION * (page + 1);
			}
		}

		for (int i = page * WEB_CHAT_LIMITIATION; i < currentNumber; i++) {
			StrategyQueryStockBean bean = list.get(i);
			// http://m.money.163.com/stock/0600036.html
			// http://m.quote.eastmoney.com/stock,600162.shtml
			// http://s.m.sohu.com/t/cn/001/300001.html
			// http://m.hexun.com/stock.php?code=156
			double leijiPercentage = MathUtil.formatDoubleWith2((bean
					.getLatestSpj() - bean.getDqj()) / bean.getDqj() * 100);

			StringBuffer stockInfo = new StringBuffer();
			stockInfo
					.append(bean.getStockId())
					.append(" ")
					.append("<a href=\"http://m.quote.eastmoney.com/stock,"
							+ bean.getStockId() + ".shtml\">")
					.append(bean.getName()).append("</a>").append(" ")
					.append(leijiPercentage + "%").append("\n");
			outM += stockInfo;
		}
		String header = StrategyConfiguration.getInstance()
				.getStrategyBean(strategy).getName()
				+ "(" + DateUtil.formatDay(date) + "):\n";
		header += "代码 名称 当日价 累计涨幅\n";
		outM = header + outM;

		if ((page + 1) < pageCount) {
			// outM += ("未完待续，请求下一页请输入:\n" + strategy + " "
			// + DateUtil.formatDay(date) + " " + (page + 1));
			outM += ("未完待续，请求下一页请输入:" + " page" + (page + 1));
		}
		return outM;
	}

	private boolean verifySignature(String nonce, String timestamp,
			String signature) {
		List<String> list = new ArrayList<String>();
		String token = wxMpConfigStorage.getToken();

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
			logger.error("SHA throw:", e);
		}
		return sha.equals(signature);
	}

	private String sha1(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

	public void destroy() {
		super.destroy();
	}

}