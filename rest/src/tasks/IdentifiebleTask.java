package tasks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

public class IdentifiebleTask {

    public String id;

    @JsonIgnore
    ScheduledFuture future;

    public IdentifiebleTask(String id, ScheduledFuture future) {
        this.id = id;
        this.future = future;
    }

    public IdentifiebleTask(ScheduledFuture future) {
        this.future = future;
        id = UUID.randomUUID().toString();
    }

    public void stop() {
        future.cancel(false);
    }
}
