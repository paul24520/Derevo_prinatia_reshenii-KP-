package com.enteractive.trees;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы со списком стрелок
 */
public class Arrows implements Serializable {
    /**
     * Список стрелок
     */
    private final List<Arrow> arrowList;

    /**
     * Конструктор
     */
    public Arrows()
    {
        arrowList = new ArrayList<>();
    }

    /** Получить список стрелок
     * @return список стрелок
     */
    public List<Arrow> getArrowList() {
        return arrowList;
    }
}
