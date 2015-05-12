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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Main {

    static final Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        log.info("Starting web server");
        Instant starTime = Instant.now();

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RepositoryConfig.class);
        rootContext.register(ServletConf.class);
        ConfigurableEnvironment environment = rootContext.getEnvironment();
        MutablePropertySources sources = environment.getPropertySources();
        sources.addLast(new ResourcePropertySource("file:config/application.properties"));

        ServletHolder servletHolder = new ServletHolder("dispatcher", new DispatcherServlet(rootContext));

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar("webapp");
        webapp.addServlet(servletHolder, "/rest/*");

        Server server = new Server(8080);
        server.setHandler(webapp);
        server.start();
        log.info("Done in {} seconds", Duration.between(starTime, Instant.now()).get(ChronoUnit.SECONDS));
        server.join();
    }
}
