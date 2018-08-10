package io.github.whimthen.script.entity;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;

@Table(name = "server")
public class ConnectRes extends Model {

    private Integer id;

    private String host;

    private Integer port;

    private String user;

    private String alias;

    private Long createTime;

    private Long lastLoginTime;

    private int status;

    public int getStatus() {
        return status;
    }

    public ConnectRes setStatus(int status) {
        this.status = status;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ConnectRes setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public ConnectRes setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public ConnectRes setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ConnectRes setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public ConnectRes setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUser() {
        return user;
    }

    public ConnectRes setUser(String user) {
        this.user = user;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public ConnectRes setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public static ConnectRes newInstance() {
        return new ConnectRes();
    }

}
