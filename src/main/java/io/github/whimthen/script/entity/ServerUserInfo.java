package io.github.whimthen.script.entity;

import com.jcraft.jsch.UserInfo;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 15 下午3:3009 | 八月. 星期三
 */
public class ServerUserInfo implements UserInfo {

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean promptPassword(String s) {
        return false;
    }

    @Override
    public boolean promptPassphrase(String s) {
        return false;
    }

    @Override
    public boolean promptYesNo(String s) {
        return true;
    }

    @Override
    public void showMessage(String s) {

    }

    public static ServerUserInfo newInstance() {
        return new ServerUserInfo();
    }

}
