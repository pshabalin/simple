import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import config.RepositoryConfig;
import config.ServletConf;
import org.eclipse.jetty.server.HandlerContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Main {

    static final Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws Exception {
        Instant starTime = Instant.now();
        configureLogger();
        log.info("Starting web server");

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RepositoryConfig.class);
        rootContext.register(ServletConf.class);
        ConfigurableEnvironment environment = rootContext.getEnvironment();
        MutablePropertySources sources = environment.getPropertySources();
        sources.addLast(new ResourcePropertySource("file:config/application.properties"));

        ServletHolder servletHolder = new ServletHolder("dispatcher", new DispatcherServlet(rootContext));

        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(servletHolder, "/*");

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.setHandler(handler);

        Server server = new Server(8080);
        server.setHandler(contextHandler);
        server.start();
        log.info("Web Server started in {} seconds", Duration.between(starTime, Instant.now()).get(ChronoUnit.SECONDS));
        server.join();
    }

    private static void configureLogger() {
        File configFile = new File("config/logback.xml");
        if (!configFile.exists()) {
            return;
        }
        // assume SLF4J is bound to logback in the current environment
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(configFile);
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }
}
