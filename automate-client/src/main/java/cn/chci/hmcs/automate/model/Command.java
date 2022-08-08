package cn.chci.hmcs.automate.model;

public class Command {
    private String commandName;
    private String targetName;
    private String methodName;
    private Class<?>[] paramsType;
    private Object[] params;

    public Command() {
    }

    public Command(String targetName, String methodName, Class<?>[] paramsType, Object[] params) {
        this.targetName = targetName;
        this.methodName = methodName;
        this.paramsType = paramsType;
        this.params = params;
    }

    public Command(String commandName, String targetName, String methodName, Class<?>[] paramsType, Object[] params) {
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

    public Class<?>[] getParamsType() {
        return paramsType;
    }

    public void setParamsType(Class<?>[] paramsType) {
        this.paramsType = paramsType;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
