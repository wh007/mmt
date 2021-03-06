package lemon.weixin.message.bean;

import lemon.shared.toolkit.xstream.annotations.XStreamCDATA;
import lemon.shared.toolkit.xstream.annotations.XStreamProcessCDATA;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * media message<br>
 * such as: image, voice, video
 * @author lemon
 * @version 1.0
 *
 */
@XStreamProcessCDATA
public class MediaMessage extends WeiXinMessage {
	/** MediaId */
	@XStreamAlias("MediaId")
	@XStreamCDATA
	protected String mediaId;
	
	public MediaMessage(String msgType) {
		super(msgType);
	}
	
	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

}
