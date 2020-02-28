import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarTest {

    @Test
    public void jarTest() throws IllegalAccessException, InstantiationException, ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, MalformedURLException {

        //Java Reflection
        File myJar = new File("target/RoadRecovery-1.0-jar-with-dependencies.jar");

        URLClassLoader child = new URLClassLoader(
                new URL[] {myJar.toURI().toURL()},
                this.getClass().getClassLoader()
        );
        Class classToLoad = Class.forName("nju.ics.Main.PathRestoration", true, child);
        Method method = classToLoad.getMethod("pathRestorationMethod", String.class, String.class);
        Object instance = classToLoad.newInstance();
        String basic_data_file_path = "src/test/resources/basic-data.xls";
        String returnedString = (String) method.invoke(
                instance,
                PathRestorationTest.successJsonObject.toString(),
                basic_data_file_path
        );

        System.out.println(returnedString);
    }


    @Test
    public void jarTest2() {


    }

}
