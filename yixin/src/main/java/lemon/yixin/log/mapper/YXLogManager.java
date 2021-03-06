package lemon.yixin.log.mapper;

import java.util.List;

import lemon.shared.log.bean.SiteAccess;
import lemon.yixin.log.bean.MsgLog;
import lemon.yixin.log.bean.SubscribeLog;
import lemon.yixin.log.bean.UnSubscribeLog;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * This class is for YiXin's log
 * 
 * @author lemon
 * @version 1.0
 * 
 */
@Repository
public interface YXLogManager {
	/**
	 * save message log
	 * 
	 * @param log
	 */
	@Insert("INSERT INTO yixin_log_msg(cust_id,msgType,msg) SELECT #{cust_id},#{msgType},#{msg}")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void saveMessageLog(MsgLog log);
	
	/**
	 * save subscribe log
	 * @param log
	 */
	@Insert("INSERT INTO yixin_log_subscribe(cust_id,yxid) SELECT #{cust_id},#{yxid}")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void saveSubscribeLog(SubscribeLog log);
	
	/**
	 * save unsubscribe log
	 * @param log
	 */
	@Insert("INSERT INTO yixin_log_unsubscribe(cust_id,yxid) SELECT #{cust_id},#{yxid}")
	void saveUnSubscribeLog(UnSubscribeLog log);

	/**
	 * query for site access log 
	 * 
	 * @return
	 */
	// TODO query for site access log
	List<SiteAccess> listSiteAccessLogs(int cust_id);

	/**
	 * query for message log
	 * 
	 * @return
	 */
	// TODO query for message log
	List<MsgLog> listMsgLogs(int cust_id);
}
