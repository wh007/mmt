package lemon.yixin.message.bean;

import lemon.shared.toolkit.xstream.annotations.XStreamProcessCDATA;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * video message
 * 
 * @author lemon
 * @version 1.0
 * 
 */
@XStreamAlias("xml")
@XStreamProcessCDATA
public class VideoMessage extends MediaMessage {
	public VideoMessage() {
		super(MsgType.VIDEO, "video/mp4");
	}

}
