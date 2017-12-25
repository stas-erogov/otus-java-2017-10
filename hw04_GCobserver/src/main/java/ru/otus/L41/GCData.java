package ru.otus.L41;

public class GCData {

    private int count = 0;
    private long used_time = 0L;
    private String type;

    public GCData(String type) {
        this.type = type;
    }

    public void addStat(long used_time) {
        this.used_time += used_time;
        this.count++;
    }

    @Override
    public String toString() {
        return String.format("GC:%s\tperformed counts: %s times, elapsed time: %s ms", this.type, this.count, this.used_time);
    }

    public int getCount() {
        return count;
    }

    public long getUsedTime() {
        return used_time;
    }
}
