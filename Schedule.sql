CREATE DATABASE 'schedule_db';


CREATE TABLE schedule_type (
    type_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '类型ID',
    type_name VARCHAR(16) NOT NULL COMMENT '类型名称',
    type_description VARCHAR(128) NOT NULL COMMENT '类型描述'
);


INSERT INTO schedule_type (type_id, type_name, type_description) 
VALUES (011, '工作', '工作赚钱，立身之本...'),
       (012, '学习', '学习新的知识'),
       (013, '睡眠', '良好的睡眠有助于生活'),
       (021, '休息', '劳逸结合'),
       (022, '娱乐', '有助于消除压力，调节情绪'),
       (023, '社交', '走亲访友'),
       (051, '其他', '...');


CREATE TABLE schedule_record (
    date DATE NOT NULL COMMENT '日期',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    title VARCHAR(32) NOT NULL COMMENT '日程标题',
    type_id INT NOT NULL COMMENT '类型ID',
    schedule_description VARCHAR(256) NOT NULL COMMENT '日程描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 定义联合主键
    PRIMARY KEY (start_time, end_time),

    -- 添加索引
    INDEX idx_type_id (type_id),
    INDEX idx_date (date)
);
