package utils;

import java.util.Comparator;

/**
 * Created by sgnatiuk on 10/28/15.
 */
public class PriorityItem<T>{
    private int priority;
    private T data;

    public PriorityItem(int priority, T data) {
        this.priority = priority;
        this.data = data;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriorityItem<?> that = (PriorityItem<?>) o;

        return !(data != null ? !data.equals(that.data) : that.data != null);

    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PriorityItem{" +
                "priority=" + priority +
                ", data=" + data +
                '}';
    }

    /**
     *
     * @param <T>
     */
    public static class PriorityItemComparator<T> implements Comparator<PriorityItem<T>> {

        @Override
        public int compare(PriorityItem<T> o1, PriorityItem<T> o2) {
            return o2.getPriority() - o1.getPriority();
        }
    }

}
