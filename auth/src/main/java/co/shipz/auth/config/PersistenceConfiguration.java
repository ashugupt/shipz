package co.shipz.auth.config;

import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Component
@ComponentScan(basePackages = {
  "co.shipz.auth.config"
})
public class PersistenceConfiguration {
  private final DataSource writeDataSource;
  private final DataSource readDataSource;

  @Autowired
  public PersistenceConfiguration(DataSource writeDataSource, DataSource readDataSource) {
    this.writeDataSource = writeDataSource;
    this.readDataSource = readDataSource;
  }

  @Bean
  public Jdbi dbReader() {
    synchronized (Jdbi.class) {
      return Jdbi
        .create(writeDataSource)
        .installPlugin(new SqlObjectPlugin())
        .installPlugin(new PostgresPlugin());
    }
  }

  @Bean
  public Jdbi dbWriter() {
    synchronized (Jdbi.class) {
      return Jdbi
        .create(readDataSource)
        .installPlugin(new SqlObjectPlugin())
        .installPlugin(new PostgresPlugin());
    }
  }

  @PostConstruct
  public void migrateFlyway() {
    Flyway flyway = new Flyway();

    /*
     * ReadDataSource is our primary datasource bean
     * Spring auto-initializes this datasource for migration
     * No need to init explicitly.
     * This comment is here just to avoid confusion
     */

    /*
     * Setup migration for write datasource
     */
    flyway.setDataSource(writeDataSource);
    flyway.repair();
    flyway.migrate();
  }
}
