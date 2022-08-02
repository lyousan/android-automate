package cn.chci.hmcs.automator.model;

public class Command {
    private String commandName;
    private String targetName;
    private String methodName;
    // 经过json序列化后Class[]类型会变成以全类名为内容的String[]，但是我们反射的时候需要还原回Class[]，在执行command的时候会对此进行还原
    private String[] paramsType;
    private Class<?>[] realParamsType;
    // 因为无法指定具体的参数类型，所以在json反序列化后参数会变成JSONObject类型，在执行command的时候也需要根据paramsType将params全部转换成真正的参数类型
    private Object[] params;

    public Command() {
    }

    public Command(String targetName, String methodName, String[] paramsType, Object[] params) {
        this.targetName = targetName;
        this.methodName = methodName;
        this.paramsType = paramsType;
        this.params = params;
    }

    public Command(String commandName, String targetName, String methodName, String[] paramsType, Object[] params) {
        this.commandName = commandName;
        this.targetName = targetName;
        this.methodName = methodName;
        this.paramsType = paramsType;
        this.params = params;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(String[] paramsType) {
        this.paramsType = paramsType;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class<?>[] getRealParamsType() {
        return realParamsType;
    }

    public void setRealParamsType(Class<?>[] realParamsType) {
        this.realParamsType = realParamsType;
    }
}
