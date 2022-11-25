package com.enteractive.trees;

/**
 * Класс для соединяемых блоков
 */
public class ConnectedBlocks {
    /**
     * Массив соединяемых блоков
     */
    public Block[] arr;

    /** Конструктор
     * @param from первый соединяемый блок
     * @param to второй соединяемый блок
     */
    public ConnectedBlocks(Block from, Block to)
    {
        arr = new Block[2];
        arr[0]= from;
        arr[1] = to;
    }
}
