package io.github.whimthen.script.exception;

import io.github.whimthen.script.enums.SysCode;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 08 下午6:0305 | 八月. 星期三
 */
public class ScriptException extends Exception {

    private int code;

    public ScriptException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ScriptException(SysCode sysCode) {
        super(sysCode.getMessage());
        this.code = sysCode.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
