function showCategoryEditFormForAdd() {
    document.querySelector('#create-category-txtbtn').hidden = true;

    const textInp = document.createElement('input');
    textInp.type = "text";
    textInp.id = "categoryNameInput";

    const submitInp = document.createElement('input');
    submitInp.type = "image";
    submitInp.src = "../../images/save.png"
    submitInp.id = "imgInEditCategory";
    submitInp.addEventListener("click", (event) =>
        checkInputCategory(event)
            .then(addNewCategory)
            .catch((msg) => {
                alert(msg);
            }));

    const editCategoryForm = document.createElement('form');
    editCategoryForm.id = "editCategoryForm";
    editCategoryForm.className = "CategoryName";

    const main = document.querySelector('#main-block');
    main.appendChild(editCategoryForm);

    document.querySelector('#editCategoryForm').appendChild(textInp);
    document.querySelector('#editCategoryForm').appendChild(submitInp);
}

function addNewCategory() {
    const textInp = document.querySelector('#categoryNameInput');

    const url = "http://localhost:8888/ajaxController"
    const body = `command=add_category&categoryName=${textInp.value}`

    const promise = sendRequest(url, 'POST', body)
    promise.then(onCategoryCreated)
        .catch((response) => {
            var error = response.statusText
            window.location = `http://localhost:8888/errorPage?errorMsg=${error}`
        })
}

var errorMsg;

function onCategoryCreated(response) {
    if (response.validationError) {
        if (document.querySelector('#editCategoryForm').lastChild == errorMsg) {
            document.querySelector('#editCategoryForm').removeChild(errorMsg);
        }
        errorMsg = document.createElement('h3');
        errorMsg.id = "errorMsgH";
        errorMsg.innerHTML = response.message;

        document.querySelector('#editCategoryForm').appendChild(errorMsg);
    } else {
        if (document.querySelector('#editCategoryForm').lastChild == errorMsg) {
            document.querySelector('#editCategoryForm').removeChild(errorMsg);
        }

        var categoryId = response.id;
        var categoryName = response.name;

        const createdCategory = document.createElement('h2');
        createdCategory.id = `category${categoryId}`
        createdCategory.className = 'CategoryName';
        createdCategory.innerHTML =
            `<span id="categoryName${categoryId}">${categoryName}</span>
                    <input type="image" src="../../images/edit.png" alt="edit" class="imgInTd"
                        onclick="showEditCategory(${categoryId}, event)">
                    <input type="image" src="../../images/remove.png" alt="remove" class="imgInTd"
                        onclick="removeCategory(${categoryId}, event)">`;

        const main = document.querySelector('#main-block');
        const createCategoryForm = document.querySelector('#create-category-txtbtn');
        main.insertBefore(createdCategory, createCategoryForm);

        const table = document.createElement('table');
        table.innerHTML =
            `<tr>
                <td colspan="5">
                    <a href="/home?createDish=true&categoryForAdd=${categoryId}">
                        <img src="../../images/addContent.png" alt="add dish" id="imgAddContent">
                    </a>
                </td>
            </tr>`;
        main.insertBefore(table, createCategoryForm);

        createCategoryForm.hidden = false;
        document.querySelector('#editCategoryForm').remove();
    }
}

function checkInputCategory(event) {
    event.preventDefault()
    categoryInp = document.querySelector('#categoryNameInput');

    return new Promise((resolve, reject) => {
        if (categoryInp.value.trim().length == 0) {
            reject("you should enter the NAME of the category!");
        }
        resolve();
    })
}