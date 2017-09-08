package co.shipz.auth.config;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@ComponentScan(basePackages = {
  "co.shipz.auth.config"
})
public class PersistenceConfiguration {
  private final DataSource dataSource;

  @Autowired
  public PersistenceConfiguration(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Bean
  public Jdbi jdbi() {
    synchronized (Jdbi.class) {

      return Jdbi
        .create(dataSource)
        .installPlugin(new PostgresPlugin());
    }
  }

}
