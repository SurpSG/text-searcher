package com.pack;

import java.util.TreeSet;

/**
 * Created by sgnatiuk on 5/8/15.
 */
public class testic {

    public static void main(String[] args) {
        TreeSet<Item> set = new TreeSet<Item>();
        set.add(new Item(10));
        set.add(new Item(5));
        set.add(new Item(5));
        set.add(new Item(20));
        set.add(new Item(2));
        System.out.println(set);
    }

    static class Item implements Comparable<Item>{
        int n;
        Item(int n) {
            this.n = n;
        }
        public String toString() {
            return "Item " + n;
        }

        @Override
        public int compareTo(Item item) {
            return n - item.n;
        }
    }
}
