USE schedule_db;

CREATE TABLE schedule_type (
    type_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '类型ID',
    type_name VARCHAR(16) NOT NULL COMMENT '类型名称',
    type_description VARCHAR(128) NOT NULL COMMENT '类型描述',
    bg_color VARCHAR(7) NOT NULL DEFAULT '#FFFFFF' COMMENT '背景色'
);


CREATE TABLE schedule (
    schedule_date DATE NOT NULL COMMENT '日期',
    start_time TIME NOT NULL COMMENT '开始时间',
    end_time TIME NOT NULL COMMENT '结束时间',
    title VARCHAR(32) NOT NULL COMMENT '日程标题',
    type_id INT NOT NULL COMMENT '类型ID',
    description VARCHAR(256) NOT NULL COMMENT '日程描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 定义联合主键
    PRIMARY KEY (schedule_date, start_time, end_time),

    -- 添加索引
    INDEX type_id (type_id),
    INDEX schedule_date (schedule_date)
);