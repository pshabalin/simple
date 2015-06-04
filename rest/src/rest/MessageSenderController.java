package rest;

import model.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/message")
public class MessageSenderController {

    @Autowired
    JmsMessagingTemplate messagingTemplate;

    @RequestMapping(method = GET, produces = "application/json")
    @ResponseBody
    public String get(@RequestParam String message) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("class", "model.Bean");
        messagingTemplate.convertAndSend("mainQueue", new Bean("Say: ", message), headers);
        return "OK";
    }

}
