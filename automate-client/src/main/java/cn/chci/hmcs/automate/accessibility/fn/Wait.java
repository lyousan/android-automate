package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.core.AndroidBot;
import cn.chci.hmcs.automate.exception.TimeoutException;
import cn.chci.hmcs.automate.socket.Client;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author 有三
 * @date 2022-08-26 20:28
 * @description
 **/
@Slf4j
public class Wait {
    private AndroidBot bot;
    private Client client;
    @Getter
    private final WaitOptions waitOptions;

    private final ExecutorService WORKER;
    private CountDownLatch cdl;

    public Wait(AndroidBot bot, WaitOptions waitOptions) {
        if (bot == null || waitOptions == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.bot = bot;
        this.waitOptions = waitOptions;
        this.WORKER = Executors.newSingleThreadExecutor(r -> new Thread(r, "WaitWorker-" + bot.getUdid()));
    }

    public Wait(Client client, WaitOptions waitOptions) {
        if (client == null || waitOptions == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.client = client;
        this.waitOptions = waitOptions;
        this.WORKER = Executors.newSingleThreadExecutor(r -> new Thread(r, "WaitWorker-" + client.getUdid()));
    }

    /*public <V> V until(Function<? super AndroidBot, V> isTrue) {
        long stopTime = waitOptions.calcStopTime();
        while (System.currentTimeMillis() < stopTime) {
            V value = isTrue.apply(bot);
            // 为true或任意非null值时正常返回
            if (value != null && (Boolean.class != value.getClass() || Boolean.TRUE.equals(value))) {
                return value;
            }
            waitOptions.waiting();
        }
        throw new TimeoutException(String.format("超时：条件 ==> %s; 时间：%s", isTrue, waitOptions));
    }*/

    @SneakyThrows
    public <V> V until(Function<? super AndroidBot, V> isTrue) {
        return until(isTrue, null);
    }

    @SneakyThrows
    public <V> V until(Function<? super AndroidBot, V> isTrue, V defaultValue) {
        cdl = new CountDownLatch(1);
        long stopTime = waitOptions.calcStopTime();
        Future<V> task = WORKER.submit(() -> {
            while (System.currentTimeMillis() < stopTime) {
                V value = isTrue.apply(bot);
                // 为true或任意非null值时正常返回
                if (value != null && (Boolean.class != value.getClass() || Boolean.TRUE.equals(value))) {
                    cdl.countDown();
                    return value;
                }
                waitOptions.waiting();
            }
            cdl.countDown();
            return null;
        });
        boolean await = cdl.await(waitOptions.getTimeout(), waitOptions.getTimeUnit());
        if (await) {
            return task.get();
        }
        return defaultValue;
    }

    @SneakyThrows
    public <V> V untilWithThrow(Function<? super AndroidBot, V> isTrue) {
        cdl = new CountDownLatch(1);
        long stopTime = waitOptions.calcStopTime();
        Future<V> task = WORKER.submit(() -> {
            while (System.currentTimeMillis() < stopTime) {
                V value = isTrue.apply(bot);
                // 为true或任意非null值时正常返回
                if (value != null && (Boolean.class != value.getClass() || Boolean.TRUE.equals(value))) {
                    cdl.countDown();
                    return value;
                }
                waitOptions.waiting();
            }
            cdl.countDown();
            return null;
        });
        boolean await = cdl.await(waitOptions.getTimeout(), waitOptions.getTimeUnit());
        if (await) {
            return task.get();
        }
        throw new TimeoutException(String.format("超时：条件 ==> %s; 时间：%s", isTrue, waitOptions));
    }

    @SneakyThrows
    <V> V implicitUntil(Function<? super Client, V> isTrue, V defaultValue) {
        cdl = new CountDownLatch(1);
        long stopTime = waitOptions.calcStopTime();
        Future<V> task = WORKER.submit(() -> {
            while (System.currentTimeMillis() < stopTime) {
                V value = isTrue.apply(client);
                // 为true或任意非null值时正常返回
                if (value != null && (Boolean.class != value.getClass() || Boolean.TRUE.equals(value))) {
                    cdl.countDown();
                    return value;
                }
                waitOptions.waiting();
            }
            cdl.countDown();
            return null;
        });
        boolean await = cdl.await(waitOptions.getTimeout(), waitOptions.getTimeUnit());
        if (await) {
            return task.get();
        }
        return defaultValue;
    }
}
