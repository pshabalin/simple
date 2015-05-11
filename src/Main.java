import config.RepositoryConfig;
import config.ServletConf;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

    static final Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        log.debug("Starting web server");

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RepositoryConfig.class);
        rootContext.register(ServletConf.class);

        ServletHolder servletHolder = new ServletHolder("dispatcher", new DispatcherServlet(rootContext));

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("webapp");
        webapp.addServlet(servletHolder, "/rest/*");

        Server server = new Server(8080);
        server.setHandler(webapp);
        server.start();
        log.debug("Done");
        server.join();
    }

}
