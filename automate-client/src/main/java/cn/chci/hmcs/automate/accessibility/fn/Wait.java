package cn.chci.hmcs.automate.accessibility.fn;

import cn.chci.hmcs.automate.core.AndroidBot;
import cn.chci.hmcs.automate.exception.TimeoutException;

import java.util.function.Function;

/**
 * @author 有三
 * @date 2022-08-26 20:28
 * @description
 **/
public class Wait {
    private AndroidBot bot;
    private WaitOptions waitOptions;

    public Wait(AndroidBot bot, WaitOptions waitOptions) {
        if (bot == null || waitOptions == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        this.bot = bot;
        this.waitOptions = waitOptions;
    }

    public <V> V until(Function<? super AndroidBot, V> isTrue) {
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
    }
}
