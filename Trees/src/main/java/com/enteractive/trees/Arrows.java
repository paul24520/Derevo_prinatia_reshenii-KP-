package com.enteractive.trees;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * list of connectors
 */
public class Arrows implements Serializable {
    private final List<Arrow> arrowList;
    public Arrows()
    {
        arrowList = new ArrayList<>();
    }

    /**
     * @return list of connectors
     */
    public List<Arrow> getArrowList() {
        return arrowList;
    }
}
