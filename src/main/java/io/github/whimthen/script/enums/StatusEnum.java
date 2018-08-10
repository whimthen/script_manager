package io.github.whimthen.script.enums;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 09 下午8:1556 | 八月. 星期四
 */
public enum StatusEnum {

    VALID(1, "有效"),
    NOT_VALID(-1, "无效"),

    ;

    StatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
