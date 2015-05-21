package config;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.Slf4jRequestLog;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.session.JDBCSessionIdManager;
import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;


@Configuration
public class JettyConfig {

    @Value("${envName}")
    String workerName;

    @Autowired
    DataSource dataSource;

    @Bean
    ServletHandler servletHandler() {
        return new ServletHandler();
    }

    @Bean
    ServletContextHandler servletContextHandler() {
        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.setServletHandler(servletHandler());
        contextHandler.setSessionHandler(sessionHandler());
        return contextHandler;
    }

    @Bean
    SessionIdManager sessionIdManager() {
        JDBCSessionIdManager sessionIdManager = new JDBCSessionIdManager(server());
        sessionIdManager.setDatasource(dataSource);
        sessionIdManager.setWorkerName(workerName);
        return sessionIdManager;
    }

    @Bean
    SessionManager sessionManager() {
        JDBCSessionManager sessionManager = new JDBCSessionManager();
        sessionManager.setSessionIdManager(sessionIdManager());
        sessionManager.setSaveInterval(60);
        sessionManager.setMaxInactiveInterval(15);
        return sessionManager;
    }

    @Bean
    SessionHandler sessionHandler() {
        return new SessionHandler(sessionManager());
    }

    @Bean
    RequestLogHandler requestLogHandler() {
        RequestLogHandler requestLogHandler = new RequestLogHandler();
        Slf4jRequestLog requestLog = new Slf4jRequestLog();
        requestLog.setExtended(false);
        requestLog.setLogTimeZone("GMT");
        requestLogHandler.setRequestLog(requestLog);
        return requestLogHandler;
    }

    HandlerCollection topLevelHandlers() {
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] {servletContextHandler(), requestLogHandler()});
        return handlers;
    }

    @Bean(destroyMethod = "stop")
    Server server() {
        return new Server(8080);
    }

    @PostConstruct
    void init() throws Exception {
        Server server = server();
        server.setHandler(topLevelHandlers());
        server.start();
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
