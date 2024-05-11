$(document).ready(function () {
    // Проверяем наличие токена если нет то отправляем на регистрацию
    if (getToken() === null) {
        $("#registration").show()
    } else if (getToken() === "" || isTokenExpired(getToken())) {
        $("#authorization").show()
    } else {
         getAllTasks();
    }

    // Регистрация
    $("#registration-form").submit(function (e) {
        e.preventDefault();
        let json = convertFormToJson($(this))
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/v1/auth/registration",
            contentType: "application/json",
            dataType: "json",
            data: json,
            success: function () {
                alert('Вы успешно загеристрировались!');
                $("#registration").hide()
                $("#authorization").show()
            },
            error: function (xhr){
                if (xhr.status === 400){
                    $("#error-text-registration").html(getBindingErrorText(xhr));
                } else if(xhr.status === 409){
                    $("#error-text-registration").text("Пользователь с данной почтой уже существует")
                }
                else {
                    $("#error-text-registration").text("Ошибка сервера, пожалуйста попробуйте еще раз")
                }
                $(".error-message-registration").show()
            }
        });
    });

    //Авторизация
    $("#authorization-form").submit(function (e) {
        e.preventDefault();
        let json = convertFormToJson($(this))
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/v1/auth/login",
            contentType: "application/json",
            dataType: "json",
            data: json,
            success: function (result) {
                alert('Авторизация успешно пройдена!');
                setToken(result)
                $("#span-username").text(getUserEmail(result))
                // Рендинг задач пользователя

            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-authorization").html(getBindingErrorText(xhr));
                }
                else if (xhr.status === 403) {
                    $("#error-text-authorization").text("Почта или пароль неверны. Пожалуйста повторите попытку")
                }
                else if (xhr.status === 404) {
                    $("#error-text-authorization").text("Пользователь с такой почтой не найден")
                }
                else {
                    $("#error-text-authorization").text("Ошибка сервера, пожалуйста попробуйте еще раз")
                }
                $(".error-message-authorization").show()
            }
        });
    });

    // Выход из приложения
    $("#logout_btn").click(function (e){
        e.preventDefault();
        removeToken();
        $(".main-content").hide()
        $("#authorization").show();
    });

// Создание новой задачи
    $("#task-create-form").submit(function (e){
       e.preventDefault();
       let json = convertFormToJson($(this));
       let token = getToken();
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/api/v1/tasks",
            contentType: "application/json",
            dataType: "json",
            data: json,
            headers: {
                Authorization: 'Bearer ' + token
            },
            success: function (result) {
                alert('Задача ' + result.title + ' успешно создана');
                getAllTasks();
                $('#create-task').modal('hide');
            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-create").html(getBindingErrorText(xhr));
                } else {
                    $("#error-text-create").text("Ошибка сервера, пожалуйста попробуйте еще раз")
                }
                $(".error-message-create").show();
            }
        });
    });

    // Удаление задачи
    $(".card").on("click", ".remove-b", function() {
        let token = getToken();
        let id = $(this).attr('data-delete-taskId');
        $.ajax({
            type: "DELETE",
            url: "http://localhost:8080/api/v1/tasks/" + id,
            contentType: "application/json",
            dataType: "json",
            headers: {
                Authorization: 'Bearer ' + token
            },
            success: function () {
                alert('Задача удалена');
                getAllTasks();
            },
            error: function (error){
                //     Todo обработка ошибок
            }
        });
    });


    // Внесение данных о задаче, при нажатии кнопки изменить
    $(".card").on("click", ".update-b", function() {
        const detailsElement = $(this).closest('details');
        let title = detailsElement.find('summary').text()
        let description = detailsElement.find('p').text()
        let taskId = $(this).attr('data-update-taskId');

        $(".error-message-update").hide();
        $("#update-id").val(taskId)
        $("#update-title").val(title);
        $("#update-description").val(description);
    });

    // Изменение задачи
    $("#task-update-form").submit(function (e){
       e.preventDefault();
       let token = getToken();
       let json = convertFormToJson($(this));
       let taskId = $(this).find('#update-id').val();
       $.ajax({
           type: "PATCH",
           url: "http://localhost:8080/api/v1/tasks/" + taskId,
           contentType: "application/json",
           dataType: "json",
           data: json,
           headers: {
               Authorization: 'Bearer ' + token
           },
           success: function () {
               alert('Задача изменена');
               $('#update-task').modal('hide');
               getAllTasks();
           },
           error: function (xhr) {
               if (xhr.status === 400) {
                   $("#error-text-update").html(getBindingErrorText(xhr));
               } else {
                   $("#error-text-update").text("Ошибка сервера, пожалуйста попробуйте еще раз")
               }
               $(".error-message-update").show();
           }
       });
    });
    // Завершение задачи
    $(".card").on("click", ".complite-b", function() {
        let token = getToken();
        let taskId = $(this).attr('data-complite-taskId');
        const detailsElement = $(this).closest('details');
        let title = detailsElement.find('summary').text()
        let description = detailsElement.find('p').text()
        let dto = {title:title, description:description, iscomplited:true}

        $.ajax({
            type: "PATCH",
            url: "http://localhost:8080/api/v1/tasks/" + taskId,
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(dto),
            headers: {
                Authorization: 'Bearer ' + token
            },
            success: function () {
                alert('Задача завешена');
                getAllTasks();
            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-update").html(getBindingErrorText(xhr));
                } else {
                    $("#error-text-update").text("Ошибка сервера, пожалуйста попробуйте еще раз")
                }
                $(".error-message-update").show();
            }
        });
    });

    // Вернуть задачу из сделанных
    $(".card").on("click", ".unfinish-b", function() {
        let token = getToken();
        let taskId = $(this).attr('data-unfinish-taskId');
        const detailsElement = $(this).closest('details');
        let title = detailsElement.find('summary').text()
        let description = detailsElement.find('p').text()
        let dto = {title:title, description:description, iscomplited:false}

        $.ajax({
            type: "PATCH",
            url: "http://localhost:8080/api/v1/tasks/" + taskId,
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(dto),
            headers: {
                Authorization: 'Bearer ' + token
            },
            success: function () {
                alert('Задача возвращена');
                getAllTasks();
            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-update").html(getBindingErrorText(xhr));
                } else {
                    $("#error-text-update").text("Ошибка сервера, пожалуйста попробуйте еще раз")
                }
                $(".error-message-update").show();
            }
        });

    });





        // Скрыть - показать пароль
        $(".toggle-password").click(function () {
            var input = $(this).prev();
            var type = input.attr('type') === 'password' ? 'text' : 'password';
            input.attr('type', type);
            $(this).text(type === 'password' ? 'Показать' : 'Скрыть');
        });






    });





