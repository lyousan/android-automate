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

    public static By className(String className) {
        String xpath = "//*[@class=\"" + className + "\"]";
        return new By(xpath, "className", className);
    }

    public static By contentDesc(String contentDesc) {
        String xpath = "//*[@content-desc=\"" + contentDesc + "\"]";
        return new By(xpath, "contentDesc", contentDesc);
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
