package com.example.traveler.service;

import com.example.traveler.dao.ConnDao;
import com.example.traveler.model.Country;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CountryService {

    private static ConnDao connDao;

    public static void createTableCountry() {
        String query = "" +
        "CREATE TABLE IF NOT EXISTS country (" +
            "id INTEGER, " +
            "countrycode	TEXT, " +
            "currencycode TEXT, " +
            "countryname TEXT, " +
            "PRIMARY KEY('id' AUTOINCREMENT) " +
        ")";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

    }

    public static void deleteTableCountry() {
        String query = "DROP TABLE IF EXISTS country";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

    }


    public static boolean insertCountry(String countryCode, String currencyCode, String countryName) {

        if (countryCode == null || countryCode == "") {
            return false;
        }

        if (currencyCode == null || currencyCode == "") {
            return false;
        }

        if (countryName == null || countryName == "") {
            return false;
        }
        /* Does NOT work on Java 8, needs Java 11+ maybe:
        String query = "" +
            "INSERT INTO country (countrycode, currencycode, countryname) " +
            "VALUES (:countryCode, :currencyCode, :countryName);";
         */

        /* Does NOT work on Java 8, needs Java 11+ maybe:
        String query = "" +
            "INSERT INTO country (countrycode, currencycode, countryname) " +
            "VALUES (?, ?, ?);";
        */

        /* This works: */
        String query = "" +
            "INSERT INTO country " +
            "(countrycode, currencycode, countryname) " +
            "VALUES " +
            "(" +
            "'" + countryCode + "'" + ", " +
            "'" + currencyCode + "'" + ", " +
            "'" + countryName + "'" +
            ");";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static boolean editCountry( int id, String countryCode, String currencyCode, String countryName) {

        if (id != (int)id || (int)id < 1)  {
            return false;
        }

        if (countryCode == null || countryCode == "") {
            return false;
        }

        if (currencyCode == null || currencyCode == "") {
            return false;
        }

        if (countryName == null || countryName == "") {
            return false;
        }

        String query = "UPDATE country SET " +
            "countrycode = '" + countryCode + "', " +
            "currencyCode = '" + currencyCode + "'" +
            " WHERE id = " + id;

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static boolean deleteCountry(int id) {

        if (id != (int)id || (int)id < 1)  {
            return false;
        }

        String query = "DELETE FROM country WHERE id = " + id;

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static List<Country> getCountries() {

        String sql = "SELECT * FROM country";

        try {

            Connection conn = connDao.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<Country> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String countryCode = rs.getString("countrycode");
                String currencyCode = rs.getString("currencycode");
                String countryName = rs.getString("countryname");

                Country dbRow = new Country(id, countryCode, currencyCode, countryName);
                data.add(dbRow);
            }

            return data;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public static void pp() {
        System.out.println("-------------------------------");
    }

    public static Country getCountryObjByCountryName1(String inputCountryName) {

        String sql = "SELECT * FROM country WHERE countryname like " + "'%" + inputCountryName + "%'";

        try {

            Connection conn = connDao.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            List<Country> data = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String countryCode = rs.getString("countrycode");
                String currencyCode = rs.getString("currencycode");
                String countryName = rs.getString("countryname");

                Country dbRow = new Country(id, countryCode, currencyCode, countryName);
                data.add(dbRow);
            }
            if (data.get(0).getId() > 0) {
                return data.get(0);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
        public static Country getCountryObjByCurrencyCode(String inputCurrencyCode) {

        String sql = "SELECT * FROM country WHERE currencycode like " + "'%" + inputCurrencyCode + "%'";

        try {

            Connection conn = connDao.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<Country> data = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt("id");
                String countryCode = rs.getString("countrycode");
                String currencyCode = rs.getString("currencycode");
                String countryName = rs.getString("countryname");

                Country dbRow = new Country(id, countryCode, currencyCode, countryName);
                data.add(dbRow);
            }
            if (data.get(0).getId() > 0) {
                return data.get(0);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public static void migrate() {
        deleteTableCountry();
        createTableCountry();
        insertCountry("BG", "BGN", "Bulgaria");
        insertCountry("RO", "RON", "Romania");
        insertCountry("TR", "TRY", "Turkey");
        insertCountry("GR", "EUR", "Greece");
        insertCountry("MK", "MKD", "North Macedonia");
        insertCountry("SR", "RSD", "Serbia");
        // insertCountry("ZM", "ZMD", "Zamunda");
        // editCountry(1, "RO", "RON", "Romania"); // works
        // deleteCountry(6); // works
    }
    
    public static List<Country> getCountriesWithoutStartingCountry(
            List<Country> countries, 
           String startingCountryCurrencyCode
    ) {
        List<Country> processedCountries = new ArrayList<Country>();
        
        for (Country country : countries) {
            if (country.getCurrencyCode().equals(startingCountryCurrencyCode)) {
                continue;
            }
            processedCountries.add(country);
        }
        return processedCountries;
    }

    public static void printAllCountries() {

        List<Country> countries = getCountries();
        for (Country country : countries) {
            System.out.println(
                    country.getId() + " " +
                    country.getCountryCode() + " " +
                    country.getCountryName() + " " +
                    country.getCurrencyCode()
            );
        }
    }

    public static List<String> getCountryOptions() {
        List<String> countryOptions = new ArrayList<String>();
        List<Country> countries = getCountries();
        for (Country country : countries) {
            String countryName = country.getCountryName();
            countryOptions.add(countryName);
        }
        return countryOptions;
    }
    
    public static List<String> getCurrencyOptions() {
        List<String> currencyOptions = new ArrayList<String>();
        List<Country> countries = getCountries();
        for (Country country : countries) {
            String currencyCode = country.getCurrencyCode();
            currencyOptions.add(currencyCode);
        }
        return currencyOptions;
    }

    public static List<Map<String,Object>> getCountryOptionsExt() {
        List<Map<String,Object>> countryOptionsExt = new ArrayList<Map<String, Object>>();
        List<Country> countries = getCountries();
        for (Country country : countries) {
            Map<String, Object> entry = new HashMap<String, Object>();
            String countryName = country.getCountryName();
            String currencyCode = country.getCurrencyCode();
            entry.put("countryName", countryName);
            entry.put("currencyCode", currencyCode);
            countryOptionsExt.add(entry);
        }
        return countryOptionsExt;
    }



}
