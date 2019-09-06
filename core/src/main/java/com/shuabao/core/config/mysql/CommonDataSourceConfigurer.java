package com.shuabao.core.config.mysql;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.shuabao.core.config.BaseDataSourceConfigurer;
import org.apache.ibatis.io.VFS;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Scott Wei on 4/7/2018.
 */

@Configuration
@MapperScan(basePackages = "com.shuabao.core.mapper", sqlSessionTemplateRef ="commonSqlSessionTemplate")
public class CommonDataSourceConfigurer extends BaseDataSourceConfigurer {//掃描的mapper包，以及使用對應的sqlSessionTemplate

    @Primary//標志著優先加載
    @ConfigurationProperties(prefix = "spring.commondatasource")//配置配置文件裏面的spring.commonDataSource屬性（用戶，密碼等）
    @Bean(name = "commonDataSource")//數據源的bean
    public DataSource initDataSource () {
        return super.configure(DruidDataSourceBuilder.create().build());
    }

    @Primary
    @Bean(name = "commonSqlSessionTemplate")//SqlSessionTemplate的bean,形參是由上面的commonDataSource依賴注入的
    public SqlSessionTemplate initSqlSessionTemplate(@Qualifier("commonDataSource") DataSource dataSource) throws Exception{
//        VFS.addImplClass(SpringBootVFS.class);//mybatis的vfs識別不了依賴jar的mapper，改用spring的。大坑
        final SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        //設置xml的位置
        factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/shuabao/core.mapper/*.xml"));
        return new SqlSessionTemplate(factory.getObject());
    }


    @Primary
    @Bean(name = "commonTransactionInterceptor")//事務攔截器的bean,形參是由上面的commonDataSource依賴注入的
    public TransactionInterceptor initTransactionInterceptor(@Qualifier("commonDataSource") DataSource dataSource) {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        final Properties properties = new Properties();
        properties.setProperty("find*", "PROPAGATION_SUPPORTS,readOnly");
        properties.setProperty("get*", "PROPAGATION_SUPPORTS,readOnly");
        properties.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        properties.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
        return new TransactionInterceptor(transactionManager, properties);
    }

    @Bean
    public BeanNameAutoProxyCreator initProxy() {//事務代理的bean
        final BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
        creator.setInterceptorNames("commonTransactionInterceptor");//加入事務攔截器的bean
        creator.setBeanNames("*Service","*ServiceImpl");//事務實行的對象是service類和它的實現類
        creator.setProxyTargetClass(true);
        return creator;
    }


}
