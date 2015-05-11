import config.RepositoryConfig;
import config.ServletConf;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Main {

    static final Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        log.debug("Starting web server");

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RepositoryConfig.class);
        rootContext.register(ServletConf.class);
        ConfigurableEnvironment environment = rootContext.getEnvironment();
        MutablePropertySources sources = environment.getPropertySources();
        for (String activeProfile : environment.getActiveProfiles()) {
            sources.addLast(new ResourcePropertySource("classpath:/application-" + activeProfile + ".properties"));
        }
        sources.addLast(new ResourcePropertySource("classpath:/application.properties"));


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
