package model;

import java.util.Date;

public class Bean {

    public String name;
    public String value;

    public transient Date date = new Date();

    public Bean() {
    }

    public Bean(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
