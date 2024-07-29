package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.ITest;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class GlobalTimeoutListener implements IAnnotationTransformer, ITest {

    private static final double GLOBAL_TIMEOUT = 180; // SECONDS

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        if (annotation.getTimeOut() == 0) {
            annotation.setTimeOut((long) (GLOBAL_TIMEOUT * 1000));
        }
    }

    @Override
    public String getTestName() {
        return null;
    }
}
