package com.beidou.exchange.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Configuration
@EnableTransactionManagement
@Slf4j
@PropertySource("classpath:druid-jdbc.properties")
public class DataBaseConfiguration {

    @Value("${druid.url}")
    private String url;

    @Value("${druid.userName}")
    private String userName;

    @Value("${druid.password}")
    private String password;

    @Value("${druid.initialSize}")
    private Integer initialSize;

    @Value("${druid.minIdle}")
    private Integer minIdle;

    @Value("${druid.maxActive}")
    private Integer maxActive;

    @Value("${druid.maxWait}")
    private Long maxWait;

    @Value("${druid.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;

    @Value("${druid.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;

    @Value("${druid.validationQuery}")
    private String validationQuery;

    @Value("${druid.testWhileIdle}")
    private Boolean testWhileIdle;

    @Value("${druid.testOnBorrow}")
    private Boolean testOnBorrow;

    @Value("${druid.testOnReturn}")
    private Boolean testOnReturn;

    @Value("${druid.poolPreparedStatements}")
    private Boolean poolPreparedStatements;

    @Value("${druid.maxPoolPreparedStatementPerConnectionSize}")
    private Integer maxPoolPreparedStatementPerConnectionSize;

    @Value("${druid.filters:null}")
    private String filters;

    @Value("${druid.removeAbandoned}")
    private Boolean removeAbandoned;

    @Value("${druid.removeAbandonedTimeout}")
    private Integer removeAbandonedTimeout;

    @Value("${druid.logAbandoned}")
    private Boolean logAbandoned;

    @Value("${druid.statFilter:false}")
    private Boolean statFilter;

    @Value("${druid.statFilterLogSlowSql:true}")
    private Boolean statFilterLogSlowSql;

    @Value("${druid.statFilterMergeSql:false}")
    private Boolean statFilterMergeSql;

    @Value("${druid.statFilterSlowLogMillis:0}")
    private Long statFilterSlowLogMillis;

    @Bean(destroyMethod = "close", initMethod = "init", name = "bdDataSource")
    public DataSource writeDataSource() {
        log.info("注入druid！！！");
        return createDataSource();
    }

    private DataSource createDataSource() {

        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        if (initialSize != null) {
            ds.setInitialSize(initialSize);
        }
        if (minIdle != null) {
            ds.setMinIdle(minIdle);
        }

        if (maxActive != null) {
            ds.setMaxActive(maxActive);
        }

        if (maxWait != null) {
            ds.setMaxWait(maxWait);
        }

        if (timeBetweenEvictionRunsMillis != null) {
            ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }

        if (minEvictableIdleTimeMillis != null) {
            ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }

        if (StringUtils.isNoneBlank(validationQuery)) {
            ds.setValidationQuery(validationQuery);
        }

        if (testWhileIdle != null) {
            ds.setTestWhileIdle(testWhileIdle);
        }

        if (testOnBorrow != null) {
            ds.setTestOnBorrow(testOnBorrow);
        }

        if (testOnReturn != null) {
            ds.setTestOnReturn(testOnReturn);
        }

        if (poolPreparedStatements != null) {
            ds.setPoolPreparedStatements(poolPreparedStatements);
        }

        if (maxPoolPreparedStatementPerConnectionSize != null) {
            ds.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        }

        if (StringUtils.isNoneBlank(filters)) {
            try {
                ds.setFilters(filters);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (removeAbandoned != null) {
            ds.setRemoveAbandoned(removeAbandoned);
        }

        if (removeAbandonedTimeout != null) {
            ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        }

        if (logAbandoned != null) {
            ds.setLogAbandoned(logAbandoned);
        }

        List<Filter> filters = Lists.newArrayList();
        if (statFilter != null && statFilter) {
            StatFilter sf = new StatFilter();
            if (statFilterLogSlowSql != null) {
                sf.setLogSlowSql(statFilterLogSlowSql);
            }

            if (statFilterMergeSql != null) {
                sf.setMergeSql(statFilterMergeSql);
            }

            if (statFilterSlowLogMillis != null) {
                sf.setSlowSqlMillis(statFilterSlowLogMillis);
            }
            filters.add(sf);
        }
        if (filters.size() > 0) {
            ds.setProxyFilters(filters);
        }

        return ds;
    }
}
