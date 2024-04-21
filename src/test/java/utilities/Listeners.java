package utilities;

import org.testng.ITestListener;
import org.testng.ITestResult;

import java.time.LocalDateTime;

public class Listeners implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String message = String.format("[%s] Test started: %s.%s",
                LocalDateTime.now(), result.getTestClass().getName(), result.getMethod().getMethodName());
        System.out.println(message);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String message = String.format("[%s] Test passed: %s.%s",
                LocalDateTime.now(), result.getTestClass().getName(), result.getMethod().getMethodName());
        System.out.println(message);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String message = String.format("[%s] Test failed: %s.%s",
                LocalDateTime.now(), result.getTestClass().getName(), result.getMethod().getMethodName());
        System.out.println(message);
        System.out.println("Failure reason: " + result.getThrowable().getMessage());
    }
}
