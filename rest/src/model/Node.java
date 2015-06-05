package model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;
import java.util.UUID;

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="class")
public class Node {

    public Node parent;

    public UUID id;

    public String name;
    public String description;

    public Date created;
    public Date updated;

}
