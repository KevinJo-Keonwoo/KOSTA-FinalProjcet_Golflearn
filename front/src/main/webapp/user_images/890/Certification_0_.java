package com.golflearn.config;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
//application.yml파일을 application.properties파일로 변경
@PropertySource("classpath:/application.properties")
public class DatabaseConfiguration {
	@Bean // 스프링컨테이너로 관리될 객체 선언
	@ConfigurationProperties(prefix="spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	@Bean
	public DataSource dataSource() throws Exception {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		return dataSource;
	}

	@Autowired
	private ApplicationContext applicationContext;
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {//<bean id="sqlSessionFactory">
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);

		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatisConf/mybatis-config.xml"));
		return sqlSessionFactoryBean.getObject();
	}
}
