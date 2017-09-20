package co.shipz.auth.service;

import co.shipz.auth.model.Country;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CountryService {
  List<Country> listAllCountriesSync();

  CompletableFuture<List<Country>> listAllCountries();

  CompletableFuture<String> getCountryCodeByName(String countryName);

  CompletableFuture<String> getCountryPhoneCodeByName(String countryName);
}
