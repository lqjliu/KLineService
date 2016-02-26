package com.bgj.authentication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.common.web.Token;

public class LoginMgrImplTest {

	@Test
	public void testParseWeChatProfileToToken() {
		Token originalToken = new Token();
		originalToken.setOpenId("oLVPpjqs9BhvzwPj5A-vTYAX3GLc");
		originalToken.setCity("Shenzhen");
		originalToken.setCountry("CN");
		originalToken.setNickName("·½±¶");
		originalToken.setProvince("Guangdong");
		originalToken.setSex(1);

		LoginMgrImpl loginMgr = LoginMgrImpl.getInstance();
		String json = "    { \"openid\": \"oLVPpjqs9BhvzwPj5A-vTYAX3GLc\", \"nickname\": \"·½±¶\","
				+ "       \"sex\": 1, \"language\": \"zh_CN\", \"city\": \"Shenzhen\", \"province\":"
				+ "       \"Guangdong\", \"country\": \"CN\", \"headimgurl\":"
				+ "       \"http://wx.qlogo.cn/mmopen/utpKYf69VAbCRDRlbUsPsdQN38DoibCkrU6SAMCSNx558eTaLVM8PyM6jlEGzOrH67hyZibIZPXu4BK1XNWzSXB3Cs4qpBBg18/0\""
				+ "       , \"privilege\": [] }\" ";

		Token token = loginMgr.parseWeChatProfileToToken(json);

		assertEquals(token, originalToken);
	}

}
