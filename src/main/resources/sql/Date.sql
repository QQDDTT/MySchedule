INSERT INTO schedule_type (type_id, type_name, type_description) 
VALUES (011, '工作', '工作赚钱，立身之本...'),
       (012, '学习', '学习新的知识'),
       (013, '睡眠', '良好的睡眠有助于生活'),
       (021, '休息', '劳逸结合'),
       (022, '娱乐', '有助于消除压力，调节情绪'),
       (023, '社交', '走亲访友'),
       (051, '其他', '...');

INSERT INTO schedule_record (schedule_date, start_time, end_time, title, type_id, description)
VALUES (CURRENTDATE, '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
       (CURRENTDATE, '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
       (CURRENTDATE, '08:00:00', '18:00:00', '工作', 011, 'Working...'),
       (CURRENTDATE, '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
       (CURRENTDATE, '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
       (CURRENTDATE, '22:00:00', '24:00:00', '睡眠', 013, 'Sleeping...'),
       ('20241011', '00:00:00', '06:00:00', '睡眠', 013, 'Sleeping...'),
       ('20241011', '06:00:00', '08:00:00', '通勤', 051, 'Going...'),
       ('20241011', '08:00:00', '18:00:00', '工作', 011, 'Working...'),
       ('20241011', '18:00:00', '20:00:00', '通勤', 051, 'Going...'),
       ('20241011', '20:00:00', '22:00:00', '娱乐', 022, 'Playing...'),
       ('20241011', '22:00:00', '24:00:00', '睡眠', 013, 'Sleeping...');