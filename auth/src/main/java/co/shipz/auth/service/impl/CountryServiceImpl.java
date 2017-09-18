package co.shipz.auth.service.impl;

import co.shipz.auth.dao.CountryDao;
import co.shipz.auth.model.Country;
import co.shipz.auth.service.CountryService;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class CountryServiceImpl implements CountryService {
  private final Jdbi dbReader;
  private static final Logger log = LoggerFactory.getLogger(CountryService.class);

  @Autowired
  public CountryServiceImpl(final Jdbi dbReader) {
    this.dbReader = dbReader;
  }

  @Override
  @Async("jdbiAsyncExecutor")
  public CompletableFuture<List<Country>> listAllCountries() {
    log.info("Querying the database asynchronously to get country list.");
    List<Country> result = dbReader.withHandle(handle -> {
      CountryDao dao = handle.attach(CountryDao.class);
      return dao.listAll();
    });

    return CompletableFuture.completedFuture(result);

//    CompletableFuture<List<Country>> result = CompletableFuture
//      .supplyAsync(() -> dbReader.withHandle(handle -> {
//        CountryDao dao = handle.attach(CountryDao.class);
//        return dao.listAll();
//      }));
//
//    return result;
  }

  @Override
  public List<Country> listAllCountriesSync() {
    return dbReader.withHandle(handle -> {
      CountryDao dao = handle.attach(CountryDao.class);
      return dao.listAll();
    });
  }

  @Override
  @Async("jdbiAsyncExecutor")
  public CompletableFuture<String> getCountryCodeByName(String countryName) {
    return CompletableFuture
      .supplyAsync(() -> dbReader.withHandle(handle -> {
          CountryDao dao = handle.attach(CountryDao.class);
          return dao.findSortNameByCountryName(countryName);
        })
      );
  }

  @Override
  @Async("jdbiAsyncExecutor")
  public CompletableFuture<String> getCountryPhoneCodeByName(String countryName) {
    return CompletableFuture
      .supplyAsync(() -> dbReader.withHandle(handle -> {
          CountryDao dao = handle.attach(CountryDao.class);
          return dao.findPhoneCodeByCountryName(countryName);
        })
      );
  }
}
