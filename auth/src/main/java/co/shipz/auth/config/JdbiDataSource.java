package co.shipz.auth.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class JdbiDataSource {
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource writeDataSource() throws Exception {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource.read")
  public DataSource readDataSource() throws Exception {
    return DataSourceBuilder.create().build();
  }
}
