package lemon.shared.message.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.scripting.defaults.RawLanguageDriver;
import org.springframework.stereotype.Repository;

import lemon.shared.message.bean.MsgBean;
import lemon.shared.message.mapper.sqlprovider.MsgBeanSQLProvider;

/**
 * MsgBeanMapper Repository
 * 
 * @author lemon
 * @version 1.0
 * 
 */
@Repository
public interface MsgBeanMapper {
	
	/**
	 * 添加MsgBean
	 * @param msg
	 * @param level
	 * @return
	 */
	@InsertProvider(type = MsgBeanSQLProvider.class, method = "addMsgSQL")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "msg.id")
	int addMsg(@Param("msg") MsgBean msg, @Param("level") int level);
	
	/**
	 * 获取MsgBean
	 * @param id
	 * @param level
	 * @return
	 */
	@SelectProvider(type = MsgBeanSQLProvider.class, method = "getMsgSQL")
	MsgBean getMsg(@Param("id") int id, @Param("level") int level);
	
	/**
	 * 更新MsgBean
	 * @param msg
	 * @param level
	 * @return
	 */
	@UpdateProvider(type = MsgBeanSQLProvider.class, method = "updateMsgSQL")
	int updateMsg(@Param("msg") MsgBean msg, @Param("level") int level);
	
	/**
	 * 删除MsgBean
	 * @param id
	 * @param level
	 * @return
	 */
	@DeleteProvider(type = MsgBeanSQLProvider.class, method = "deleteMsgSQL")
	int deleteMsg(@Param("id") String id, @Param("level") int level);

	/**
	 * 根据KEY获取一级消息<br>
	 * 严格匹配
	 * 
	 * @param cust_id
	 * @param key
	 * @return
	 */
	@Select("SELECT A.id,A.cust_id,A.`key`,A.`value` FROM mmt_biz_l1 A WHERE A.cust_id=#{cust_id} AND A.`key`=#{key}")
	@Lang(RawLanguageDriver.class)
	MsgBean getL1Msg(@Param("cust_id") int cust_id, @Param("key") String key);

	/**
	 * 根据KEY获取二级消息<br>
	 * 模糊匹配
	 * 
	 * @param cust_id
	 * @param key
	 * @return
	 */
	@Select("SELECT A.id,A.cust_id,A.`key`,A.`value` FROM mmt_biz_l2 A WHERE A.cust_id=#{cust_id} AND A.`key` LIKE CONCAT('%',#{key},'%') ORDER BY A.`key` LIMIT 1")
	@Lang(RawLanguageDriver.class)
	MsgBean getL2Msg(@Param("cust_id") int cust_id, @Param("key") String key);

	/**
	 * 获取通用消息<br>
	 * 模糊匹配
	 * 
	 * @param cust_id
	 * @param key
	 * @return
	 */
	@Select("SELECT A.id,A.`key`,A.`value` FROM mmt_biz_l3 A WHERE A.`key` LIKE CONCAT('%',#{key},'%') ORDER BY A.`key` LIMIT 1")
	@Lang(RawLanguageDriver.class)
	MsgBean getL3Msg(@Param("key") String key);
	
	/**
	 * 获取一级消息列表
	 * @param cust_id
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select("SELECT A.id,A.cust_id,A.`key`,A.`value` FROM mmt_biz_l1 A WHERE A.cust_id=#{cust_id} LIMIT #{start},#{limit}")
	@Lang(RawLanguageDriver.class)
	List<MsgBean> getL1List(@Param("cust_id") int cust_id,
			@Param("start") int start, @Param("limit") int limit);
	
	/**
	 * 获取二级消息列表
	 * @param cust_id
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select("SELECT A.id,A.cust_id,A.`key`,A.`value` FROM mmt_biz_l2 A WHERE A.cust_id=#{cust_id} LIMIT #{start},#{limit}")
	@Lang(RawLanguageDriver.class)
	List<MsgBean> getL2List(@Param("cust_id") int cust_id,
			@Param("start") int start, @Param("limit") int limit);
	
	/**
	 * 获取二级消息数量
	 * @param cust_id
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM mmt_biz_l2 A WHERE A.cust_id=#{cust_id}")
	@Lang(RawLanguageDriver.class)
	int getL2Count(int cust_id);
	
	/**
	 * 获取三级消息列表
	 * @param cust_id
	 * @param start
	 * @param limit
	 * @return
	 */
	@Select("SELECT A.id,A.`key`,A.`value` FROM mmt_biz_l3 A LIMIT #{start},#{limit}")
	@Lang(RawLanguageDriver.class)
	List<MsgBean> getL3List(@Param("start") int start, @Param("limit") int limit);
	
	/**
	 * 获取三级消息数量
	 * @return
	 */
	@Select("SELECT COUNT(1) FROM mmt_biz_l3 A")
	@Lang(RawLanguageDriver.class)
	int getL3Count();
}
