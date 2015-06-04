import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import config.JettyConfig;
import config.RepositoryConfig;
import config.ServletConf;
import config.SwagerConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import search.EmbeddedElasticsearchServer;

import java.io.File;
import java.io.IOException;

public class Main {

    static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        configureLogger();
        log.info("Starting web server");
        try {
            AnnotationConfigApplicationContext rootContext = initRootContext();
            AnnotationConfigApplicationContext jettyContext = initJettyContext(rootContext);
            initSpringMvcContext(rootContext, jettyContext);
            log.info("Initialization completed");
            jettyContext.getBean(Server.class).join();
        } catch (Throwable e) {
            log.error("Can not start server", e);
            throw e;
        }
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


    private static AnnotationConfigApplicationContext initRootContext() throws IOException {
        AnnotationConfigApplicationContext rootContext = new AnnotationConfigApplicationContext();
        rootContext.register(RepositoryConfig.class);
        rootContext.register(EmbeddedElasticsearchServer.class);
        ConfigurableEnvironment environment = rootContext.getEnvironment();
        MutablePropertySources sources = environment.getPropertySources();
        sources.addLast(new ResourcePropertySource("file:config/application.properties"));
        rootContext.refresh();
        return rootContext;
    }

    private static AnnotationConfigApplicationContext initJettyContext(AnnotationConfigApplicationContext rootContext) {
        AnnotationConfigApplicationContext jettyContext = new AnnotationConfigApplicationContext();
        jettyContext.setParent(rootContext);
        jettyContext.register(JettyConfig.class);
        jettyContext.refresh();
        return jettyContext;
    }

    private static void initSpringMvcContext(AnnotationConfigApplicationContext rootContext, AnnotationConfigApplicationContext jettyContext) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.setParent(rootContext);
        applicationContext.register(ServletConf.class);
        applicationContext.register(SwagerConfig.class);

        ServletHolder servletHolder = new ServletHolder("dispatcher", new DispatcherServlet(applicationContext));
        jettyContext.getBean(ServletHandler.class).addServletWithMapping(servletHolder, "/*");
    }

 }
