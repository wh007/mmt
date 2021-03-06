package lemon.weixin.message.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lemon.shared.api.simple.MMTRobot;
import lemon.weixin.WeiXinException;
import lemon.weixin.config.WeiXin;
import lemon.weixin.config.bean.WeiXinConfig;
import lemon.weixin.message.WeiXinMsgHelper;
import lemon.weixin.message.bean.EventMessage;
import lemon.weixin.message.bean.ImageMessage;
import lemon.weixin.message.bean.LinkMessage;
import lemon.weixin.message.bean.LocationMessage;
import lemon.weixin.message.bean.NewsMessage;
import lemon.weixin.message.bean.TextMessage;
import lemon.weixin.message.bean.VideoMessage;
import lemon.weixin.message.bean.VoiceMessage;
import lemon.weixin.message.bean.WeiXinMessage;
import lemon.weixin.message.parser.NewsMsgParser;
import lemon.weixin.message.parser.TextMsgParser;
import lemon.weixin.toolkit.WeatherAdapter;

/**
 * 简单的微信消息机器人
 * 
 * @author lemon
 * @version 1.0
 * 
 */
@Service
public class SimpleWeiXinMsgProcessor extends WXBasicMsgProcessor {
	@Autowired
	private WeiXinMsgHelper msgHelper;
	@Autowired
	private TextMsgParser textMsgParser;
	@Autowired
	private NewsMsgParser newsMsgParser;
	@Autowired
	private MMTRobot mmtRobot;
	@Autowired
	private WeatherAdapter weatherAdapter;

	@Override
	public String processImageMsg(String mmt_token, ImageMessage msg) {
		return sendTextMessage(msg, "亲，我暂时无法识别图片信息哦，您可以给我发文字消息。");
	}

	@Override
	public String processLinkMsg(String token, LinkMessage msg) {
		return sendTextMessage(msg, "亲，我暂时无法识别链接信息哦，您可以给我发文字消息。");
	}

	@Override
	public String processLocationMsg(String token, LocationMessage msg) {
		//TODO 对于地理位置消息，暂时先发送天气消息，以后可以考虑图文发送美食，天气等消息
		NewsMessage replyMsg = new NewsMessage();
		buildReplyMsg(msg, replyMsg);
		//查询天气信息
		String cityName = msg.getLabel().split("市")[0];
		replyMsg = weatherAdapter.generateWeatherReport(cityName, replyMsg);
		if(replyMsg == null)
			throw new WeiXinException("地理位置消息处理失败。");
		// save log
		msgHelper.saveSendNewsMsg(replyMsg);
		return newsMsgParser.toXML(replyMsg);
	}

	@Override
	public String processTextMsg(String mmt_token, TextMessage msg) {
		WeiXinConfig cfg = WeiXin.getConfig(mmt_token);
		// 生成回复消息
		String reply = mmtRobot.reply(cfg.getCust_id(), msg.getContent());
		if (null == reply)
			reply = getWelcome(cfg);
		return sendTextMessage(msg, reply);
		
	}

	@Override
	public String processClickEvent(String token, EventMessage msg) {
		//TODO 识别CLICK事件
		return sendTextMessage(msg, "MMTChat Event Click message replay.");
	}

	@Override
	public String processVideoMsg(String mmt_token, VideoMessage msg) {
		return sendTextMessage(msg, "亲，我暂时无法识别视频信息哦，您可以给我发文字消息。");
	}

	@Override
	public String processVoiceMsg(String mmt_token, VoiceMessage msg) {
		return sendTextMessage(msg, "亲，我暂时无法识别语音信息哦，您可以给我发文字消息。");
	}
	
	/**
	 * 发送文本消息
	 * @param msg
	 * @param content
	 * @return
	 */
	protected String sendTextMessage(WeiXinMessage msg,String content){
		TextMessage replyMsg = new TextMessage();
		buildReplyMsg(msg, replyMsg);
		replyMsg.setContent(content);
		// save log
		msgHelper.saveSendTextMsg(replyMsg);
		return textMsgParser.toXML(replyMsg);
	}

}
