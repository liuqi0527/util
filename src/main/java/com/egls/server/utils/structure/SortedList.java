package com.egls.server.utils.structure;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * <pre>
 *     一个在任何时候都保持有序的列表.排序时间复杂度为(n * log n)
 *
 *     与{@link PriorityQueue}不同,本类是在插入的时候就实现排序,这样在插入之后就是有序的.
 *     而{@link PriorityQueue}在插入的时候并不进行完全排序,在使用的时候进行取到最适合的元素.
 *
 *     对于List来说,可以直接获取第二个,第三个,而对于队列来说,永远只能获取第一个.两者的应用场景不同,本类比{@link PriorityQueue}慢.
 *
 *     不是线程安全的.不能添加null元素.
 * </pre>
 *
 * @author mayer - [Created on 2018-08-21 14:37]
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class SortedList<E> implements Iterable<E> {

    private enum ComparableComparator implements Comparator {
        /**
         *
         */
        INSTANCE;

        /**
         * Comparable based compare implementation.
         *
         * @param obj1 left hand side of comparison
         * @param obj2 right hand side of comparison
         * @return negative, 0, positive comparison value
         */
        @Override
        public int compare(final Object obj1, final Object obj2) {
            return ((Comparable) obj1).compareTo(obj2);
        }
    }


    /**
     * 容量对齐单位,一个合适的值.
     */
    private static final int UNIT = 64;

    private static final int DEFAULT_CAPACITY = 256;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;


    private final AtomicLong modCount = new AtomicLong(0);

    private Object[] elements;

    /**
     * The ordering scheme used in this list.
     */
    private final Comparator<E> comparator;

    private final int initialSize;

    /**
     * inclusive
     */
    private int position = 0;

    /**
     * exclusive
     */
    private int limit = 0;

    public SortedList(final E element) {
        this();
        this.add(element);
    }

    public SortedList(final E[] elements) {
        this(elements.length);
        this.addAll(elements);
    }

    public SortedList(final Collection<E> elements) {
        this(elements.size());
        this.addAll(elements);
    }

    public SortedList(final E element, final Comparator<E> comp) {
        this(comp);
        this.add(element);
    }

    public SortedList(final E[] elements, final Comparator<E> comp) {
        this(elements.length, comp);
        this.addAll(elements);
    }

    public SortedList(final Collection<E> elements, final Comparator<E> comp) {
        this(elements.size(), comp);
        this.addAll(elements);
    }

    public SortedList() {
        this(DEFAULT_CAPACITY, null);
    }

    public SortedList(final int initialSize) {
        this(initialSize, null);
    }

    public SortedList(final Comparator<E> comp) {
        this(DEFAULT_CAPACITY, comp);
    }

    public SortedList(final int initialSize, final Comparator<E> comp) {
        if (initialSize <= 0) {
            throw new IllegalArgumentException("initial size must be positive." + initialSize);
        }
        this.initialSize = initialSize;
        this.comparator = comp == null ? ComparableComparator.INSTANCE : comp;
        this.elements = new Object[this.initialSize];
    }

    private void ensureIndexValid(final int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }

    public int getPosition() {
        return position;
    }

    public int getLimit() {
        return limit;
    }

    public int size() {
        return limit - position;
    }

    public int listSize() {
        return elements.length;
    }

    public final boolean hasRemaining() {
        return size() > 0;
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public void clear() {
        modCount.incrementAndGet();
        elements = new Object[this.initialSize];
        position = limit = 0;
    }

    public Object[] toArray() {
        return Arrays.copyOfRange(elements, position, limit);
    }

    public <T> T[] toArray(final T[] array) {
        if (array.length < size()) {
            return (T[]) Arrays.copyOfRange(elements, position, limit, array.getClass());
        }
        for (int i = 0, j = position; i < array.length; i++, j++) {
            array[i] = j < limit ? (T) elements[j] : null;
        }
        return array;
    }

    /**
     * 正序取排行.计数从0开始, 小于0是没有排名
     */
    public int rankOf(final E element) {
        return indexOf(element);
    }

    /**
     * 倒序取排行.计数从0开始, 小于0是没有排名
     */
    public int lastRankOf(final E element) {
        int lastIndex = lastIndexOf(element);
        return lastIndex >= 0 ? size() - 1 - lastIndex : lastIndex;
    }

    public int indexOf(final E element) {
        for (int i = position; i < limit; i++) {
            if (elements[i].equals(element)) {
                return i - position;
            }
        }
        return -1;
    }

    public int lastIndexOf(final E element) {
        for (int i = limit - 1; i >= position; i--) {
            if (elements[i].equals(element)) {
                return i - position;
            }
        }
        return -1;
    }

    public boolean contains(final E element) {
        return indexOf(element) >= 0;
    }

    public E peekFirst() {
        return isEmpty() ? null : get(0);
    }

    public E peekLast() {
        int size = size();
        return size > 0 ? get(size - 1) : null;
    }

    public E pollFirst() {
        return isEmpty() ? null : remove(0);
    }

    public E pollLast() {
        int size = size();
        return size > 0 ? remove(size - 1) : null;
    }

    /**
     * index对于使用者来看就是元素的索引.
     * 但是在实现内部,index实际是一个相对位置.
     * 这是一个优化方案,减少了部分的拷贝.
     *
     * @param index 元素索引
     * @return 获取到的元素
     */
    public E get(final int index) {
        ensureIndexValid(index);

        return (E) elements[position + index];
    }

    public void add(final E element) {
        Objects.requireNonNull(element);
        modCount.incrementAndGet();
        ensureCapacity(1);
        elements[limit++] = element;
        sort();
    }

    public void addAll(final E[] elementArray) {
        ensureCapacity(elementArray.length);
        Arrays.stream(elementArray).forEach(this::add);
    }

    public void addAll(final Collection<E> collection) {
        ensureCapacity(collection.size());
        collection.forEach(this::add);
    }

    /**
     * 单次排序. 时间复杂度(log n)
     */
    private void sort() {
        int hit = limit - 1;
        int low = position;
        int high = hit;
        E key = (E) elements[hit];
        while (low <= high) {
            int mid = (low + high) >>> 1;
            E midVal = (E) elements[mid];
            int cmp = this.comparator.compare(midVal, key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                hit = mid;
                high = mid - 1;
            } else {
                hit = mid;
                break;
            }
        }
        System.arraycopy(elements, hit, elements, hit + 1, limit - hit - 1);
        elements[hit] = key;
    }

    /**
     * index对于使用者来看就是元素的索引.
     * 但是在实现内部,index实际是一个相对位置.
     * 这是一个优化方案,减少了部分的拷贝.
     *
     * @param index 元素索引
     * @return 被移除的元素
     */
    public E remove(final int index) {
        ensureIndexValid(index);

        modCount.incrementAndGet();
        E result = (E) elements[position + index];
        elements[position + index] = null;
        if (index == 0) {
            position++;
        } else if (index == limit - position - 1) {
            limit--;
        } else {
            //size / 2 等于中点
            final int mid = size() >>> 1;
            if (index < mid) {
                //离左边近,左边的元素少,移动左边
                System.arraycopy(elements, position, elements, position + 1, index);
                elements[position] = null;
                position++;
            } else {
                //离右边近,右边的元素少,移动右边
                System.arraycopy(elements, position + index + 1, elements, position + index, size() - index - 1);
                limit--;
                elements[limit] = null;
            }
        }
        return result;
    }

    public boolean remove(final E element) {
        return remove(indexOf(element)) != null;
    }

    private void ensureCapacity(final int incremental) {
        if (limit + incremental > listSize()) {
            if (listSize() - size() >= incremental) {
                recycle(incremental);
            } else {
                int needLength = incremental - (listSize() - limit);
                int formatNeedLength = UNIT + ((needLength / UNIT) * UNIT);
                int newLength = listSize() + formatNeedLength;
                if (newLength > MAX_ARRAY_SIZE) {
                    throw new IllegalStateException("newLength too large! newLength : " + newLength);
                }

                Object[] temp = new Object[newLength];
                System.arraycopy(elements, position, temp, 0, size());
                elements = temp;
                limit -= position;
                position = 0;
            }
        }
    }

    private void recycle(final int incremental) {
        if (position == 0) {
            return;
        }
        if (listSize() > initialSize && (size() + incremental) <= initialSize) {
            Object[] temp = new Object[this.initialSize];
            System.arraycopy(elements, position, temp, 0, size());
            elements = temp;
        } else {
            System.arraycopy(elements, position, elements, 0, size());
        }
        limit -= position;
        position = 0;
        //清除多余的引用.
        for (int i = limit; i < elements.length; i++) {
            elements[i] = null;
        }
    }

    @Override
    public final String toString() {
        return getClass().getName()
                + "["
                + "initialSize=" + initialSize
                + ",position=" + position
                + ",limit=" + limit
                + ",size=" + size()
                + "]";
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private final class Itr implements Iterator<E> {

        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = position;

        /**
         * Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;

        /**
         * The modCount value that the iterator believes that the backing
         * List should have.  If this expectation is violated, the iterator
         * has detected concurrent modification.
         */
        long expectedModCount = modCount.get();

        @Override
        public boolean hasNext() {
            return cursor < limit;
        }

        @Override
        public E next() {
            int i = cursor;
            if (i >= limit) {
                throw new NoSuchElementException();
            }
            Object[] elementData = elements;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkForModification();
            try {
                final int index = lastRet - position;
                SortedList.this.remove(index);
                cursor = position + index;
                lastRet = -1;
                expectedModCount = modCount.get();
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(final Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int limit = SortedList.this.limit;
            int i = cursor;
            if (i >= limit) {
                return;
            }
            final Object[] elementData = elements;
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            while (i < limit && expectedModCount == modCount.get()) {
                consumer.accept((E) elementData[i++]);
            }
            // update once at end of iteration to reduce heap write traffic
            cursor = i;
            lastRet = i - 1;
            checkForModification();
        }

        final void checkForModification() {
            if (expectedModCount != modCount.get()) {
                throw new ConcurrentModificationException();
            }
        }

    }

}
