package utils;

import java.util.Comparator;

/**
 * Created by sgnatiuk on 10/29/15.
 */
public class PriorityDateItem<T> extends PriorityItem<T> {

    private long date;

    public PriorityDateItem(int priority, long date, T data) {
        super(priority, data);
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PriorityDateItem{" +
                "date=" + date +
                '}';
    }


    public static class PriorityDateItemComparator<T> implements Comparator<PriorityDateItem<T>> {


        @Override
        public int compare(PriorityDateItem<T> o1, PriorityDateItem<T> o2) {
            int result = o2.getPriority() - o1.getPriority();
            if(result != 0){
                return result;
            }

            if(o1.getDate() > o2.getDate()){
                return -1;
            }
            return 1;
        }
    }
}
