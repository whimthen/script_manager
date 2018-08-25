package io.github.whimthen.script.cache;

import com.blade.kit.StringKit;
import io.github.whimthen.script.constant.GlobalConstant;
import io.github.whimthen.script.entity.SSHInfo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 16 下午4:3040 | 八月. 星期四
 */
public class ChannelTypeManager {

    private static ChannelTypeManager typeManager;

    private static ConcurrentHashMap<String, String> TYPE_MAP = new ConcurrentHashMap<>();

    static {
        typeManager = new ChannelTypeManager();
    }

    public static void put(SSHInfo sshInfo, String type) {
        TYPE_MAP.put(getKey(sshInfo), type);
    }

    public static String get(SSHInfo sshInfo) {
        String type = TYPE_MAP.get(getKey(sshInfo));
        if (StringKit.isBlank(type)) {
            return GlobalConstant.JSCH_CHANNEL_TYPE_SHELL;
        }
        return type;
    }

    private static String getKey(SSHInfo sshInfo) {
        return sshInfo.getHost() + sshInfo.getUser() + sshInfo.getPort() + sshInfo.getPassword();
    }

}
