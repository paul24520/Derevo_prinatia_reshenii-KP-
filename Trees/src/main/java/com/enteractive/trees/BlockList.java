package com.enteractive.trees;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.DatePicker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы со списком блоков
 */
public class BlockList implements Serializable {

    /**
     * Список блоков
     */
    private final List<Block> blocks;
    /**
     * Слушатель перемещения блока для отрисовки стрелок
     */
    transient DragListener onDrag;

    /** Конструктор
     * @param onDrag слушатель перемещения блока для отрисовки стрелок
     */
    public BlockList(DragListener onDrag)
    {
        this.onDrag = onDrag;
        blocks = new ArrayList<>();
    }


    /**
     * Добавить блок
     * @param point2D координаты блока
     * @param text текст блоа
     * @param ready метка готовности
     * @param width параметр ширины блока
     * @param height параметр высоты блока
     * @return созданный блок
     */
    public Block addBlock(Point2D point2D, String text, boolean ready, DoubleProperty width, ReadOnlyDoubleProperty height, LocalDate data,String desc) {
        Block block = new Block(point2D,text,ready,onDrag,width,height,data,desc);
        blocks.add(block);
        return block;
    }

    /** Возвращает список блоков
     * @return список блоков
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Удаляет все родительские метки
     */
    public void parentDelete()
    {
        for (Block block:blocks) {
            block.setParent(false);
        }
    }
}
