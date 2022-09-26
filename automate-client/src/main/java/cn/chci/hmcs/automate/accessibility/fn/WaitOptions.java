package cn.chci.hmcs.automate.accessibility.fn;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author 有三
 * @date 2022-08-09 17:35
 * @description
 **/
public class WaitOptions {
    public static final long DEFAULT_TIMEOUT = 5L;
    public static final long DEFAULT_INTERVAL = 1L;
    public static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;
    public static final WaitOptions DEFAULT_WAIT_OPTIONS = new WaitOptions(DEFAULT_TIMEOUT, DEFAULT_INTERVAL, DEFAULT_TIMEUNIT);
    @Setter
    @Getter
    private long timeout = DEFAULT_TIMEOUT;
    @Setter
    @Getter
    private long interval = DEFAULT_INTERVAL;
    @Setter
    @Getter
    private TimeUnit timeUnit = DEFAULT_TIMEUNIT;

    public WaitOptions() {
    }

    public WaitOptions(long timeout, long interval, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.interval = interval;
        this.timeUnit = timeUnit;
    }

    long calcStopTime() {
        return System.currentTimeMillis() + timeUnit.toMillis(timeout);
    }

    @SneakyThrows
    void waiting() {
        timeUnit.sleep(interval);
    }

    @Override
    public String toString() {
        return String.format("timeout: %s, interval: %s, unit: %s", timeout, interval, timeUnit);
    }
}
