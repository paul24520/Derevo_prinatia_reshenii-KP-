package com.enteractive.trees;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * list of Blocks
 */
public class BlockList implements Serializable {

    private final List<Block> blocks;
    transient DragListener onDrag;
    public BlockList(DragListener onDrag)
    {
        this.onDrag = onDrag;
        blocks = new ArrayList<>();
    }


    /**
     * Create a new Block
     * @param point2D point on pane
     * @param text in label
     * @param ready tag
     * @param width bound of pane
     * @param height bound of pane
     * @return new block
     */
    public Block addBlock(Point2D point2D, String text,boolean ready, DoubleProperty width, ReadOnlyDoubleProperty height) {
        Block block = new Block(point2D,text,ready,onDrag,width,height);
        blocks.add(block);
        return block;
    }

    /**
     * @return list of blocks in Tree
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * deletes all parentMarks
     */
    public void parentDelete()
    {
        for (Block block:blocks) {
            block.setParent(false);
        }
    }
}
