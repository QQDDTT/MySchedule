-- 白色 (White): #FFFFFF
-- 黑色 (Black): #000000
-- 红色 (Red): #FF0000
-- 绿色 (Green): #00FF00
-- 蓝色 (Blue): #0000FF
-- 黄色 (Yellow): #FFFF00
-- 紫色 (Purple): #800080
-- 青色 (Cyan): #00FFFF
-- 灰色 (Gray): #808080
-- 橙色 (Orange): #FFA500
-- 粉色 (Pink): #FFC0CB
-- 棕色 (Brown): #A52A2A

TRUNCATE TABLE schedule_type;

INSERT INTO schedule_type (type_id, type_name, type_description, bg_color) 
VALUES (011, '工作', '专注于工作与职业发展，努力实现目标。', '#007BFF'),  -- 蓝色，代表专业与冷静
       (012, '学习', '追求新知，提升个人能力与素养。', '#28A745'),  -- 绿色，象征成长与希望
       (013, '睡眠', '保证充足的睡眠以恢复精力，促进健康。', '#6C757D'),  -- 灰色，传达平静与放松
       (021, '休息', '适时放松，调节身心以提高效率。', '#FFC107'),  -- 黄色，代表轻松与愉悦
       (022, '娱乐', '进行休闲活动，放松心情，增进快乐。', '#FF5722'),  -- 橙色，体现活力与快乐
       (023, '社交', '与朋友和家人交流，增进感情与联系。', '#17A2B8'),  -- 青色，传达友好与沟通
       (051, '其他', '其他类型的安排与活动，灵活应对。', '#FFFFFF');  -- 白色，代表多样性与灵活性


TRUNCATE TABLE schedule;

INSERT INTO schedule (schedule_date, start_time, end_time, title, type_id, description)
VALUES 
-- 第一天
(CURRENT_DATE, '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
(CURRENT_DATE, '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
(CURRENT_DATE, '08:00:00', '18:00:00', '工作', 011, 'Working...'),
(CURRENT_DATE, '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
(CURRENT_DATE, '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
(CURRENT_DATE, '22:00:00', '23:59:59', '睡眠', 013, 'Sleeping...'),

-- 第二天
(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '08:00:00', '18:00:00', '工作', 011, 'Working...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 1 DAY), '22:00:00', '23:59:59', '睡眠', 013, 'Sleeping...'),

-- 第三天
(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '08:00:00', '18:00:00', '工作', 011, 'Working...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), '22:00:00', '23:59:59', '睡眠', 013, 'Sleeping...'),

-- 第四天
(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '08:00:00', '18:00:00', '工作', 011, 'Working...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY), '22:00:00', '23:59:59', '睡眠', 013, 'Sleeping...'),

-- 第五天
(DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY), '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY), '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY), '08:00:00', '18:00:00', '工作', 011, 'Working...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY), '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY), '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 4 DAY), '22:00:00', '23:59:59', '睡眠', 013, 'Sleeping...'),

-- 第六天
(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '08:00:00', '18:00:00', '工作', 011, 'Working...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 5 DAY), '22:00:00', '23:59:59', '睡眠', 013, 'Sleeping...'),

-- 第七天
(DATE_ADD(CURRENT_DATE, INTERVAL 6 DAY), '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 6 DAY), '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 6 DAY), '08:00:00', '18:00:00', '工作', 011, 'Working...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 6 DAY), '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 6 DAY), '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
(DATE_ADD(CURRENT_DATE, INTERVAL 6 DAY), '22:00:00', '23:59:59', '睡眠', 013, 'Sleeping...');
