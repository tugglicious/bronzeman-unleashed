package com.elertan.utils;

import java.time.Duration;

public class TickUtils {

    private static final int SECONDS_PER_TICK_NUM = 3;  // 0.6 seconds per tick = 3/5
    private static final int SECONDS_PER_TICK_DEN = 5;

    public static Duration ticksToDuration(long ticks) {
        long q = ticks / SECONDS_PER_TICK_DEN;           // quotient
        long r = ticks % SECONDS_PER_TICK_DEN;           // remainder 0..4

//        long seconds = Math.multiplyExact(q, SECONDS_PER_TICK_NUM);
//        long extraSeconds = (r * (long) SECONDS_PER_TICK_NUM) / SECONDS_PER_TICK_DEN;
//        seconds = Math.addExact(seconds, extraSeconds);

        long seconds = q * SECONDS_PER_TICK_NUM; // safe: q<=Long.MAX/5, so q*3 fits in long
        long extraSeconds = (r * (long) SECONDS_PER_TICK_NUM) / SECONDS_PER_TICK_DEN;
        seconds = seconds + extraSeconds; // also safe

        int nanos = (int) (((r * (long) SECONDS_PER_TICK_NUM) % SECONDS_PER_TICK_DEN)
            * 200_000_000L); // 1/5 sec = 200ms

        return Duration.ofSeconds(seconds, nanos);
    }
}
