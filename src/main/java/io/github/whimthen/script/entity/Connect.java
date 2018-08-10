package io.github.whimthen.script.entity;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Table;

@Table(name = "server")
public class Connect extends Model {

    private Integer id;

    private String host;

    private Integer port;

    private String user;

    private String password;

    private String alias;

    private Long createTime;

    private Long lastLoginTime;

    private Long updateTime;

    public Long getUpdateTime() {
        return updateTime;
    }

    public Connect setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    private int status;

    public int getStatus() {
        return status;
    }

    public Connect setStatus(int status) {
        this.status = status;
        return this;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public Connect setCreateTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public Connect setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Connect setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Connect setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public Connect setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getUser() {
        return user;
    }

    public Connect setUser(String user) {
        this.user = user;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Connect setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public Connect setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public static Connect newInstance() {
        return new Connect();
    }

    @Override
    public String toString() {
        return "Connect{" + "id=" + id + ", host='" + host + '\'' + ", port=" + port + ", user='" + user + '\'' + ", password='" + password + '\'' + ", alias='" + alias + '\'' + '}';
    }
}
