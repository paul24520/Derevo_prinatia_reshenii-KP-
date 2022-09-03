package com.enteractive.trees;

/**
 * SubClass for connected block
 */
public class ConnectedBlocks {
    public Block[] arr;
    public ConnectedBlocks(Block from, Block to)
    {
        arr = new Block[2];
        arr[0]= from;
        arr[1] = to;
    }
}
