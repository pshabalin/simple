package model.annotation;

import model.Node;

public @interface AllowedChild {

    Class<? extends Node>[] value();

}
