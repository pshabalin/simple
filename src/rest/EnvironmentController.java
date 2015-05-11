package rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/version")
public class EnvironmentController {

    @Autowired
    Environment env;

    @Value("#{systemProperties['envName']} + 1")
    String systemProperties1;

    @Value("#{systemProperties['spring.profiles.active']} + 1")
    String systemProperties2;

    @Value("${appName}")
    String appName;

    @Value("${envName}")
    String envName;

    @RequestMapping(method = GET, produces = "text/plain")
    @ResponseBody
    public String get() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("env.getProperty spring.profiles.active: ").append(env.getProperty("spring.profiles.active")).append("\n");
        buffer.append("env.getProperty appName: ").append(env.getProperty("appName")).append("\n");
        buffer.append("systemProperties1: ").append(systemProperties1).append("\n");
        buffer.append("systemProperties2: ").append(systemProperties2).append("\n");
        buffer.append("appName: ").append(appName).append("\n");
        buffer.append("envName: ").append(envName).append("\n");
        return buffer.toString();
    }

}
