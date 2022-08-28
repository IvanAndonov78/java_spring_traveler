package com.example.traveler.dao;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class ConnDao {

    /*
    1) Download the needed sqlite database driver:
    https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.25.2

    2) Add the jar into project
    To apply the used JDBC driver for sqlite (C:\intelli_ws\traveler\external_jars\sqlite-jdbc-3.25.2.jar):
    => Via Intellij IDEA =>
    File => Project Structure => Modules => Dependencies => Alt + Ins (or click on [+] btn)
    => 1. JARs or Directories
    => [x] C:\intelli_ws\traveler\external_jars\sqlite-jdbc-3.25.2.jar
    => Apply => OK

    3) Edit dependencies in pom.xml by adding this:
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.25.2</version>
    </dependency>
    */

    public static String getNotCompiledPathToFile(String osStyle) {
        Path rareRootPath = Paths.get("").toAbsolutePath();
        String currentWorkingDir = rareRootPath.normalize().toString();
        // System.out.println(currentWorkingDir); // C:\intelli_ws\traveler

        String fileSeparator = System.getProperty("file.separator");
        String fileName = "temp_state.json";

        String pathToFile = "jdbc:sqlite:" + currentWorkingDir + fileSeparator
                + "src" + fileSeparator
                + "main" + fileSeparator
                + "resources" + fileSeparator
                + "sqlite_data" + fileSeparator
                + fileName;

        if (osStyle == "winStyle") {
            return pathToFile;
            // C:\intelli_ws\traveler\src\main\resources\sqlite_data\data.db
        } else if (osStyle == "linStyle") {
            return pathToFile.replaceAll("\\\\", "/"); // replace '\' with '/'
            // C:/intelli_ws/traveler/src/main/resources/sqlite_data/data.db
        }
        return pathToFile;
    }

    public static Connection connect() {
        // String url = "jdbc:sqlite:C:/intelli_ws/traveler/src/main/resources/sqlite_data/data.db";
        String url = getNotCompiledPathToFile("linStyle");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("SQL Connection has been established!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return conn;
    }

}
