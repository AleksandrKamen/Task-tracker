const host = location.host;
$(document).ready(function () {
    if (getToken() === null) {
        showElementAndHideElements("#registration", ".navbar-text", "#index")
    } else if (isTokenInvalid(getToken())) {
        showElementAndHideElements("#authorization", ".navbar-text", "#index")
    } else {
        showElementAndHideElements(".navbar-text", ".error-message-index")
        $("#span-username").text(getUserEmailFromToken(getToken()))
        getAllUsersTasks();
    }

    $("#registration-form").submit(function (e) {
        e.preventDefault();
        let json = convertFormToJson($(this))
        $.ajax({
            type: "POST",
            url: `http://${host}:8080/api/v1/auth/registration`,
            contentType: "application/json",
            dataType: "json",
            data: json,
            success: function () {
                $("#registration-form")[0].reset();
                showElementAndHideElements("#authorization", "#registration")
            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-registration").html(getBindingErrorText(xhr));
                } else {
                    $("#error-text-registration").html(getErrorText(xhr));
                }
                $(".error-message-registration").show();
            }
        });
    });

    $("#authorization-form").submit(function (e) {
        e.preventDefault();
        let json = convertFormToJson($(this))
        $.ajax({
            type: "POST",
            url: `http://${host}:8080/api/v1/auth/login`,
            contentType: "application/json",
            dataType: "json",
            data: json,
            success: function (result) {
                setTokenInLocaleStorage(result)
                location.reload();
            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-authorization").html(getBindingErrorText(xhr));
                } else {
                    $("#error-text-authorization").html(getErrorText(xhr))
                }
                $(".error-message-authorization").show()
            }
        });
    });

    $("#logout_btn").click(function (e) {
        e.preventDefault();
        removeTokenFromLocaleStorage();
        location.reload();
    });

    $("#task-create-form").submit(function (e) {
        e.preventDefault();
        let json = convertFormToJson($(this));
        $.ajax({
            type: "POST",
            url: `http://${host}:8080/api/v1/tasks`,
            contentType: "application/json",
            dataType: "json",
            data: json,
            headers: {
                Authorization: 'Bearer ' + getToken()
            },
            success: function () {
                getAllUsersTasks();
                $('#task-create-form')[0].reset();
                $('#create-task').modal('hide');
            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-create").html(getBindingErrorText(xhr));
                } else {
                    $("#error-text-create").html(getErrorText(xhr));
                }
                $(".error-message-create").show();
            }
        });
    });

    $("#delete-button").click(function () {
        const form = $(this).closest('#task-update-form');
        let taskId = form.find('#update-id').val();
        deleteTask(taskId)
    });

    $(".card").on("click", ".delete-btn", function () {
        const li = $(this).closest('.list-group-item');
        let taskId = li.find('.task-id').val();
        deleteTask(taskId)
    });

    $(".card").on("click", ".tasks-button-info", function () {
        const li = $(this).closest('.list-group-item');
        let taskId = li.find('.task-id').val();
        let title = li.find('.task-title').val()
        let description = li.find('.task-description').val()
        let iscompleted = li.find('.task-iscompleted').val()

        $(".error-message-update").hide();
        $("#update-id").val(taskId)
        $("#update-title").val(title);
        $("#update-description").val(description);
        $("#iscompleted").prop("checked", iscompleted === 'true')
    });


    $("#update-description, #update-title, #iscompleted").on("input change", function () {
        $(this).closest('form').submit();
    });

    $("#task-update-form").submit(function (e) {
        e.preventDefault();
        let json = convertFormToJson($(this));
        let taskId = $(this).find('#update-id').val();
        $.ajax({
            type: "PATCH",
            url: `http://${host}:8080/api/v1/tasks/` + taskId,
            contentType: "application/json",
            dataType: "json",
            data: json,
            headers: {
                Authorization: 'Bearer ' + getToken()
            },
            success: function () {
                getAllUsersTasks();
            },
            error: function (xhr) {
                if (xhr.status === 400) {
                    $("#error-text-update").html(getBindingErrorText(xhr));
                } else {
                    $("#error-text-update").html(getErrorText(xhr));
                }
                $(".error-message-update").show();
            }
        });
    });

    $(".card").on("click", ".update-btn", function () {
        const li = $(this).closest('.list-group-item');
        let taskId = li.find('.task-id').val();
        let title = li.find('.task-title').val();
        let description = li.find('.task-description').val();
        let iscompleted = $(this).data('iscompleted');
        updateTask(taskId, title, description, iscompleted);
    });

    $("#registration-link").click(function () {
        showElementAndHideElements("#registration", "#authorization")
        $("#error-text-authorization").empty()
    });

    $("#authorization-link").click(function () {
        showElementAndHideElements("#authorization", "#registration")
        $("#error-text-registration").empty()
    });

    $("#create-task, #update-task").draggable({});

    $(".toggle-password").click(function () {
        var input = $(this).prev();
        var type = input.attr('type') === 'password' ? 'text' : 'password';
        input.attr('type', type);
        $(this).text(type === 'password' ? 'Показать' : 'Скрыть');
    });
});

function convertFormToJson(form) {
    let form_data_array = form.serializeArray();
    let form_data = {};
    $.each(form_data_array, function (index, field) {
        if (field.name === 'iscompleted') {
            let checkboxValue = field.value === 'on' ? true : false;
            form_data[field.name] = checkboxValue;
        } else {
            form_data[field.name] = field.value;
        }
    });
    return JSON.stringify(form_data);
}

