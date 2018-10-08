package com.beidou.exchange.redis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Redis连接池配置属性定义
 * 
 * @author w
 *
 */
@Data
public class RedisProperty  {


	private String ip;


	private Integer port;


	private String password;


	private Integer db;
	

	private Integer maxtTotal;
	

	private Integer maxIdle;
	

	private Long maxWaitMillis;
	

	private Integer minIdle;
	

	private Boolean testOnBorrow;
	

	private Boolean testOnReturn;

	private Integer timeout;

}
