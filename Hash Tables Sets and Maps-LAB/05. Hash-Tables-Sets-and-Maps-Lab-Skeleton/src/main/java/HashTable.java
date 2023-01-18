import com.sun.jdi.Value;

import java.security.Key;
import java.util.*;
import java.util.stream.Stream;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.80d;

    private LinkedList<KeyValue<K, V>>[] slots;
    private int count;
    private int capacity;


    public HashTable() {
        this(INITIAL_CAPACITY);


    }

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.count = 0;
        this.slots = new LinkedList[capacity];

    }

    public void add(K key, V value) {
        this.growIfNeeded();
        int slotNumber = this.findSlotNumber(key);
        if (this.slots[slotNumber] == null) {
            this.slots[slotNumber] = new LinkedList<>();
        }

        for (KeyValue<K, V> element : slots[slotNumber]) {
            if (element.getKey().equals(key)) {
                throw new IllegalArgumentException("Key already exists: " + key);
            }
        }

        KeyValue<K, V> newElement = new KeyValue<>(key, value);
        this.slots[slotNumber].addLast(newElement);
        this.count++;

    }

    private int findSlotNumber(K key) {
        return Math.abs(key.hashCode()) % this.slots.length;
    }

    private void growIfNeeded() {

        double fillFactor = (double) (this.size() + 1) / this.capacity();
        if (fillFactor > LOAD_FACTOR) {
            this.grow();

        }
    }

    private void grow() {
        HashTable<K, V> newHashTable = new HashTable<>(this.slots.length * 2);

        for (LinkedList<KeyValue<K, V>> slot : this.slots) {
            if (slot != null) {
                for (KeyValue<K, V> kvKeyValue : slot) {
                    newHashTable.add(kvKeyValue.getKey(), kvKeyValue.getValue());
                }
            }
        }
        this.capacity = newHashTable.capacity;
        this.slots = newHashTable.slots;
        this.count = newHashTable.count;

    }

    public int size() {
        return this.count;
    }

    public int capacity() {
        return this.capacity;
    }

    public boolean addOrReplace(K key, V value) {


        int slotNumber = this.findSlotNumber(key);
        if (this.slots[slotNumber] == null) {
            this.slots[slotNumber] = new LinkedList<>();
        }

        for (KeyValue<K, V> element : slots[slotNumber]) {
            if (element.getKey().equals(key)) {
             element.setValue(value);
             return false;
            }
        }

        KeyValue<K, V> newElement = new KeyValue<>(key, value);
        this.slots[slotNumber].addLast(newElement);
        this.count++;
        return true;
    }

    public V get(K key) {
        KeyValue<K, V> kvKeyValue = this.find(key);
        if (kvKeyValue == null) {
            throw new IllegalArgumentException();
        }
        return kvKeyValue.getValue();

    }
//

    public KeyValue<K, V> find(K key) {
        int slotNumber = this.findSlotNumber(key);
        if (this.slots[slotNumber] != null) {
            for (KeyValue<K, V> kvKeyValue : this.slots[slotNumber]) {
                if (kvKeyValue != null) {
                    if (kvKeyValue.getKey().equals(key)) {
                        return kvKeyValue;
                    }
                }
            }
        }
        return null;

    }

    public boolean containsKey(K key) {
        int slotNumber = this.findSlotNumber(key);
        if (this.slots[slotNumber] != null) {


            for (KeyValue<K, V> kvKeyValue : this.slots[slotNumber]) {
                if (kvKeyValue.getKey().equals(key)) {
                    return true;
                }
            }

        }
        return false;
    }

    public boolean remove(K key) {
        KeyValue<K, V> kvKeyValue = this.find(key);
        if (kvKeyValue == null) {
            return false;
        }
        int slotNumber = this.findSlotNumber(key);
        this.slots[slotNumber].remove(kvKeyValue);
        this.count--;
        return true;
    }

    public void clear() {
        this.count = 0;
        this.slots = new LinkedList[INITIAL_CAPACITY];
    }

    public Iterable<K> keys() {
        throw new UnsupportedOperationException();
    }

    public Iterable<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
       return new Iterator<KeyValue<K, V>>() {

          Deque<KeyValue<K,V>> elements= new ArrayDeque<>();



           @Override
           public boolean hasNext() {
               return !elements.isEmpty();
           }

           @Override
           public KeyValue<K, V> next() {
               return elements.poll();
           }
       };
    }
}
