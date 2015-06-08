package rest;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tasks.IdentifiebleTask;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/scheduler")
public class SchedulerController {

    private static final Logger log = LoggerFactory.getLogger(SchedulerController.class);

    @Autowired
    TaskScheduler taskScheduler;

    private List<IdentifiebleTask> tasks = new LinkedList<>();

    @PostConstruct
    void init() {
        log.debug("Initializing tasks");
        ScheduledFuture scheduledFuture = taskScheduler.schedule(() -> {
            log.debug("Static Cron Task");
        }, new CronTrigger("*/3 * * * * MON-FRI"));
        tasks.add(new IdentifiebleTask(scheduledFuture));
    }

    @RequestMapping(method = GET, produces = "application/json")
    @ResponseBody
    public synchronized IdentifiebleTask addNewTask() {
        ScheduledFuture scheduledFuture = taskScheduler.schedule(() -> {
            log.debug("New Cron Task");
        }, new CronTrigger("*/2 * * * * MON-FRI"));
        IdentifiebleTask task = new IdentifiebleTask(scheduledFuture);
        tasks.add(task);
        return task;
    }

    @RequestMapping(value = "/list", produces = "application/json")
    @ResponseBody
    public synchronized List<IdentifiebleTask> list() {
        return Lists.newArrayList(tasks);
    }

    @RequestMapping(value = "/stop", produces = "application/json")
    @ResponseBody
    public synchronized IdentifiebleTask stop(@RequestParam String id) {
        for(Iterator<IdentifiebleTask> i = tasks.iterator(); i.hasNext();) {
            IdentifiebleTask task = i.next();
            if (id.equals(task.id)) {
                task.stop();
                log.debug("Task [{}] has been stopped", id);
                i.remove();
                return task;
            }
        }
        return null;
    }
}
