import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
public class Testing {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(CompleteTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println("Failing somewhere:"+failure.toString());
        }
        System.out.println("Test success: "+result.wasSuccessful());

    }
}