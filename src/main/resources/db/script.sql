create table if not exists  script(
    id INTEGER primary key autoincrement ,
    alias varchar(100) default null,    -- 别名
    content text default null,          -- 内容
    last_run_time long default null,    -- 最后一次执行时间
    create_time long default null       -- 创建时间
);


create table if not exists server(
    id INTEGER primary key autoincrement,
    alias varchar(100) default null,    -- 别名
    host varchar(20) default null,      -- Host
    port int default 22,                -- 端口
    user varchar(100) default null,     -- 用户名
    password varchar(200) default null, -- 密码
    status int default 1,               -- 1-有效、-1无效
    create_time long default null,      -- 创建时间
    update_time long default null,      -- 修改时间
    last_login_time long default null   -- 最后一次登录的时间
)