function setTokenInLocaleStorage(json) {
    localStorage.setItem("access_token", json.token);
}

function getToken() {
    return localStorage.getItem("access_token");
}

function isTokenInvalid(token) {
    if (token === "") {
        return true;
    } else {
        const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;
        return (Math.floor((new Date()).getTime() / 1000)) >= expiry;
    }
}

function getUserEmailFromToken(token) {
    return (JSON.parse(atob(token.split('.')[1]))).sub;
}

function removeTokenFromLocaleStorage() {
    localStorage.setItem("access_token", "");
}

function createTasksCard(task) {

    const li = $('<div>').addClass('list-group-item');
    const button = $('<button>').addClass('tasks tasks-button-info').attr({
        'data-bs-toggle': 'modal',
        'data-bs-target': '#update-task'
    });
    const h6 = $('<h6>').text(task.title);
    const taskDatesSpan = $('<span>').text(getStringTaskDate(task));
    const delete_btn = $('<button>').html(`<i class="bi bi-trash"></i>`).addClass('tasks-button delete-btn');
    const check_btn = $('<button>').html(`<i class="bi bi-check-circle"></i>`).addClass('tasks-button update-btn').data('iscompleted', true);
    const cancel_btn = $('<button>').html(`<i class="bi bi-bookmark-x"></i>`).addClass('tasks-button update-btn').data('iscompleted', false);
    const info_btn = $('<button>').html(`<i class="bi bi-info-circle"></i>`).addClass('tasks-button tasks-button-info')
        .attr({'data-bs-toggle': 'modal', 'data-bs-target': '#update-task'});
    const taskId = $('<input>').addClass('task-id').val(task.id).attr('type', 'hidden')
    const title = $('<input>').addClass('task-title').val(task.title).attr('type', 'hidden')
    const description = $('<input>').addClass('task-description').val(task.description).attr('type', 'hidden')
    const iscompleted = $('<input>').addClass('task-iscompleted').val(task.iscompleted).attr('type', 'hidden')
    button.append(h6);
    li.append([button, taskDatesSpan, delete_btn, taskId, title, description, iscompleted])
    const buttons = !task.iscompleted ? [check_btn, info_btn] : [cancel_btn, info_btn];
    li.append(buttons);
    return li;
}

function getBindingErrorText(xhr) {
    let errorMessage = JSON.parse(xhr.responseText);
    let errorText = "";
    errorMessage.errors.forEach(function (error) {
        errorText += `<i class="bi bi-exclamation-triangle"></i> ${error} <br>`;
    });
    return errorText;
}

function getErrorText(xhr) {
    return `<i class="bi bi-exclamation-triangle"></i> ${xhr.responseJSON.errors}`;
}

function getStringTaskDate(task) {
    const dateFormatOptions = {year: 'numeric', month: 'long', day: 'numeric'};
    taskCreatedDate = new Date(task.createdAt).toLocaleDateString(undefined, dateFormatOptions);
    if (task.iscompleted) {
        taskCompletedDate = new Date(task.completed_at).toLocaleDateString(undefined, dateFormatOptions);
        return 'Срок выполнения: ' + taskCreatedDate + ' - ' + taskCompletedDate;
    }
    return 'Дата создания: ' + taskCreatedDate;
}

function getAllUsersTasks() {
    let token = getToken();
    $.ajax({
        type: "GET",
        url: `http://${host}:8080/api/v1/tasks`,
        contentType: "application/json",
        dataType: "json",
        headers: {
            Authorization: 'Bearer ' + token
        },
        success: function (result) {
            const unfulfilledTasks = result.filter(task => !task.iscompleted);
            const completedTasks = result.filter(task => task.iscompleted);
            let containerCompletedTask = $("#completed-tasks").empty();
            let containerUnfulfilledTasks = $("#unfulfilled-tasks").empty();
            completedTasks.forEach(task => {
                containerCompletedTask.append(createTasksCard(task))
            });
            unfulfilledTasks.forEach(task => {
                containerUnfulfilledTasks.append(createTasksCard(task))
            })
        },
        error: function (xhr) {
            console.log()
            $("#error-text-index").html(getErrorText(xhr))
            $(".error-message-index").show();
        }
    })
}

function deleteTask(taskId) {
    $.ajax({
        type: "DELETE",
        url: `http://${host}:8080/api/v1/tasks/` + taskId,
        contentType: "application/json",
        dataType: "json",
        headers: {
            Authorization: 'Bearer ' + getToken()
        },
        success: function () {
            getAllUsersTasks();
        },
        error: function () {
            $("#error-text-index").html(getErrorText(xhr))
            $(".error-message-index").show();
        }
    });
}

function updateTask(taskId, title, description, iscompleted) {
    let json = {title: title, description: description, iscompleted: iscompleted};
    $.ajax({
        type: "PATCH",
        url: `http://${host}:8080/api/v1/tasks/` + taskId,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(json),
        headers: {
            Authorization: 'Bearer ' + getToken()
        },
        success: function () {
            getAllUsersTasks();
        },
        error: function (xhr) {
            $("#error-text-index").html(getErrorText(xhr))
            $(".error-message-index").show();
        }
    });
}

function showElementAndHideElements(showElement, ...hideElements) {
    $(showElement).show();
    hideElements.forEach(element => {
        $(element).hide();
    });
}