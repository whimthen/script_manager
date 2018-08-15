package io.github.whimthen.script.entity;

/**
 * @project: script_manager
 * @created: with IDEA
 * @author: nzlong
 * @Date: 2018 08 15 下午3:3230 | 八月. 星期三
 */
public class SSHInfo {

    private String user;
    private String password;
    private String host;
    private int port;

    private SSHInfo() {

    }

    private SSHInfo(String user, String password, String host, int port) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public static SSHInfo newInstance(String user, String password, String host, int port) {
        return new SSHInfo(user, password, host, port);
    }

    public static SSHInfo newInstance() {
        return new SSHInfo();
    }

    public String getUser() {
        return user;
    }

    public SSHInfo setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SSHInfo setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHost() {
        return host;
    }

    public SSHInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public SSHInfo setPort(int port) {
        this.port = port;
        return this;
    }

}
