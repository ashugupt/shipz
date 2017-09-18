package co.shipz.auth.controller;

import co.shipz.auth.model.Country;
import co.shipz.auth.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@EnableAsync
public class CountryController {
  private final CountryService countryService;

  @Autowired
  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @RequestMapping(value = "/country", method = RequestMethod.GET)
  public List<Country> listAllCountries() throws InterruptedException, ExecutionException {
    List<Country> countries = countryService
      .listAllCountries()
      .get();

    return countries;
  }

  @Async("servletAsyncExecutor")
  @RequestMapping(value = "/countryAsync", method = RequestMethod.GET)
  public Future<List<Country>> listAllCountriesAsync() throws InterruptedException, ExecutionException {
    CompletableFuture<List<Country>> countries = countryService
      .listAllCountries();

    return countries;
  }
}
