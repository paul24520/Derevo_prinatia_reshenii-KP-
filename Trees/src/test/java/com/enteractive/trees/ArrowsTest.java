package com.enteractive.trees;

import de.saxsys.javafx.test.JfxRunner;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(JfxRunner.class)
class ArrowsTest {

    @Test
    void getArrowList() {
        JFXPanel fxPanel = new JFXPanel();
        Arrows arrows = new Arrows();
        BlockList blockList=new BlockList(()->{});
        Block block = blockList.addBlock(new Point2D(0, 9), "j", false,new SimpleDoubleProperty(3),new SimpleDoubleProperty(5), LocalDate.now(),"");
        arrows.getArrowList().add(new Arrow(new Line(),block,block));
        assertEquals(arrows.getArrowList().size(), 1);
    }
}