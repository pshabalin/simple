package model.annotation;

import model.Node;

public @interface AllowedParent {

    Class<? extends Node>[] value();

}
