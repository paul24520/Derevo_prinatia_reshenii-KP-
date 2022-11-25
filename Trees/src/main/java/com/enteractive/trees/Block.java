package com.enteractive.trees;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Класс для работы с блоком
 */
public class Block extends Group implements Serializable {

    /**
     * Слушатели для отрисовки стрелок при перемещении блоков
     */
    transient public ArrayList<DragListener> listeners = new ArrayList<>();
    /**
     * Элемент интерфейса, показывающий пользователям текст блока
     */
    transient protected Label blockText;

    /**
     * Элемент интерфейса, показывающий пользователям дату
     */
    transient protected DatePicker data;
    /**
     * Элемент интерфейса, позволяющий пользователям редактировать текст блока
     */
    transient protected TextField editText;
    /**
     * Дата блока
     */
    public LocalDate datacal;
    /**
     * Текст блока
     */
    public String text;
    /**
     * Описание блока
     */
    public String description;
    /**
     * Элемент интерфейса, показывающий пользователям описание блока
     */
    transient TextArea desc;
    /**
     * ширина
     */
    protected double width;
    /**
     * высота
     */
    protected double height;
    /**
     * Координаты блока на панели
     */
    transient protected Point2D point;
    /**
     * Координата Х
     */
    public double pX;
    /**
     * Координата У
     */
    public double pY;
    /**
     * Якорь для перемещения блока по оси Х
     */
    private double mouseAnchorX;
    /**
     * Метка, показывающая что задача блока выполнена или нет
     */
    public boolean ready;
    /**
     * Якорь для перемещения блока по оси У
     */
    private double mouseAnchorY;
    /**
     * Метка, показывающая является ли блок родительским
     */
    transient boolean parent;

    /** Конструктор
     * @param point координаты
     * @param text текст
     * @param Ready выполнена ли задача
     * @param onDrag слушатели перемещений
     * @param widthBound слушатель ширины блока
     * @param heightBound слушатель высоты блока
     * @param datacal дата блока
     * @param description описание блока
     */
    public Block(Point2D point, String text,boolean Ready, DragListener onDrag, DoubleProperty widthBound, ReadOnlyDoubleProperty heightBound, LocalDate datacal,String description)
    {
        this.text=text;
        pX=point.getX();
        pY=point.getY();
        ready = Ready;
        this.description = description;
        float div=0;
        setTranslateX(point.getX());
        setTranslateY(point.getY());
        desc=new TextArea(description);
        blockText = new Label(text);
        editText= new TextField(text);
        this.datacal=datacal;
        data = new DatePicker(this.datacal);
        data.setMaxWidth(100);
        desc.setPrefColumnCount(10);
        desc.setPrefRowCount(2);
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
        desc.setOnKeyTyped(e -> this.description=desc.getText());
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
        data.setOnKeyTyped(e -> this.datacal=data.getValue());
        data.setOnAction(e -> this.datacal=data.getValue());
        editText.setOnKeyTyped(e -> this.datacal=data.getValue());
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
        desc.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-border-color: transparent; -fx-background-color:transparent;  -fx-faint-focus-color: -fx-control-inner-background ;");
        desc.setFont(Font.font("Console", 10));
        this.point = point;
        pX = this.point.getX();
        pY = this.point.getY();
        this.width = computePrefWidth(-1);
        this.height = computePrefHeight(-1);

        editText.setVisible(false);
        getChildren().add(blockText);
        getChildren().add(editText);
        getChildren().add(data);
        getChildren().add(desc);

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
     * @return является ли родительским блоком
     */
    public boolean getParentBoolean()
    {
        return parent;
    }

    /**
     * @param parent1 устанавливает, что блок родительский
     */
    public void setParent(boolean parent1)
    {
        parent = parent1;
    }

    /**
     * Метод отрисовки блока на панели
     */
    public void draw() {
        data.applyCss();
        data.layout();
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
        data.prefWidth(width);
        getChildren().add(data);
        getChildren().add(desc);
        getChildren().addAll(ready1,ready2);
        blockText.setTranslateX(offset * 0.5f);
        blockText.setTranslateY(offset * 0.5f);
        data.setTranslateY(offset* 2.7f);
        desc.setTranslateY(offset*5);
    }

    /** Получить высоту блока
     * @return высота
     */
    public double getHeight() {
        return height;
    }

    /** Получить ширину блока
     * @return ширину блока
     */
    public double getWidth() {
        return width;
    }

    /** Получить координаты блока
     * @return координаты
     */
    public Point2D getPosition() {
        return point;
    }

    /** Получить минимальную позицию по У
     * @return минимальная позиция по У
     */
    public double minY() {
        return getPosition().getY();
    }

    /** Получить минимальную позицию по Х
     * @return минимальная позиция по Х
     */
    public double minX() {
        return getPosition().getX();
    }

    /** Получить максимальную позицию по Х
     * @return максимальная позиция по Х
     */
    public double maxY() {
        return getPosition().getY() + getHeight();
    }

    /** Получить максимальную позицию по У
     * @return максимальная позиция по У
     */
    public double maxX() {
        return getPosition().getX() + getWidth();
    }

    /** Получить список минимальных и минимальных точек
     * @return список минимальных и минимальных точек
     */
    public ArrayList<Point2D> getArrayOfMinMaxPoints() {
        ArrayList<Point2D> fromPoints = new ArrayList<>();
        fromPoints.add(new Point2D(maxX(), maxY() - getHeight() * 0.5));
        fromPoints.add(new Point2D(maxX() - getWidth() * 0.5, minY()));
        fromPoints.add(new Point2D(maxX() - getWidth() * 0.5, maxY()));
        fromPoints.add(new Point2D(minX(), maxY() - getHeight() * 0.5));

        return fromPoints;
    }

    /** Сравнение 2 блоков
     * @param block сравниваемый
     * @return являются ли блоки одинаковыми
     */
    public boolean Equals(Block block)
    {
        return Objects.equals(this.text, block.text) && this.pX==block.pX && this.pY==block.pY;
    }
}
