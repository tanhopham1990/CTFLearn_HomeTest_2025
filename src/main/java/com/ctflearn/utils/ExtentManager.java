package com.ctflearn.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExtentManager {

    private static final ExtentReports extent = new ExtentReports();
    @Getter
    private static ExtentTest test;

    @Setter
    @Getter
    private static boolean isError = true;

    public static ExtentReports createInstance() {
        String reportPath = "test-output/ExtentReport.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Automation Test Report");
        spark.config().setDocumentTitle("Test Execution Report");

        extent.attachReporter(spark);
        return extent;
    }

    public static void createTest(String name) {
        test = extent.createTest(name);
    }

    public static void generateReport(String screenshotPath) {

        String logFilePath = "logs/automation.log";

        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("INFO") &&
                        !line.contains("WebTestWrapper") &&
                        !line.contains("MobileTestWrapper") &&
                        test != null)
                    test.info(line);
            }
            if (screenshotPath != null) {
                test.addScreenCaptureFromPath(screenshotPath);
            }
        } catch (IOException e) {
            System.out.println("Error reading log file: " + e.getMessage());
        }

    }

    public static void flushReports() {
        extent.flush();
    }

}
