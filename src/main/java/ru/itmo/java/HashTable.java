package ru.itmo.java;



public class HashTable {

    private static class Entry {
        private Object value;
        private Object key;
        private boolean deleted;
        private Entry(Object newValue, Object newKey, boolean newDeleted) {
            value = newValue;
            key = newKey;
            deleted = newDeleted;
        }
    }
    private Entry[] table;
    private int size;
    private int elementsQuantity;
    private int realElementsQuantity;
    private double loadFactor;
    private  int threshold;

    private int Pow2(int size) {
        int tmp = 1;
        while(tmp < size) {
            tmp *= 2;
        }
        return tmp;
    }

    public HashTable(int newSize) {
        table = new Entry[Pow2(newSize)];
        elementsQuantity = 0;
        realElementsQuantity = 0;
        size = newSize;
        loadFactor = 0.5;
    }


    public HashTable(int newSize, double newLoadFactor) {
        table = new Entry[Pow2(newSize)];
        elementsQuantity = 0;
        realElementsQuantity = 0;
        size = newSize;
        loadFactor = newLoadFactor;
    }

    private int GetHash(Object key) {
        return Math.abs(key.hashCode());
    }
    Object put(Object key, Object value) {
        int hashCode = GetHash(key) % size;
        int step = 1;

        while(table[hashCode] != null && !table[hashCode].key.equals(key)) {
            hashCode = (GetHash(key) + step * step) % size;
            step++;
        }
        if(table[hashCode] == null) {
            table[hashCode] = new Entry(value, key, false);
            elementsQuantity++;
            realElementsQuantity++;
            if(realElementsQuantity >= threshold) {
                restructuring();
            }
            return null;
        }
        if(table[hashCode].deleted) {
            table[hashCode].value = value;
            table[hashCode].key = key;
            table[hashCode].deleted = false;
            elementsQuantity++;
            realElementsQuantity++;
            if(realElementsQuantity >= threshold) {
                restructuring();
            }
            return null;
        } else {

            Object oldValue = table[hashCode].value;
            table[hashCode].value = value;
            return oldValue;
        }
    }
    private void restructuring() {
        Entry[] copyTable = table.clone();
        Entry[] newTable = new Entry[size * 2];
        int oldSize = size;
        size = size * 2;
        threshold = (int)(loadFactor * size);
        table = newTable;
        elementsQuantity = 0;
        realElementsQuantity = 0;
        for(int i = 0; i < oldSize; i++) {
            if(copyTable[i] != null && !copyTable[i].deleted) {
                put(copyTable[i].key, copyTable[i].value);
            }
        }
    }
    Object get(Object key) {
        int hashCode = GetHash(key) % size;
        int step = 1;
        while(table[hashCode] != null && !table[hashCode].key.equals(key)) {
            hashCode = (GetHash(key) + step * step) % size;
            step++;
        }
        if(table[hashCode] == null || table[hashCode].deleted) {
            return null;
        } else {
            return table[hashCode].value;
        }
    }

    Object remove(Object key) {
        int hashCode = GetHash(key) % size;
        int step = 1;
        while(table[hashCode] != null && !table[hashCode].key.equals(key)) {
            hashCode = (GetHash(key) + step * step) % size;
            step++;
        }
        if(table[hashCode] == null || table[hashCode].deleted) {
            return null;
        } else {
            table[hashCode].deleted = true;
            elementsQuantity--;
            return table[hashCode].value;
        }
    }

    int size() {
        return elementsQuantity;
    }

}
