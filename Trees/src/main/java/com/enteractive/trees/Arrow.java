package com.enteractive.trees;

import javafx.scene.Group;
import javafx.scene.shape.Line;

import java.io.Serializable;

/**
 * Класс для работы со с стрелками, соединяющими блоки
 */
public class Arrow extends Group implements Serializable {
    /**
     * Блок откуда идет стрелка
     */
    public Block from;
    /**
     * Блок куда идет стрелка
     */
    public Block to;

    /** Конструктор
     * @param line линия, соединящая блоки
     * @param from Блок откуда идет стрелка
     * @param to Блок куда идет стрелка
     */
    public Arrow(Line line, Block from, Block to) {
        super(line);
        this.from=from;
        this.to=to;
    }
}