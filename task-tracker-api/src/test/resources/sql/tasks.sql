insert into app_data.tasks(id, title, description, iscompleted, created_at, completed_at, id_user)
VALUES
(1, 'Первая задача', 'Описание первой задачи', true, '2024-05-05 12:00:00', '2024-05-05 14:00:00',1),
(2, 'Вторая задача', 'Описание второй задачи', false, '2024-05-05 12:00:00', null,1),
(3, 'Третья задача', 'Описание третьей задачи', true, '2024-05-05 12:00:00', '2024-05-05 18:00:00',1),
(4, 'Четвертая задача', 'Описание четвертой задачи', false, '2024-05-05 12:00:00', null,1),
(5, 'Пятая задача', 'Описание пятой задачи', true, '2024-05-05 12:00:00', '2024-05-05 16:00:00',1);