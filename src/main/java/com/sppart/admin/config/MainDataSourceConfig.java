package com.sppart.admin.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;


@Configuration
// domain 패키지를 이동했는데 spring devtools 예외가 발생해 이런 식으로 작성하게 됐습니다.
@MapperScan(value = {"com.sppart.admin.exhibition.domain.mapper", "com.sppart.admin.member.domain.mapper",
        "com.sppart.admin.productexhibition.mapper", "com.sppart.admin.product.domain.mapper",
        "com.sppart.admin.like.mapper", "com.sppart.admin.productwithtag.domain.mapper",
        "com.sppart.admin.pictureinfo.domain.mapper"})
public class MainDataSourceConfig {

    private final String MAIN_DATA_SOURCE = "MainDataSource";

    @Primary
    @Bean(MAIN_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.main.datasource.hikari")
    public DataSource mainDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean
    public SqlSessionFactory mainSqlSessionFactory(@Qualifier(MAIN_DATA_SOURCE) DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/main/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);

        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean
    public DataSourceTransactionManager mainTransactionManager(@Qualifier(MAIN_DATA_SOURCE) DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
