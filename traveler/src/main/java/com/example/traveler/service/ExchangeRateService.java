package com.example.traveler.service;

import com.example.traveler.model.ExchangeRate;
import com.example.traveler.model.User;
import org.springframework.stereotype.Service;

import com.example.traveler.dao.ConnDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRateService {

    private static ConnDao connDao;

    public static void createTableExchangeRate() {
        String query = "" +
                "CREATE TABLE IF NOT EXISTS exchangerate (" +
                "rateid INTEGER, " +
                "currencycodenumerator	TEXT, " +
                "currencycodedenumerator TEXT, " +
                "ratedate TEXT, " +
                "ratevalue REAL, " +
                "PRIMARY KEY('rateid' AUTOINCREMENT) " +
                ");";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("Table exchangerate has been created!");
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

    }

    public static void deleteTableExchangeRate() {
        String query = "DROP TABLE IF EXISTS exchangerate;";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("Table user has been deleted!");
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

    }

    public static boolean insertRate(ExchangeRate exchangeRate) {
        
        String currencyCodeNumerator = exchangeRate.getCurrencyCodeNumerator();
        if (currencyCodeNumerator == null || currencyCodeNumerator == "") {
            return false;
        }

        String currencyCodeDenumerator = exchangeRate.getCurrencyCodeDenumerator();
        if (currencyCodeDenumerator == null || currencyCodeDenumerator == "") {
            return false;
        }

        String rateDate = exchangeRate.getRateDate();
        if (rateDate == null || rateDate == "") {
            return false;
        }
        
        Double rateValue = exchangeRate.getRateValue();
        if (rateValue != (Double)rateValue) {
            return false;
        }
        
        String query = "" +
                "INSERT INTO exchangerate " +
                "(currencycodenumerator, currencycodedenumerator, ratedate, ratevalue) " +
                "VALUES " +
                "(" +
                "'" + currencyCodeNumerator + "'" + ", " +
                "'" + currencyCodeDenumerator + "'" + ", " +
                "'" + rateDate + "'" + ", " +
                "'" + rateValue + "'" +
                ");";

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("New Rate have been created!");
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static boolean editRate(ExchangeRate exchangeRate) {

        int rateId = exchangeRate.getRateId();
        if (rateId != (int)rateId || (int)rateId < 1)  {
            return false;
        }

        String currencyCodeNumerator = exchangeRate.getCurrencyCodeNumerator();
        if (currencyCodeNumerator == null || currencyCodeNumerator == "") {
            return false;
        }

        String currencyCodeDenumerator = exchangeRate.getCurrencyCodeDenumerator();
        if (currencyCodeDenumerator == null || currencyCodeDenumerator == "") {
            return false;
        }

        String rateDate = exchangeRate.getRateDate();
        if (rateDate == null || rateDate == "") {
            return false;
        }

        Double rateValue = exchangeRate.getRateValue();
        if (rateValue != (Double)rateValue) {
            return false;
        }
        
        String query = "UPDATE exchangerate SET " +
                "currencycodenumerator = '" + currencyCodeNumerator + "', " +
                "currencycodedenumerator = '" + currencyCodeDenumerator + "', " +
                "ratedate = '" + rateDate + "', " +
                "ratevalue = '" + rateValue + "'" +
                " WHERE rateid = " + rateId;

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("Exchange Rate has been edited!");
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static boolean deleteRate(int rateId) {

        if (rateId != (int)rateId || (int)rateId < 1)  {
            return false;
        }

        String query = "DELETE FROM exchangerate WHERE rateid = " + rateId;

        try {
            Connection connObject = connDao.connect();
            Statement stmt = connObject.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            // connObject.commit();
            connObject.close();
            System.out.println("Exchange rate has been deleted!");
            return true;
        } catch (SQLException ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }

        return false;

    }

    public static List<ExchangeRate> getExchangeRates() {

        String sql = "SELECT * FROM exchangerate;";

        try {

            Connection conn = connDao.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<ExchangeRate> data = new ArrayList<>();

            while(rs.next()) {
                
                int rateId = rs.getInt("rateid");
                String currencyCodeNumerator = rs.getString("currencycodenumerator");
                String currencyCodeDenumerator = rs.getString("currencycodedenumerator");
                String rateDate = rs.getString("ratedate");
                Double rateValue = rs.getDouble("ratevalue");

                ExchangeRate dbRow = new ExchangeRate(
                    rateId, currencyCodeNumerator, currencyCodeDenumerator, rateDate, rateValue
                );
                data.add(dbRow);
            }

            return data;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public static ExchangeRate getExchangeRateObjByCurrencyCode(String currencyCode) {

        String sql = "SELECT * FROM exchangerate WHERE currencycodedenumerator like " + "'%" + currencyCode + "%'";

        try {

            Connection conn = connDao.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<ExchangeRate> data = new ArrayList<>();
            while(rs.next()) {

                int rateId = rs.getInt("rateid");
                String currencyCodeNumerator = rs.getString("currencycodenumerator");
                String currencyCodeDenumerator = rs.getString("currencycodedenumerator");
                String rateDate = rs.getString("ratedate");
                Double rateValue = rs.getDouble("ratevalue");

                ExchangeRate dbRow = new ExchangeRate(
                        rateId, currencyCodeNumerator, currencyCodeDenumerator, rateDate, rateValue
                );
                data.add(dbRow);
            }
            return data.get(0);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public static void migrate() {

        deleteTableExchangeRate();
        createTableExchangeRate();

        ExchangeRate bgnBGN = new ExchangeRate(
                "BGN", "BGN", "2022-08-23", 1.00
        );
        insertRate(bgnBGN);
        
        ExchangeRate bgnRON = new ExchangeRate(
                "BGN", "RON", "2022-08-23", 2.50
        );
        insertRate(bgnRON); 

        ExchangeRate bgnTL = new ExchangeRate(
                "BGN", "TRY", "2022-08-23", 9.24
        );
        insertRate(bgnTL); 

        ExchangeRate bgnEUR = new ExchangeRate(
                "BGN", "EUR", "2022-08-23", 1.96
        );
        insertRate(bgnEUR); 

        ExchangeRate bgnMKD = new ExchangeRate(
                "BGN", "MKD", "2022-08-23", 31.19
        );
        insertRate(bgnMKD); 

        ExchangeRate bgnRSD = new ExchangeRate(
                "BGN", "RSD", "2022-08-23", 60.14
        );
        insertRate(bgnRSD);
        
    }

    public static void printAllRates() {

        List<ExchangeRate> rates = getExchangeRates();
        for (ExchangeRate rate : rates) {
            System.out.println(
                rate.getRateId() + " " +
                rate.getCurrencyCodeNumerator() + " " +
                rate.getCurrencyCodeDenumerator() + " " +
                rate.getRateDate() + " " +
                rate.getRateValue()
            );
        }
    }
}
