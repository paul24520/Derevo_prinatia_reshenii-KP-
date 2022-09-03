package com.enteractive.trees;

import de.saxsys.javafx.test.JfxRunner;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
@RunWith(JfxRunner.class)
class BlockListTest {

    BlockList blockList;
    @BeforeEach
    void setUp() {
        JFXPanel fxPanel = new JFXPanel();
        blockList= new BlockList(() -> {});
    }

    @Test
    void addBlock() {
        Block block = blockList.addBlock(new Point2D(0, 9), "j", false,new SimpleDoubleProperty(3),new SimpleDoubleProperty(5));
        Assert.assertEquals(block,blockList.getBlocks().get(0));
    }

    @Test
    void getBlocks() {
        blockList.addBlock(new Point2D(0, 9), "j", false,new SimpleDoubleProperty(3),new SimpleDoubleProperty(5));
        blockList.addBlock(new Point2D(10, 9), "j", false,new SimpleDoubleProperty(3),new SimpleDoubleProperty(5));
        blockList.addBlock(new Point2D(110, 9), "jsd", false,new SimpleDoubleProperty(35),new SimpleDoubleProperty(35));
        Assert.assertEquals(3,blockList.getBlocks().size());
    }

    @Test
    void parentDelete() {
        Block block = blockList.addBlock(new Point2D(110, 9), "jsd", false,new SimpleDoubleProperty(35),new SimpleDoubleProperty(35));
        block.setParent(true);
        blockList.parentDelete();
        Assert.assertFalse(block.getParentBoolean());
    }
}