// Преобразование формы в JSON
    function convertFormToJson(form) {
        let form_data_array = form.serializeArray();
        let form_data = {};
        $.each(form_data_array, function (index, field) {
            form_data[field.name] = field.value;
        });
        return JSON.stringify(form_data);
    }


// Сохранить токен в хранилище
    function setToken(json) {
        localStorage.setItem("access_token", json.token);
    }

// Получаем токен из хранилища
    function getToken() {
        return localStorage.getItem("access_token");
    }

// Проверить токен на время жизни
    function isTokenExpired(token) {
        const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;
        return (Math.floor((new Date()).getTime() / 1000)) >= expiry;
    }
// Получить почту пользователя
function getUserEmail(token){
    return (JSON.parse(atob(token.split('.')[1]))).sub;
}

// Удаление токена из хранилища
function removeToken(){
        localStorage.setItem("access_token","");
}
//Получить все задачи
function getAllTasks(){
        let token = getToken();
         $.ajax({
           type: "GET",
           url: "http://localhost:8080/api/v1/tasks",
           contentType: "application/json",
           dataType: "json",
           headers: {
               Authorization: 'Bearer ' + token
           },
           success: function (result) {
               const unfulfilledTasks = result.filter(task => !task.iscomplited);
               const doneTasks = result.filter(task => task.iscomplited);
               let containerTask = $("#done-tasks").empty();
               let containerTask2 = $("#unfulfilled-tasks").empty();
               doneTasks.forEach(task=>{
                   let card = createCard(task, 'unfinish');
                   containerTask.append(card)
               });
               unfulfilledTasks.forEach(task=>{
                   let card = createCard(task, 'complite');
                   containerTask2.append(card)
               })

           },
           error: function (xhr){

           }
        })
}

function createCard(task, compliteBtn){
        const li = $('<div>').addClass('list-group-item');
        const details = $('<details>');
        const summary = $('<summary>').text(task.title);
        const description = $('<p>').text(task.description);
        const remove_btn = $('<button>').text('Удалить').addClass('remove-b').attr('data-delete-taskId', task.id);
        const update_btn = $('<button>').text('Изменить').addClass('update-b').attr({'data-bs-toggle': 'modal',
            'data-bs-target': '#update-task',
            'data-update-taskId': task.id
        });
        const complite_btn = $('<button>').text(compliteBtn === 'complite' ? 'Завершить' : 'Вернуть')
            .addClass(compliteBtn + '-b').attr('data-' + compliteBtn + '-taskId', task.id);

        details.append(summary);
        details.append(description);
        details.append(remove_btn);
        details.append(update_btn);
        details.append(complite_btn);

        li.append(details);
        return li;
}

function getBindingErrorText(xhr) {
    let errorMessage = JSON.parse(xhr.responseText);
    let errorText = "";
    errorMessage.errors.forEach(function (error) {
        errorText += `<i class="bi bi-exclamation-triangle"></i> ${error}<br>`;
    });
    return errorText;
}




