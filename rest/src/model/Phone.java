package model;

import model.annotation.AllowedChild;

@AllowedChild({})
public class Phone extends Node {

    public String apiKey;

    public GeoLocation location;

    public Bean bean;
}
