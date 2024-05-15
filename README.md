# Обзор проекта (Планировщик задач)
![image](https://github.com/AleksandrKamen/Task-tracker/assets/144233016/51b182f9-00e4-499e-819c-10776a2afb9b)

**Техническое задание проекта** -  [https://zhukovsd.github.io/java-backend-learning-course/Projects/TaskTracker/](https://zhukovsd.github.io/java-backend-learning-course/Projects/TaskTracker/)

**Суть проекта** - Многопользовательский планировщик задач. Пользователи могут использовать его в качестве TODO листа.

**Используемые технологии/инструменты:**

•	[Maven](https://maven.apache.org/)   

•	[Spring](https://spring.io/projects/spring-boot) (Spring Boot, Spring Security, Spring Sessions, Spring Test, Spring Web, Spring AMQP, Spring Scheduler, Spring Mail) 

•	[Docker](https://www.docker.com/)

•	[Thymeleaf](https://www.thymeleaf.org/)

•	[Jquery](https://jquery.com/)

•	[Bootstrap](https://getbootstrap.com/)

•	[Testcontainers](https://testcontainers.com/)

•	[PostgreSQL](https://www.postgresql.org/)

•	[RabbitMQ](https://www.rabbitmq.com/)

•	[GitActions](https://docs.github.com/ru/actions)

**Структура базы данных**

В базе данных содержится информация о личных данных пользователя, зашифрованный пароль и также информация о дате регистрации, дате обновления пользователя и роли пользователя в системе.  
В таблице tasks содержится информация о задачах пользователей - название, описание, время создания и выполнения. 

![image](https://github.com/AleksandrKamen/Task-tracker/assets/144233016/60984b46-902f-4844-80e5-a8b1a9dee719)

**Функционал приложения**

• Регистрация

• Авторизация

• Создание/Редактирование/Удаление/Изменение задач пользователя

• Оповещение пользователей о выполненных задачах за последние 24 часа 

• Оповещение пользователей о невыполненных задачах  за уста

• Приветственное письмо при регистрации в приложении

**Сервисы**

• task-tracker-api - отвечает за взаимодействие с базой данных и авторизацию пользователей с помощью jwt

• task-tracker-frontend - исполнен по принципу single-page-application с использованием javaScript и jQuery.

• task-tracker-email-sender - отвечает за отправку писем пользователям

• task-tracker-scheduler - создает отчет о задачах и изменениях в них за сутки.

**Локальный запуск**

1. Установить docker и docker-compose на ваш компьютер - https://docs.docker.com/get-docker/
2. Клонировать данный репозиторий - https://github.com/AleksandrKamen/Task-tracker.git
3. Добавить в кореневой католог проекта файл .env и заполнить его по образцу 
<pre>
Пример .env файла  
RABBITMQ_QUEUE_NAME=EMAIL_SENDING_TASKS - название очереди в rabbitmq
RABBITMQ_USER=rabbit_user -  имя пользователя в rabbitmq
RABBITMQ_PASSWORD=rabbit_password - пароль пользователя в rabbitmq
POSTGRES_DATABASE=data - название базы данных 
POSTGRES_USER=postgres - имя пользователя в postgres
POSTGRES_PASSWORD=postgres пароль пользователя в postgres
JWT_SECRET_KEY=3cfa76ef14937c2c0ec519f8fc057a80fcd04a7450f4e1bcd0a7567c272e007b - пример секретного ключа для подписи jwt токена 
JWT_EXPIRATION_TIME=3600000 - время жизни токена  
MAIL_EMAIL_FROM=example@gmail.com - почта с которой будет отправлять сообщения 
MAIL_USERNAME=example@gmail.com
MAIL_PASSWORD=aaad ddws eytw mklg - сгенерированный пароль для вашей почты 
ADMINER_PORT=8082:8080 - порт на котором будет adminer
</pre>
4. Запусить приложение использоваа docker-compose-dev
<pre>docker-compose -f docker-compose-dev.yml up</pre>
  

