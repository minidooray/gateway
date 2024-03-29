function toggleProjectList() {
    var projectList = document.getElementById("projectList");
    projectList.classList.toggle("hidden");
}

function redirectToTask(projectId, taskId) {
    var url = '/projects/' + projectId + '/task/' + taskId;
    window.location.href = url;
}

function toggleMenu() {
    var menu = document.getElementById("menu");
    menu.classList.toggle("active");
}

function projectMenu() {
    var menu = document.getElementById("projectmenu");
    menu.classList.toggle("active");
}

var username = document.getElementById("username");
username.addEventListener("click", toggleMenu);

var thisproject = document.getElementById("projectName");
thisproject.addEventListener("click",projectMenu)