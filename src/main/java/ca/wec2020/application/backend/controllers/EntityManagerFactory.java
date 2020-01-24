package ca.wec2020.application.backend.controllers;

import ca.wec2020.application.backend.models.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

public class EntityManagerFactory {
    @Bean(name = "entityManagerFactory")
    public static javax.persistence.EntityManagerFactory entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUsername("wec@wec2020-sql");
        dataSource.setPassword("J7L89E8EQFJansAG");
        dataSource.setUrl("jdbc:mysql://wec2020-sql.mysql.database.azure.com:3306/wec2020?useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        emf.setDataSource(dataSource);
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(jpaVendorAdapter);
        emf.setPackagesToScan("ca.wec2020.application.backend.models");
//        emf.setPackagesToScan(Account.class.getPackage().getN);
        emf.setPersistenceUnitName("default");
        emf.afterPropertiesSet();
        return emf.getObject();
    }
}

