package ru.otus.HooArrayList;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Objects;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class HooArrayList<T> implements List<T> {

    private int size = 0;
    private int capacity = 10;

    private Object[] elementData;

    public HooArrayList(int size) {
        if (size >= 0) {
            elementData = new Object[size];
        } else {
            throw new IllegalArgumentException("Negative capacity!");
        }
    }

    public HooArrayList() {
        this.elementData = new Object[this.capacity];
    }

    public HooArrayList(Collection<? extends T> c) {
        this.size = c.size();
        this.elementData = c.toArray();
    }

    private void checkRange(int i) {
        if (i < 0 || i > this.size) {
            throw new IndexOutOfBoundsException("Index: " + i);
        }
    }

    private void ensureCapacity(int c) {
        while (this.size + c > this.capacity) {
            this.capacity = this.capacity * 3 / 2 + 1;
        }
        Object[] tmp_array = this.elementData;
        this.elementData = new Object[this.capacity];
        System.arraycopy(tmp_array, 0, this.elementData, 0, this.size);
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void clear() {
        for (int i = 0; i < this.size; ++i) {
            this.elementData[i] = null;
        }
        this.size = 0;
    }

    public boolean contains(Object o) {
        return false;
    }

    public T get(int i) {
        checkRange(i);
        return (T) this.elementData[i];
    }

    public T set(int i, T t) {
        checkRange(i);
        Object o = this.elementData[i];
        this.elementData[i] = t;
        return (T) o;
    }

    public void add(int i, T t) {
        checkRange(i);
        ensureCapacity(1);
        System.arraycopy(this.elementData, i, this.elementData, i + 1, this.size - i);
        this.elementData[i] = t;
        this.size++;
    }

    public boolean add(T t) {
        ensureCapacity(1);
        this.elementData[this.size++] = t;
        return true;
    }

    public boolean remove(Object o) {
        T t = remove(indexOf(o));
        return t.equals(o);
    }

    public T remove(int i) {
        checkRange(i);
        Object o = this.elementData[i];
        System.arraycopy(this.elementData, i + 1, this.elementData, i, --this.size);
        return (T) o;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < this.size; i++) {
                if (this.elementData[i] == null) return i;
            }
        } else {
            for (int i = 0; i < this.size; i++) {
                if (this.elementData[i].equals(o)) return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        int index = -1;
        if (o == null) {
            for (int i = 0; i < this.size; i++) {
                if (this.elementData[i] == null) index = i;
            }
        } else {
            for (int i = 0; i < this.size; i++) {
                if (this.elementData[i].equals(o)) index = i;
            }
        }
        return index;
    }

    public Object[] toArray() {
        Object[] o = new  Object[this.size];
        System.arraycopy(this.elementData, 0, o, 0, this.size);
        return o;
    }

    public <T> T[] toArray(T[] ts) {
        if (ts.length < this.size) {
            System.arraycopy(this.elementData, 0, ts, 0, ts.length);
        } else {
            if (this.size > 0) {
                System.arraycopy(this.elementData, 0 , ts, 0, this.size);
            } else {
                ts = null;
            }
        }

        return ts;
    }

    public List<T> subList(int from, int to) {
        checkRange(from);
        checkRange(to);
        if (from > to) {
            throw new IllegalArgumentException("fromIndex: " + from + " > toIndex " + to);
        }
        List<T> subList = new HooArrayList<>();
        for (int i = from; i < from + to; ++i) {
            subList.add((T) this.elementData[i]);
        }
        return subList;
    }

    public void sort(Comparator<? super T> comparator) {
        Arrays.sort((T[]) this.elementData,0, this.size, comparator);
    }

    public void forEach(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        T[] array = (T[])this.elementData;
        for (int i = 0; i < this.size; i++) {
            consumer.accept(array[i]);
        }
    }

    public Iterator<T> iterator() {
        return new HooArrayIterator<>(0);
    }

    public ListIterator<T> listIterator() {
        return new HooArrayIterator<>(0);
    }

    public ListIterator<T> listIterator(int i) {
        return new HooArrayIterator<>(i);
    }

    private class HooArrayIterator<E> implements ListIterator<E> {

        int cursor;
        int last = -1;

        public HooArrayIterator (int i) {
            this.cursor = i;
        }

        @Override
        public void add(E e) {
            throw new NotImplementedException();
        }

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public boolean hasPrevious() {
            throw new NotImplementedException();
        }

        @Override
        public E next() {
            last = cursor;
            E e = null;
            if (hasNext()) {
                e = (E) HooArrayList.this.elementData[cursor++];
            }
            return e;
        }

        @Override
        public int nextIndex() {
            throw new NotImplementedException();
        }

        @Override
        public E previous() {
            throw new NotImplementedException();
        }


        @Override
        public int previousIndex() {
            throw new NotImplementedException();
        }

        @Override
        public void remove() {
            throw new NotImplementedException();
        }

        @Override
        public void set(E e) {
            HooArrayList.this.set(last, (T) e);
        }

    }


    public boolean containsAll(Collection<?> collection) {
        throw new NotImplementedException("HooArrayList.containsAll");
    }

    public boolean addAll(Collection<? extends T> collection) {
        throw new NotImplementedException("HooArrayList.addAll");
    }

    public boolean addAll(int i, Collection<? extends T> collection) {
        throw new NotImplementedException("HooArrayList.addAll i");
    }

    public boolean removeAll(Collection<?> collection) {
        throw new NotImplementedException("HooArrayList.removeAll");
    }

    public boolean removeIf(Predicate<? super T> predicate) {
        throw new NotImplementedException("HooArrayList.removeIf");
    }

    public boolean retainAll(Collection<?> collection) {
        throw new NotImplementedException("HooArrayList.retainAll");
    }

    public void replaceAll(UnaryOperator<T> unaryOperator) {
        throw new NotImplementedException("HooArrayList.unaryOperator");
    }

    @Override
    public String toString() {
        return Arrays.toString(this.toArray());
    }
}
