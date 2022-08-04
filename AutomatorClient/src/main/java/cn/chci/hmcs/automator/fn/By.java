package cn.chci.hmcs.automator.fn;

public class By {
    String xpath;
    String type;
    String param;

    public By(String xpath, String type, String param) {
        this.xpath = xpath;
        this.type = type;
        this.param = param;
    }

    public static By xpath(String xpath) {
        return new By(xpath, "xpath", xpath);
    }

    public static By id(String id) {
        String xpath = "//*[@resource-id=\"" + id + "\"]";
        return new By(xpath, "id", id);
    }

    public static By classNameEquals(String className) {
        String xpath = "//*[@class=\"" + className + "\"]";
        return new By(xpath, "classNameEquals", className);
    }

    public static By classNameContains(String className) {
        String xpath = "//*[contains(@class,\"" + className + "\")]";
        return new By(xpath, "classNameEquals", className);
    }

    public static By textEquals(String text) {
        String xpath = "//*[@text=\"" + text + "\"]";
        return new By(xpath, "textEquals", text);
    }

    public static By textContains(String text) {
        String xpath = "//*[contains(@text,\"" + text + "\")]";
        return new By(xpath, "textContains", text);
    }

    public static By contentDescEquals(String contentDesc) {
        String xpath = "//*[@content-desc=\"" + contentDesc + "\"]";
        return new By(xpath, "contentDescEquals", contentDesc);
    }

    public static By contentDescContains(String contentDesc) {
        String xpath = "//*[contains(@content-desc,\"" + contentDesc + "\")]";
        return new By(xpath, "contentDescContains", contentDesc);
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }


}
