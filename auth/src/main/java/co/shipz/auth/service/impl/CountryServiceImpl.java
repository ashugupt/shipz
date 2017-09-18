package co.shipz.auth.service.impl;

import co.shipz.auth.dao.CountryDao;
import co.shipz.auth.model.Country;
import co.shipz.auth.service.CountryService;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CountryServiceImpl implements CountryService {
  private final Jdbi dbReader;

  @Autowired
  public CountryServiceImpl(final Jdbi dbReader) {
    this.dbReader = dbReader;
  }

  @Override
  @Async("jdbiAsyncExecutor")
  public CompletableFuture<List<Country>> listAllCountries() {
    List<Country> result = dbReader.withHandle(handle -> {
      CountryDao dao = handle.attach(CountryDao.class);
      return dao.listAll();
    });

    return CompletableFuture.completedFuture(result);
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
