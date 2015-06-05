package rest;

import model.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import repository.NodeRepo;

import java.util.List;

@Controller
@RequestMapping("/repo")
public class RepositoryController {

    static final Logger log = LoggerFactory.getLogger(RepositoryController.class);

    @Autowired
    NodeRepo nodeRepo;

    @RequestMapping(value="/root", produces = "application/json")
    @ResponseBody
    public Node getRoot() {
        return nodeRepo.loadRoot();
    }

    @RequestMapping(value="/rootChildren", produces = "application/json")
    @ResponseBody
    public List<Node> getRootChildren() {
        return nodeRepo.loadRootChildren();
    }

    @RequestMapping(value="/create", produces = "application/json", consumes="application/json")
    @ResponseBody
    public Node create(@RequestBody Node node) {
        //nodeRepo.save(node);
        //return node;
        log.debug("Request: [{}]", node);
        return null;
    }

}
