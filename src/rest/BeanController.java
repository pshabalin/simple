package rest;


import model.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import repository.BeanRepo;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/bean")
public class BeanController {

    static final Logger log = LoggerFactory.getLogger(BeanController.class);

    @Autowired
    BeanRepo repo;

    public BeanController() {
        log.debug("Properties controller is up and running");
    }

    @RequestMapping(method = GET, produces = "application/json")
    @ResponseBody
    public List<Bean> get() {
        return repo.getAll();
    }
}
