package com.bgj.authentication;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.bgj.exception.AppExceptionKeys;
import com.bgj.exception.KLineAppException;
import com.common.web.Token;
import com.common.web.TokenUtil;

public class LoginMgrImpl {
	private static LoginMgrImpl instance = new LoginMgrImpl();
	private static Logger logger = Logger.getLogger(LoginMgrImpl.class);

	public static LoginMgrImpl getInstance() {
		return instance;
	}

	private LoginMgrImpl() {
	}

	private void loginValidateNull(LoginBean loginBean) throws Exception {
		if (loginBean.getWechatAccessToken() == null
				|| loginBean.getWechatAccessToken().equals("")) {
			throw new KLineAppException(
					AppExceptionKeys.WEBCHAT_ACCESSTOKEN_IS_NULL);
		}
	}

	Token parseWeChatProfileToToken(String json) {
		json = "    { \"openid\": \"oLVPpjqs9BhvzwPj5A-vTYAX3GLc\", \"nickname\": \"·½±¶\","
				+ "       \"sex\": 1, \"language\": \"zh_CN\", \"city\": \"Shenzhen\", \"province\":"
				+ "       \"Guangdong\", \"country\": \"CN\", \"headimgurl\":"
				+ "       \"http://wx.qlogo.cn/mmopen/utpKYf69VAbCRDRlbUsPsdQN38DoibCkrU6SAMCSNx558eTaLVM8PyM6jlEGzOrH67hyZibIZPXu4BK1XNWzSXB3Cs4qpBBg18/0\""
				+ "       , \"privilege\": [] }\" ";

		JSONObject obj1 = null;
		Token result = new Token();
		try {
			obj1 = new JSONObject(json);
			result.setOpenId(obj1.getString("openid"));
			result.setNickName(obj1.getString("nickname"));
			result.setProvince(obj1.getString("province"));
			result.setCity(obj1.getString("city"));
			result.setCountry(obj1.getString("country"));
			result.setSex(obj1.getInt("sex"));
		} catch (JSONException je) {
			logger.error("Parse WeChat JSON throw : ", je);
		}
		return result;
	}

	public LoginResultBean loginWithWeChat(LoginBean loginBean)
			throws Exception {
		loginValidateNull(loginBean);
		LoginResultBean result = new LoginResultBean();
		String weChatProfileJson = null;

		if (loginBean.getWechatAccessToken().equals("invalid")) {
			throw new KLineAppException(
					AppExceptionKeys.WE_CHAT_ACCESS_TOKEN_IS_INVALID);
		}
		/**
		 * 
		 * @todo
		 * 
		 *       call WeChat API to veirify the token call WeChat API to get
		 *       user profile info
		 * 
		 *       weChatProfileJson =
		 *       WeChatAPI.getProfile(loginBean.getWebchattoken);
		 * 
		 *       { "openid": "oLVPpjqs9BhvzwPj5A-vTYAX3GLc", "nickname": "·½±¶",
		 *       "sex": 1, "language": "zh_CN", "city": "Shenzhen", "province":
		 *       "Guangdong", "country": "CN", "headimgurl":
		 *       "http://wx.qlogo.cn/mmopen/utpKYf69VAbCRDRlbUsPsdQN38DoibCkrU6SAMCSNx558eTaLVM8PyM6jlEGzOrH67hyZibIZPXu4BK1XNWzSXB3Cs4qpBBg18/0"
		 *       , "privilege": [] }
		 * 
		 * **/
		Token token = parseWeChatProfileToToken(weChatProfileJson);
		String ticket = TokenUtil.getTicket(token);
		result.setkLineTicket(ticket);
		result.setNickName(token.getNickName());
		result.setOpenId(token.getOpenId());
		result.setValidUser(true);
		return result;
	}

}
