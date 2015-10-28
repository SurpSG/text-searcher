package utils;

import java.util.Comparator;

/**
 * Created by sgnatiuk on 10/28/15.
 */
public class PriorityItem<T extends Comparable<T>> implements Comparable<T>{
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
    public int compareTo(T o) {
        return data.compareTo(o);
    }

    /**
     *
     * @param <T>
     */
    public static class PriorityItemComparator<T extends Comparable<T>> implements Comparator<PriorityItem<T>> {

        @Override
        public int compare(PriorityItem<T> o1, PriorityItem<T> o2) {
            if(o1.data.compareTo(o2.data) == 0){
                o2.setPriority(o2.getPriority()+1);
                o1.setPriority(o1.getPriority()+1);
            }
            return o2.getPriority() - o1.getPriority();
        }
    }
}
