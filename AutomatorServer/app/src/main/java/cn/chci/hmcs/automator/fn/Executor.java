package cn.chci.hmcs.automator.fn;

import android.util.Log;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import cn.chci.hmcs.automator.dto.Response;
import cn.chci.hmcs.automator.model.Command;

public class Executor {
    private static final String LOG_TAG = "hmcs-automator-Executor";

    public static Response execute(Command command) {
        try {
            // 经过json反序列化后会丢失一些类型信息，需要根据paramsType对params进行转换
            convertParams(command);
            /*if (!) {
                return Response.clientFail("参数错误");
            }*/
            Object target = obtainTarget(command.getTargetName());
            Method method = obtainMethod(target, command);
            Object result = null;
            Log.d(LOG_TAG, "command perform: " + command.getTargetName() + "-" + command.getMethodName());
            Log.d(LOG_TAG, "command param: " + (command.getParams() == null || command.getParams().length == 0 ? "none params" : Arrays.toString(command.getParams())));
            long begin = System.currentTimeMillis();
            if (command.getParams() != null) {
                result = method.invoke(target, command.getParams());
            } else {
                result = method.invoke(target);
            }
            long end = System.currentTimeMillis();
            Log.d(LOG_TAG, "command finished: " + command.getTargetName() + "-" + command.getMethodName() + ", spend time: " + (end - begin) + "ms");
            Log.d(LOG_TAG, "command result: " + result.toString());
            return Response.success("执行成功", result);
        } catch (Exception e) {
            Log.e(LOG_TAG, "execute error: ", e);
            return Response.serverFail("执行失败: " + e.getMessage());
        }
    }

    private static void convertParams(Command command) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object[] paramsType = command.getParamsType();
        Class<?>[] classes = new Class[paramsType.length];
        Object[] params = command.getParams();
        Object param;
        JSONObject jsonObject;
        Field field;
        for (int i = 0; i < paramsType.length; i++) {
            // 还原参数真实的Class类型
            classes[i] = Class.forName(paramsType[i].toString());
            // 转换参数的类型
            if (params[i] instanceof JSONObject) {
                param = classes[i].newInstance();
                jsonObject = ((JSONObject) params[i]);
                Set<String> fields = jsonObject.keySet();
                for (String fieldName : fields) {
                    field = classes[i].getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(param, jsonObject.get(fieldName));
                }
                params[i] = param;
            }
        }
        command.setRealParamsType(classes);
    }

    private static Object obtainTarget(String clazzName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return Class.forName("cn.chci.hmcs.automator.fn." + clazzName).newInstance();
    }

    private static Method obtainMethod(Object target, Command command) throws NoSuchMethodException {
        return target.getClass().getDeclaredMethod(command.getMethodName(), command.getRealParamsType());
    }
}
