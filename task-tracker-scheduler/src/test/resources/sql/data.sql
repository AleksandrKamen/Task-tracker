INSERT INTO app_data.users (id, username, password, created_at, updated_at, role)
VALUES
 (1, 'test@gmail.com', 'test', now(), now(), 'USER'),
 (2, 'test2@gmail.com', 'test', now(), now(), 'USER'),
 (3, 'test3@gmail.com', 'test', now(), now(), 'USER'),
 (4, 'test4@gmail.com', 'test', now(), now(), 'USER');

INSERT INTO app_data.tasks (id, title, description, iscomplited, created_at, completed_at, id_user) VALUES
 (1, 'Первая задача', 'Описание первой задачи', true, '2024-05-05 12:00:00', NOW() - INTERVAL '1 hour', 1),
 (2, 'Вторая задача', 'Описание второй задачи', false, '2024-05-05 12:00:00', null,1),
 (3, 'Третья задача', 'Описание третьей задачи', true, '2024-05-05 12:00:00', NOW() - INTERVAL '3 hours',1),
 (4, 'Четвертая задача', 'Описание четвертой задачи', false, '2024-05-05 12:00:00', null,2),
 (5, 'Пятая задача', 'Описание пятой задачи', false, '2024-05-05 12:00:00', null,2),
 (6, 'Шестая задача', 'Описание шестой задачи', false, '2024-05-05 12:00:00', null,3),
 (7, 'Седьмая задача', 'Описание седьмой задачи', true, '2024-05-05 12:00:00', NOW() - INTERVAL '10 hours',4),
 (8, 'Восьмя задача', 'Описание восьмой задачи', true, '2024-05-05 12:00:00', NOW() - INTERVAL '30 hours',3);