package co.shipz.auth.service;

import co.shipz.auth.model.Country;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface CountryService {
  CompletableFuture<List<Country>> listAllCountries();
  List<Country> listAllCountriesSync();

  CompletionStage<String> getCountryCodeByName(String countryName);

  CompletionStage<String> getCountryPhoneCodeByName(String countryName);
}
