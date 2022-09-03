package com.enteractive.trees;

import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.io.Serializable;

/**
 * Connection class
 */
public class Arrow extends Group implements Serializable {
    public Block from;
    public Block to;
    public Arrow(Line line, Block from, Block to) {
        super(line);
        this.from=from;
        this.to=to;
    }
}