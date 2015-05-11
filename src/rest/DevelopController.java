package rest;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/env")
@Profile("develop")
public class DevelopController {

    @RequestMapping(method = GET, produces = "text/plain")
    @ResponseBody
    public String get() {
        return "Hi there, this is Develop Controller!";
    }

}
