document.querySelectorAll('.editImg').forEach(el => { el.addEventListener("click", editUserInfo) });
let editedDiv;

function editUserInfo(event) {
    document.querySelectorAll('.EditUserInput').forEach(el => {
        document.querySelector('#saveBtn').previousElementSibling.hidden = false;
        document.querySelector('#saveBtn').remove();

        el.previousElementSibling.hidden = false;
        el.remove();
    })

    let editBtn = event.target;
    editBtn.hidden = true;

    let saveBtn = document.createElement('img');
    saveBtn.id = "saveBtn";
    saveBtn.className = "editImg";
    saveBtn.src = "../../images/save.png";
    saveBtn.alt = "save";
    saveBtn.addEventListener("click", () => { saveChanges(input, gUser.login) })
    editBtn.parentElement.appendChild(saveBtn);

    editedDiv = editBtn.parentElement.previousElementSibling
    editedDiv.firstElementChild.hidden = true;

    let input;
    if (editedDiv.id == "roleDiv") {
        input = document.createElement('select');
        input.name = "role";
        input.className = "EditUserInput";
        input.style.fontSize = "25px"

        let firstOption = document.createElement('option');
        firstOption.innerHTML = "Choose a role";
        firstOption.value = 0;
        input.appendChild(firstOption);

        let i = 0;
        let roles = ["admin", "user", "cook"];
        roles.forEach(role => {
            let option = document.createElement('option')
            option.value = i + 1; // in DB role starts from 1
            option.innerHTML = roles[i];
            input.appendChild(option);
            i++;
        })

        editedDiv.appendChild(input);
    } else {
        input = document.createElement('input');
        input.type = "text";
        input.className = "EditUserInput";
        input.value = editedDiv.firstElementChild.innerHTML;
        input.style.fontSize = "25px";
        input.style.width = '335px';
        editedDiv.appendChild(input);
        input.name = input.previousElementSibling.id;
    }


}