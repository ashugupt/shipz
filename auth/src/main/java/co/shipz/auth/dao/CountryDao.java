package co.shipz.auth.dao;

import co.shipz.auth.model.Country;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface CountryDao {
  @RegisterBeanMapper(Country.class)
  @SqlQuery(
    "SELECT " +
      "id, " +
      "sortname, " +
      "name, " +
      "phonecode " +
    "FROM country;"
  )
  List<Country> listAll();

  @SqlQuery(
    "SELECT "+
      "sortname " +
    "FROM country "+
    "WHERE LOWER(name) = LOWER(:name);"
  )
  String findSortNameByCountryName(@Bind("name") String name);

  @SqlQuery(
    "SELECT "+
      "phonecode " +
    "FROM country "+
    "WHERE LOWER(name) = LOWER(:name);"
  )
  String findPhoneCodeByCountryName(@Bind("name") String name);
}
