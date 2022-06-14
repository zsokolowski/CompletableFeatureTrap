package eu.zsokolowski.completablefuturetrap;

public enum TimeBox {
    VERY_LONG_TIME(1000),
    SHORT_TIME(3);

    private final int duration;

    TimeBox(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "TimeBox {"
            + "duration=" + duration
            + '}';
    }
}
