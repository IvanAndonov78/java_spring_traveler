package com.example.traveler.service;

import com.example.traveler.model.CsrfToken;
import org.springframework.stereotype.Service;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.URL;

import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class CsrfTokenService {


    public static boolean isWindowsOS() {
        Boolean isWindowsOperSys = java.lang.System.getenv("PATH").contains("WINDOWS");
        return isWindowsOperSys ? true : false;
    }

    public static String getEnv() {
        String env = java.lang.System.getenv("PATH");
        return env;
    }

    public static String getCurrentWorkingDir() {
        // String currentWorkingDirectory = System.getProperty("user.dir");
        // System.out.println("Working directory: " + workingDirectory); // C:\intelli_ws\traveler
        // return currentWorkingDirectory;
        Path rareRootPath = Paths.get("").toAbsolutePath();
        String currentWorkingDir = rareRootPath.normalize().toString();
        // System.out.println(currentWorkingDir); // C:\intelli_ws\traveler
        return currentWorkingDir;
    }

    public static String getCompiledClassPathToFile() {
        URL url = CsrfTokenService.class.getResource("/json_data/csrf_token.json");
        String rel_path = url.getPath();
        return rel_path;
    }

    public static String getNotCompiledPathToFile(String osStyle) {
        Path rareRootPath = Paths.get("").toAbsolutePath();
        String currentWorkingDir = rareRootPath.normalize().toString();
        // System.out.println(currentWorkingDir); // C:\intelli_ws\traveler

        String fileSeparator = System.getProperty("file.separator");
        String fileName = "csrf_token.json";

        String pathToFile = currentWorkingDir + fileSeparator
                + "src" + fileSeparator
                + "main" + fileSeparator
                + "resources" + fileSeparator
                + "json_data" + fileSeparator
                + fileName;

        if (osStyle == "winStyle") {
            return pathToFile;
            // C:\intelli_ws\traveler\src\main\resources\json_data\csrf_token.json
        } else if (osStyle == "linStyle") {
            return pathToFile.replaceAll("\\\\", "/"); // replace '\' with '/'
            // C:/intelli_ws/traveler/src/main/resources/json_data/temp_state.json
        }
        return pathToFile;
    }

    public static JSONObject getCompiledJsonData() {
        JSONParser parser = new JSONParser();
        try {

            String rel_path = getCompiledClassPathToFile();
            Object obj = parser.parse(new FileReader(rel_path));
            JSONObject jsonObject = (JSONObject)obj;
            return jsonObject;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getNotCompiledJsonData() {
        JSONParser parser = new JSONParser();
        try {
            String pathToFile = getNotCompiledPathToFile("linStyle");
            Object obj = parser.parse(new FileReader(pathToFile));
            JSONObject jsonObject = (JSONObject)obj;
            return jsonObject;

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setCsrfToken(String csrfToken, int isValidCsrfToken) {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("csrfToken", csrfToken);
        jsonObj.put("isValidCsrfToken", isValidCsrfToken);

        try {

            String pathToFile = getNotCompiledPathToFile("linStyle");

            FileWriter fw = new FileWriter(pathToFile);
            fw.write(jsonObj.toJSONString());
            fw.flush();
            fw.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static CsrfToken getCsrfTokenData() {
        // JSONObject jsonObj = getCompiledJsonData();
        JSONObject jsonObj = getNotCompiledJsonData();
        CsrfToken csrfToken = new CsrfToken();
        int isValidCsrfToken = Integer.parseInt(jsonObj.get("isValidCsrfToken").toString());
        String csrfTokenVal = jsonObj.get("csrfToken").toString();
        csrfToken.setIsValidCsrfToken(isValidCsrfToken);
        csrfToken.setCsrfToken(csrfTokenVal);
        return csrfToken;
    }

    public static void testPrintCsrfToken() {
        CsrfToken csrfToken = getCsrfTokenData();
        if (csrfToken != null) {
            System.out.println("------------------------------");
            System.out.println("Is valid CSRF Token: " + csrfToken.getIsValidCsrfToken());
            System.out.println("CSRF Token: " + csrfToken.getCsrfToken());
            System.out.println("------------------------------");
        }
    }

}
