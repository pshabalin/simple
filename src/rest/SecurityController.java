package rest;

import model.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/security")
public class SecurityController {

    static final Logger log = LoggerFactory.getLogger(SecurityController.class);

    @RequestMapping(method = GET, produces = "application/json")
    @ResponseBody
    public String get(HttpSession session) {
        String beanName = (String) session.getAttribute("bean");
        if (beanName == null) {
            beanName = "Bean";
        } else {
            beanName += " B";
        }
        session.setAttribute("bean", beanName);
        return beanName;
    }


}
