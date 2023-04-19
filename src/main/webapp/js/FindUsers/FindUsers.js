document.querySelector('#inputUserLogin').addEventListener('input', findUser);
document.querySelector('#findImg').addEventListener("click", document.querySelector('#inputUserLogin'));

let gridContainer = document.querySelector('.grid-container');
let foundUsers = document.querySelector('#foundUsers');

function findUser() {
    gridContainer.style.visibility = 'hidden';

    if (this.value == "") {
        foundUsers.hidden = true;
    } else {
        let url = 'http://localhost:8888/ajaxController';
        let body = `command=find_user&login=${this.value}`;

        sendRequest(url, 'POST', body)
            .then((response) => { onUsersReceived(response) })
            .catch((response) => {
                var error = response.statusText
                window.location = `http://localhost:8888/errorPage?errorMsg=${error}`
            })
        foundUsers.hidden = false;
    }
}

function onUsersReceived(users) {
    document.querySelectorAll('.UserDiv').forEach(userDiv => {
        userDiv.remove();
    })

    if (users.validationError) {
        alert(response.message);
    } else {
        users.forEach(user => {
            let userDiv = document.createElement('div');
            userDiv.id = user.login;
            userDiv.className = 'UserDiv';
            userDiv.innerHTML = user.login + "<hr>";
            userDiv.onclick = () => { showUserInfo(user) };

            foundUsers.appendChild(userDiv);
        });
    }
}

let nameDiv = document.querySelector('#nameDiv');
let phoneNumberDiv = document.querySelector('#phoneNumberDiv');
let emailDiv = document.querySelector('#emailDiv');
let gUser;

function showUserInfo(user) {
    gUser = user;

    document.querySelectorAll('.UserDiv').forEach(userDiv => {
        userDiv.remove();
    })

    nameDiv.children.name.innerHTML = user.name;
    phoneNumberDiv.children.phoneNumber.innerHTML = user.phoneNumber;
    emailDiv.children.email.innerHTML = user.email;

    let role;
    let roleDiv = document.querySelector('#roleDiv');
    roleDiv.children.role.id = 'role';
    switch (user.roleId) {
        case 1: {
            role = "admin";
            roleDiv.children.role.innerHTML = role;
            break;
        }
        case 2: {
            role = "user";
            roleDiv.children.role.innerHTML = role;
            break;
        }
        case 3: {
            role = "cook";
            roleDiv.children.role.innerHTML = role;
            break;
        }
        default: {
            role = "ask admin";
            roleDiv.children.role.innerHTML = role;
            break;
        }
    }

    gridContainer.style.visibility = 'visible';
}