package com.common.wechat;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;

import org.apache.log4j.Logger;

import com.bgj.exception.KLineException;
import com.bgj.strategy.CommonStrategyMgrImpl;
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
	private static Logger logger = Logger.getLogger(GpphbServlet.class);

	protected WxMpConfigStorage wxMpConfigStorage;
	protected WxMpService wxMpService;
	protected WxMpMessageRouter wxMpMessageRouter;

	public void init() throws ServletException {
		super.init();

		wxMpConfigStorage = new GpphbConfig();
		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage);

		WxMpMessageHandler handler = new WxMpMessageHandler() {
			@Override
			public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
					Map<String, Object> context, WxMpService wxMpService,
					WxSessionManager sessionManager) throws WxErrorException {
				WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT()
						.content("测试加密消息").fromUser(wxMessage.getToUserName())
						.toUser(wxMessage.getFromUserName()).build();
				return m;
			}
		};

		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter.rule().async(false).content("哈哈") // 拦截内容为“哈哈”的消息
				.handler(handler).end();
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
			// if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
			// 消息签名不正确，说明不是公众平台发过来的消息
			response.getWriter().println("非法请求");
			return;
		}

		String echostr = request.getParameter("echostr");
		logger.info("echostr = " + echostr);

		if (StringUtils.isNotBlank(echostr)) {
			// 说明是一个仅仅用来验证的请求，回显echostr
			response.getWriter().println(echostr);
			return;
		}

		String encryptType = StringUtils.isBlank(request
				.getParameter("encrypt_type")) ? "raw" : request
				.getParameter("encrypt_type");
		logger.info("encryptType = " + encryptType);

		if ("raw".equals(encryptType)) {
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request
					.getInputStream());
			logger.info("inMessage = " + inMessage);
			String inM = inMessage.getContent();
			String outM = "再来一次:" + inM + "\n";
			outM += "Test Test Test Test";

			outM = getStockMessage(inM, outM);
			logger.info("Out content = " + outM);
			// TextBuilder textBuild =
			// WxMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(inMessage.getToUserName()).toUser(inMessage.getFromUserName()).build();
			// textBuild = textBuild.;
			// textBuild = textBuild.;
			//
			// textBuild = textBuild;
			// WxMpXmlOutMessage outMessage = textBuild.build();

			WxMpXmlOutMessage outMessage = WxMpXmlOutMessage.TEXT()
					.content(outM).fromUser(inMessage.getToUserName())
					.toUser(inMessage.getFromUserName()).build();

			logger.info("outMessage = " + outMessage);

			response.getWriter().write(outMessage.toXml());
			return;
		}
		response.getWriter().println("不可识别的加密类型");
		return;
	}

	private String getStockMessage(String inM, String outM) {
		if (inM.indexOf("MRZT") == 0) {
			Date date = new Date();
			if (inM.length() > 4) {
				boolean invalidDate = false;
				String sDate = inM.substring(4).trim();
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
			try {
				List<StrategyQueryStockBean> list = CommonStrategyMgrImpl
						.getInstance().queryStocks(date, "MRZT");
				if (list.size() == 0) {
					return "还未收市，本账号只提供收市后数据";
				}
				outM = convertWetChatMessage(list);
				String header = "涨停榜(" + DateUtil.formatDay(date) + "):\n";
				header = "代码 名称 当日价 累计涨幅\n";
				outM = header + outM;
			} catch (KLineException e) {
				logger.error("Read Stock throw exception:", e);
			}
		}
		return outM;
	}

	private String convertWetChatMessage(List<StrategyQueryStockBean> list) {
		String outM;
		outM = "";
		for (int i = 0; i < list.size(); i++) {
			StrategyQueryStockBean bean = list.get(i);
			// http://m.money.163.com/stock/0600036.html
			// http://m.quote.eastmoney.com/stock,600162.shtml
			// http://s.m.sohu.com/t/cn/001/300001.html
			// http://m.hexun.com/stock.php?code=156
			double leijiPercentage = MathUtil.formatDoubleWith2((bean
					.getLatestSpj() - bean.getDqj() / bean.getDqj()) * 100);

			StringBuffer stockInfo = new StringBuffer();
			stockInfo
					.append(bean.getStockId())
					.append(" ")
					.append("<a href=\"http://m.quote.eastmoney.com/stock,"
							+ bean.getStockId() + ".shtml\">")
					.append(bean.getName()).append("</a>").append(" ")
					.append(bean.getDqj()).append("% ")
					.append(leijiPercentage + "%").append("\n");
			outM += stockInfo;
		}
		return outM;
	}

	private boolean verifySignature(String nonce, String timestamp,
			String signature) {
		List<String> list = new ArrayList<String>();
		String token = wxMpConfigStorage.getToken();
		logger.info("token = " + token);

		list.add(token);

		list.add(timestamp);
		logger.info("timestamp" + timestamp);

		list.add(nonce);
		logger.info("nonce" + nonce);
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