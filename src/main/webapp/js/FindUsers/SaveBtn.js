function saveChanges(change, login) {
    if (change.value == 0) {
        alert("Choose a ROLE!");
    } else {
        let url = 'http://localhost:8888/ajaxController';
        let body;
        if (change.name == 'phoneNumber') {
            let phone = change.value.split("+")[1];
            body = `command=edit_user&login=${login}&${change.name}=%2B${phone}`;
        } else {
            body = `command=edit_user&login=${login}&${change.name}=${change.value}`;
        }

        sendRequest(url, 'POST', body)
            .then((response) => { onUserSaved(response, change) })
            .catch((response) => {
                var error = response.statusText
                window.location = `http://localhost:8888/errorPage?errorMsg=${error}`
            })
    }
}

function onUserSaved(response, change) {
    if (response.validationError) {
        alert(response.message);
    } else {
        if (change.name == "role") {
            editedDiv.firstElementChild.innerHTML = change.children[Number(change.value)].innerHTML;
        } else {
            editedDiv.firstElementChild.innerHTML = change.value;
        }
        editedDiv.lastElementChild.remove();
        editedDiv.firstElementChild.hidden = false;

        let saveBtn = document.querySelector('#saveBtn');
        saveBtn.parentElement.firstElementChild.hidden = false;
        saveBtn.remove();
    }
}