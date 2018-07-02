package cn.lcs.tomcat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.VersionLoggerListener;

/**
 * Hello world!
 *
 */
public class Application {

    public static void main(String[] args) throws ServletException, LifecycleException, IOException {

        Map<String, String> map = System.getenv();

        File catalinaHome = new File(map.getOrDefault("TOMCAT_HOME", "/opt/tomcat/"));
        int port = Integer.valueOf(map.getOrDefault("TOMCAT_PORT", "8080"));

        Tomcat tomcat = new Tomcat();

        tomcat.setPort(port); // HTTP port
        tomcat.setBaseDir(catalinaHome.getAbsolutePath());
        tomcat.getServer().addLifecycleListener(new VersionLoggerListener()); // nice to have

        //tomcat.getServer().setParentClassLoader(Application.class.getClassLoader());

        tomcat.start();
        tomcat.getServer().await();
    }
}
