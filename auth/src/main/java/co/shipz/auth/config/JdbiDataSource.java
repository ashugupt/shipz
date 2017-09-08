package co.shipz.auth.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbiDataSource {
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() throws Exception {
    return DataSourceBuilder.create().build();
  }
}
