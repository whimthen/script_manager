package io.github.whimthen.script.enums;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 08 下午6:0508 | 八月. 星期三
 */
public enum SysCode {

    SYSTEM_ERROR(1000, "系统错误 :)"),
    PARAM_MISSING(1001, "亲亲, 你少了<font color=\"red\"> [ 参数 ] </font>知道吗?"),
    REPLICATE_DATA(1002, "Oh my god! 数据已经存在了!"),

    ;

    SysCode(int code, String message) {
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
