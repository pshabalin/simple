package tasks;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;

public class SimpleScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(SimpleScheduledTasks.class);

    AtomicInteger counter1 = new AtomicInteger();

    @Scheduled(fixedRate = 1000)
    void task1() {
        log.debug("Task 1 {}", counter1.incrementAndGet());
    }

    @Scheduled(cron="*/5 * * * * MON-FRI")
    void task2() {
        log.debug("Task 2 {}", counter1.incrementAndGet());
    }
}
