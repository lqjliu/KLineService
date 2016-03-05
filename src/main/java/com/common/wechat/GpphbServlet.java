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
			String outM = "再来一次:" + inM;
			if (inM.equals("MRZT")) {
				Date date = DateUtil.parseDay("2016-03-04");
				try {
					List<StrategyQueryStockBean> list = CommonStrategyMgrImpl
							.getInstance().queryStocks(date, "MRZT");
					outM = "";
					for (int i = 0; i < list.size(); i++) {
						StrategyQueryStockBean bean = list.get(i);
						StringBuffer stockInfo = new StringBuffer();
						stockInfo.append("<a href=\"http://www.sohu.com\">")
								.append(bean.getStockId()).append(" ")
								.append(bean.getName()).append("</a>").append(" ")
								.append(bean.getZdf()).append("%").append("\n");
						outM += stockInfo;
					}
				} catch (KLineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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