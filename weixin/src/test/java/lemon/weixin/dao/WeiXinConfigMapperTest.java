package lemon.weixin.dao;

import static org.junit.Assert.*;

import java.util.List;

import lemon.shared.customer.bean.Customer;
import lemon.shared.customer.mapper.CustomerMapper;
import lemon.shared.entity.Status;
import lemon.weixin.config.bean.AccountType;
import lemon.weixin.config.bean.WeiXinConfig;
import lemon.weixin.config.mapper.WXConfigMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RunWith(JUnit4.class)
public class WeiXinConfigMapperTest {
	private WXConfigMapper configMapper;
	private CustomerMapper custMapper;
	private AbstractApplicationContext acx;
	private static Log logger = LogFactory.getLog(WeiXinConfigMapperTest.class);
	@Before
	public void init() {
		String[] resource = { "classpath:spring-db.xml",
				"classpath:spring-dao.xml", "classpath:spring-service.xml" };
		acx = new ClassPathXmlApplicationContext(resource);
		configMapper = (WXConfigMapper) acx.getBean(WXConfigMapper.class);
		custMapper = (CustomerMapper) acx.getBean(CustomerMapper.class);
		assertNotNull(configMapper);
		assertNotNull(custMapper);
	}
	@After
	public void destory(){
		acx.close();
	}
	@Test
	public void crud(){
		int cust_id = 10;
		Customer cust = custMapper.getCustomer(cust_id);
		if(null == cust){
			cust = new Customer();
			cust.setCust_name("JUnit Test Customer");
			cust.setMemo("JUnit Test");
			cust.setStatus(Status.AVAILABLE);
			custMapper.addCustomer(cust);
			cust_id = cust.getCust_id();
		}
		assertNotEquals(0, cust_id);
		
		WeiXinConfig cfg = configMapper.get(cust_id);
		if(null != cfg){
			configMapper.delete(cust_id);
		}
		cfg = new WeiXinConfig();
		cfg.setCust_id(cust_id);
		cfg.setToken("Junit Test Token");
		cfg.setWx_account("Junit Test WeiXin Account");
		cfg.setSubscribe_msg("Welcome to subscribe Junit Test");
		cfg.setWelcome_msg("业务咨询请按1");
		cfg.setApi_url("ASDLADLKJFWQ");
		cfg.setAppid("APPID");
		cfg.setSecret("secret");
		cfg.setBiz_class("com.com.XXX");
		cfg.setAccount_type(AccountType.DY);
		configMapper.save(cfg);
		List<WeiXinConfig> list = configMapper.availableList();
		for (WeiXinConfig weiXinConfig : list) {
			assertNotNull(weiXinConfig.getTimestamp());
			logger.debug(weiXinConfig);
		}
	}
}
