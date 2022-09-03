package com.enteractive.trees;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class of Block
 */
public class Block extends Group implements Serializable {

    transient public ArrayList<DragListener> listeners = new ArrayList<>();
    transient protected Label blockText;
    transient protected TextField editText;
    public String text;
    protected double width;
    protected double height;
    transient protected Point2D point;
    public double pX;
    public double pY;
    private double mouseAnchorX;
    public boolean ready;
    private double mouseAnchorY;
    transient boolean parent;

    public Block(Point2D point, String text,boolean Ready, DragListener onDrag, DoubleProperty widthBound, ReadOnlyDoubleProperty heightBound)
    {
        this.text=text;
        pX=point.getX();
        pY=point.getY();
        ready = Ready;
        float div=0;
        setTranslateX(point.getX());
        setTranslateY(point.getY());

        blockText = new Label(text);
        editText= new TextField(text);
        blockText.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && !e.isAltDown()) {
                this.text = blockText.getText();
                editText.setText(this.text);
                blockText.setVisible(false);
                editText.setVisible(true);
                editText.toFront();
                editText.requestFocus();
            }
            else if(e.isAltDown())
            {
                ready=!ready;
                draw();
            }
        });
        editText.setOnKeyTyped(e -> this.text=editText.getText());
        editText.focusedProperty().addListener((observable, oldValue, newValue) ->{
                if(!newValue) {
                    blockText.setVisible(true);
                    editText.setVisible(false);
                    blockText.setText(this.text);
                    blockText.toFront();
                    draw();
                }
        });
        editText.setOnKeyReleased(e -> {
            this.text=editText.getText();
            if(e.getCode() == KeyCode.ENTER)
            {
                blockText.setVisible(true);
                editText.setVisible(false);
                blockText.setText(this.text);
                blockText.toFront();
                draw();
            }
        });

        blockText.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        blockText.setFont(Font.font("Console", 10));
        editText.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        editText.setFont(Font.font("Console", 10));

        this.point = point;
        pX = this.point.getX();
        pY = this.point.getY();
        this.width = computePrefWidth(-1);
        this.height = computePrefHeight(-1);

        editText.setVisible(false);
        getChildren().add(blockText);
        getChildren().add(editText);

        setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
        });
        listeners.add(onDrag);

        setOnMouseDragged(mouseEvent -> {
            double translateX=Math.max(getTranslateX() + mouseEvent.getX() - mouseAnchorX, 0);
            double translateY=Math.max(getTranslateY() + mouseEvent.getY() - mouseAnchorY, 0);
            if(translateX>0)translateX=Math.min(getTranslateX() + mouseEvent.getX() - mouseAnchorX, widthBound.get() -width);
            if(translateY>0)translateY=Math.min(getTranslateY() + mouseEvent.getY() - mouseAnchorY, heightBound.get()-height);
            setTranslateX(translateX);
            setTranslateY(translateY);

            this.point = new Point2D(getTranslateX()- div, getTranslateY()- div);
            pX=this.point.getX();
            pY=this.point.getY();
            for (DragListener listener : listeners) {
                listener.onDrag();
            }
        });
    }

    /**
     * @return is it a parent-element
     */
    public boolean getParentBoolean()
    {
        return parent;
    }

    /**
     * @param parent1 - mark for TreeView
     */
    public void setParent(boolean parent1)
    {
        parent = parent1;
    }

    /**
     * drawing on pane
     */
    public void draw() {
        blockText.applyCss();
        blockText.layout();
        editText.layout();
        editText.applyCss();

        getChildren().clear();

        var textWidth = blockText.prefWidth(-1);
        var textHeight = blockText.prefHeight(-1);

        float offset = 10f;
        Rectangle rect = new Rectangle(
                0,
                0,
                textWidth + offset,
                textHeight + offset);

        float lineWidth = 2f;
        Rectangle clip = new Rectangle(
                lineWidth,
                lineWidth,
                textWidth + offset - lineWidth * 2,
                textHeight + offset - lineWidth * 2);
        Line ready1,ready2;
        if(!ready) {
            ready1 = new Line(rect.getX() + rect.getWidth() - 7,
                    rect.getY() + rect.getHeight() - 7,
                    rect.getX() + rect.getWidth() + 7,
                    rect.getY() + rect.getHeight() + 7);
            ready1.setStrokeWidth(4);
            ready2 = new Line(rect.getX() + rect.getWidth() + 7,
                    rect.getY() + rect.getHeight() - 7,
                    rect.getX() + rect.getWidth() - 7,
                    rect.getY() + rect.getHeight() + 7);
            ready1.setStroke(Color.RED);
            ready2.setStroke(Color.RED);
        }
        else {
            ready1 = new Line(rect.getX() + rect.getWidth(),
                    rect.getY() + rect.getHeight(),
                    rect.getX() + rect.getWidth() + 7,
                    rect.getY() + rect.getHeight() - 7);
            ready1.setStrokeWidth(4);
            ready2 = new Line(rect.getX() + rect.getWidth(),
                    rect.getY() + rect.getHeight(),
                    rect.getX() + rect.getWidth() - 5,
                    rect.getY() + rect.getHeight() - 4);
            ready1.setStroke(Color.GREEN);
            ready2.setStroke(Color.GREEN);
        }
        ready2.setStrokeWidth(4);
        rect.setFill(Color.BLACK);
        clip.setFill(Color.WHITE);

        point = new Point2D(getTranslateX(), getTranslateY());
        width = rect.prefWidth(-1);
        height = rect.prefHeight(-1);

        getChildren().add(rect);
        getChildren().add(clip);
        getChildren().add(blockText);
        getChildren().add(editText);
            getChildren().addAll(ready1,ready2);
        blockText.setTranslateX(offset * 0.5f);
        blockText.setTranslateY(offset * 0.5f);
    }
    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public Point2D getPosition() {
        return point;
    }
    public double minY() {
        return getPosition().getY();
    }

    public double minX() {
        return getPosition().getX();
    }

    public double maxY() {
        return getPosition().getY() + getHeight();
    }

    public double maxX() {
        return getPosition().getX() + getWidth();
    }

    /**getting list of the points for connections
     * @return list of the points
     */
    public ArrayList<Point2D> getArrayOfMinMaxPoints() {
        ArrayList<Point2D> fromPoints = new ArrayList<>();
        fromPoints.add(new Point2D(maxX(), maxY() - getHeight() * 0.5));
        fromPoints.add(new Point2D(maxX() - getWidth() * 0.5, minY()));
        fromPoints.add(new Point2D(maxX() - getWidth() * 0.5, maxY()));
        fromPoints.add(new Point2D(minX(), maxY() - getHeight() * 0.5));

        return fromPoints;
    }

    /**
     * @param block compared
     * @return equal or not
     */
    public boolean Equals(Block block)
    {
        return Objects.equals(this.text, block.text) && this.pX==block.pX && this.pY==block.pY;
    }
}
