package co.shipz.auth.controller;

import co.shipz.auth.model.Country;
import co.shipz.auth.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger log = LoggerFactory.getLogger(CountryController.class);
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
    log.info("Fetching the countries list from database asynchronously");
    CompletableFuture<List<Country>> countries = new CompletableFuture<>();
    try {
      countries = countryService.listAllCountries();
    } catch (Exception e) {
      countries.completeExceptionally(e);
    }

    return countries;
  }
}
