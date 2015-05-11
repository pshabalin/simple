package rest;


import model.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.ResponseBody;
import repository.BeanRepo;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping("/bean")
public class PropertiesRest {

    static final Logger log = LoggerFactory.getLogger(PropertiesRest.class);

    @Inject
    BeanRepo repo;

    public PropertiesRest() {
        log.debug("Properties controller is up and running");
    }

    @RequestMapping(method = GET, produces = "application/json")
    @ResponseBody
    public List<Bean> get() {
        repo.init(1);
        return repo.getAll();
    }
}
