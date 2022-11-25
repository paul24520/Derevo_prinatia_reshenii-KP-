package com.enteractive.trees;

/** Класс для работы с координатами
 * @param <X> координата
 * @param <Y> координата
 */
public class Points<X,Y> {
    /**
     * Х координата
     */
    public X item1;
    /**
     * У координата
     */
    public Y item2;

    /** Конструктор
     * @param item1 Х координата
     * @param item2 У координата
     */
    public Points(X item1, Y item2) {
        this.item1 = item1;
        this.item2 = item2;
    }
}
