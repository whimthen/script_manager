package io.github.whimthen.script.cache;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import io.github.whimthen.script.entity.SSHInfo;
import io.github.whimthen.script.entity.ServerUserInfo;
import io.github.whimthen.script.enums.SysCode;
import io.github.whimthen.script.exception.ScriptException;
import io.github.whimthen.script.utils.EncryptUtils;
import io.github.whimthen.script.utils.GlobalUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 15 下午5:3834 | 八月. 星期三
 */
public class SessionManager {

    private static SessionManager sessionManager;

    private static ConcurrentHashMap<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    static {
        sessionManager = new SessionManager();
    }

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public static Session put(SSHInfo sshInfo) throws Exception {
        checkSSHInfo(sshInfo);
        return createSession(sshInfo);
    }

    public static Session getSession(SSHInfo sshInfo) throws Exception {
        checkSSHInfo(sshInfo);
        Session session = SESSION_MAP.get(getKey(sshInfo));
        if (session == null || !session.isConnected()) {
            return createSession(sshInfo);
        }
        return session;
    }

    public static void remove(SSHInfo sshInfo) {
        SESSION_MAP.remove(getKey(sshInfo));
    }

    public static void clear() {
        SESSION_MAP.clear();
    }

    private static String getKey(SSHInfo sshInfo) {
        return sshInfo.getHost() + sshInfo.getUser() + sshInfo.getPort();
    }

    private static void checkSSHInfo(SSHInfo sshInfo) throws Exception {
        if (sshInfo == null || GlobalUtils.isAnyBlank(sshInfo.getHost(), sshInfo.getPassword(), sshInfo.getUser(), sshInfo.getPort() + "")) {
            throw new ScriptException(SysCode.PARAM_MISSING);
        }
    }

    private static Session createSession(SSHInfo sshInfo) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(sshInfo.getUser(), sshInfo.getHost(), sshInfo.getPort());
        session.setPassword(EncryptUtils.dbDecrypt(sshInfo.getPassword(), sshInfo.getUser()));
        UserInfo userInfo = ServerUserInfo.newInstance();
        session.setUserInfo(userInfo);
        SESSION_MAP.put(getKey(sshInfo), session);
        session.connect(30000);
        return session;
    }

}
