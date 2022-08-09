package cn.chci.hmcs.automate.accessibility.fn;

import lombok.Getter;
import lombok.Setter;

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
    @Setter
    @Getter
    private long implicitTimeout = DEFAULT_TIMEOUT;
    @Setter
    @Getter
    private long interval = DEFAULT_INTERVAL;
    @Setter
    @Getter
    private TimeUnit implicitTimeUnit = DEFAULT_TIMEUNIT;

    public WaitOptions() {
    }

    public WaitOptions(long implicitTimeout, long interval, TimeUnit implicitTimeUnit) {
        this.implicitTimeout = implicitTimeout;
        this.interval = interval;
        this.implicitTimeUnit = implicitTimeUnit;
    }

    public static WaitOptions DEFAULT_WAIT_OPTIONS() {
        return new WaitOptions(DEFAULT_TIMEOUT, DEFAULT_INTERVAL, DEFAULT_TIMEUNIT);
    }

    public long calcStopTime() {
        return System.currentTimeMillis() + implicitTimeUnit.toMillis(implicitTimeout);
    }

    public void waiting() throws InterruptedException {
        implicitTimeUnit.sleep(interval);
    }
}
