package cn.lcs.tomcat;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.HostConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.Tomcat.FixContextListener;
import org.apache.catalina.startup.VersionLoggerListener;
import org.apache.catalina.valves.AccessLogValve;
import org.apache.tomcat.util.descriptor.web.ErrorPage;

/**
 * Tomcat start ! TOMCAT_HOME: /opt/tomcat/, TOMCAT_PORT: 8080, Can cnofig this
 * env !
 *
 */
public class Application {

    public static void main(String[] args) throws ServletException, LifecycleException, IOException {

        Map<String, String> map = System.getenv();

        File catalinaHome = new File(map.getOrDefault("TOMCAT_HOME", "/opt/tomcat/"));
        int port = Integer.valueOf(map.getOrDefault("TOMCAT_PORT", "8080"));

        Tomcat tomcat = new Tomcat();

        tomcat.setBaseDir(catalinaHome.getAbsolutePath());
        tomcat.getServer().addLifecycleListener(new VersionLoggerListener()); // nice to have
        tomcat.getHost().addLifecycleListener(new HostConfig());
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        tomcat.getService().addConnector(connector);

        connector.setPort(port);
        connector.setURIEncoding("UTF-8");

        tomcat.setConnector(connector);
        // tomcat.getHost().setAutoDeploy(false);
        AccessLogValve access = new AccessLogValve();
        access.setPattern("%h %l %u %t \"%r\" %s %b");
        tomcat.getEngine().getPipeline().addValve(access);

        // INIT mime-type
        tomcat.getHost().addLifecycleListener(new LifecycleListener() {

            public void lifecycleEvent(LifecycleEvent event) {
                if (event.getType().equals(Lifecycle.AFTER_INIT_EVENT)) {
                    StandardHost host = (StandardHost) event.getSource();
                    host.setContextClass(StandardMimeContext.class.getName());
                }
            }
        });

        // tomcat.getServer().setParentClassLoader(Application.class.getClassLoader());

        tomcat.start();
        tomcat.getServer().await();
    }

    public static class StandardMimeContext extends StandardContext {
        public StandardMimeContext() {
            // INIT !!!! MIME-TYPE
            Tomcat.initWebappDefaults(this);
            //
            this.addLifecycleListener(new FixContextListener());
            ErrorPage page = new ErrorPage();
            page.setLocation("/error");
            this.addErrorPage(page);
        }
    }
}
