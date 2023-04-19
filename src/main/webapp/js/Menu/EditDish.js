let createDishTr;
let dishNameEl;
let dishDescriptionEl;
let dishPriceEl;

function checkInput() {
    dishNameInp = document.querySelector('#dishNameInp');
    descriptionInp = document.querySelector('#descriptionInp');
    priceInp = document.querySelector('#editedPrice');
    photoLinkInp = document.querySelector('#photoLinkInp');

    return new Promise((resolve, reject) => {
        if (dishNameInp.value.trim().length == 0) {
            reject("Enter the NAME of the dish!")
        } else if (descriptionInp.value.trim().length == 0) {
            reject("Enter the DESCRIPTION of the dish!")
        } else if (priceInp.value.trim().length == 0) {
            reject("Enter the PRICE of the dish!")
        } else if (photoLinkInp.value.trim().length == 0) {
            reject("Choose the PHOTO of the dish!")
        }
        resolve()
    })
}

function editDish(dishID, categoryID, saveFmt) {
    // close previos one if it was open
    Array.prototype.slice.call(document.querySelectorAll("tr.CreateDishRow")).forEach(el => {
        el.nextElementSibling.hidden = false;
        el.remove()
    })
    // close previous error msg if it is
    Array.prototype.slice.call(document.querySelectorAll("#errorMsg")).forEach(el => {
        el.remove()
    })

    // hide row with edit content btn
    editedDishTr = document.querySelector(`#dish${dishID}row`)
    editedDishTr.hidden = true

    // show edit dish row
    let dishNameInp = document.createElement('input')
    dishNameInp.id = "dishNameInp"
    dishNameInp.type = "text"
    dishNameInp.name = "dishName"
    dishNameInp.placeholder = "Dish name"
    dishNameEl = document.querySelector(`#dishName${dishID}_${categoryID}`)
    dishNameInp.value = dishNameEl.innerHTML
    dishNameInp.required = true

    let li = document.createElement('li')
    li.appendChild(dishNameInp)

    let dishNameH = document.createElement('h3')
    dishNameH.className = "DishName"
    dishNameH.appendChild(li)

    let td1 = document.createElement('td')
    td1.appendChild(dishNameH)

    let descriptionTextArea = document.createElement('textarea')
    descriptionTextArea.id = "descriptionInp"
    descriptionTextArea.cols = 60
    descriptionTextArea.rows = 3
    descriptionTextArea.placeholder = "Description..."
    dishDescriptionEl = document.querySelector(`#description${dishID}_${categoryID}`)
    descriptionTextArea.value = dishDescriptionEl.innerHTML
    descriptionTextArea.required = true
    td1.appendChild(descriptionTextArea)

    let saveDishBtn = document.createElement('input')
    saveDishBtn.type = "button"
    saveDishBtn.value = saveFmt
    saveDishBtn.addEventListener("click", () =>
        checkInput()
            .then(() => { saveDishChangesAfterEdit(dishID, categoryID) })
            .catch((message) => { alert(message) }))
    td1.appendChild(saveDishBtn)

    let td2 = document.createElement('td')

    let priceInp = document.createElement('input')
    priceInp.id = "editedPrice"
    priceInp.type = "text"
    priceInp.name = "price"
    priceInp.placeholder = "Price"
    dishPriceEl = document.querySelector(`#price${dishID}_${categoryID}`)
    priceInp.value = dishPriceEl.innerHTML
    priceInp.onkeypress = validatePrice
    td2.appendChild(priceInp)

    let td3 = document.createElement('td')
    td3.colSpan = 3

    let chooseImgBtn = document.createElement('input')
    chooseImgBtn.id = "photoLinkInp"
    chooseImgBtn.type = "file"
    chooseImgBtn.name = "photoLink"
    chooseImgBtn.accept = "images/*"
    chooseImgBtn.required = "true"
    td3.appendChild(chooseImgBtn)

    createDishTr = document.createElement('tr')
    createDishTr.className = "CreateDishRow"
    createDishTr.append(td1, td2, td3)

    let table = editedDishTr.parentElement
    table.insertBefore(createDishTr, editedDishTr)
}

function saveDishChangesAfterEdit(dishID, categoryID) {
    let url = 'http://localhost:8888/ajaxController'
    let body = `command=edit_dish&editedDishId=${dishID}&dishName=${dishNameInp.value}&description=${descriptionInp.value}&price=${priceInp.value}&photoLink=${photoLinkInp.files[0].name}`

    const promise = sendRequest(url, 'POST', body)
    promise.then((response) => onSavedChangesAfterEdit(response, categoryID))
        .catch((response) => {
            var error = response.statusText
            window.location = `http://localhost:8888/errorPage?errorMsg=${error}`
        })
}

function onSavedChangesAfterEdit(dish, categoryID) {
    let errorMsgTr = document.querySelector('#errorMsg');
    let dishesTable = document.querySelector(".CreateDishRow").parentElement

    if (dish.validationError) {
        if (editedDishTr.nextElementSibling == errorMsgTr) {
            dishesTable.removeChild(errorMsgTr);
        }
        let errorMsgH = document.createElement('h4')
        errorMsgH.id = 'errorMsgH'
        errorMsgH.innerHTML = dish.message;

        errorMsgTr = document.createElement('tr')
        errorMsgTr.id = "errorMsg";

        let errorMsgTd = document.createElement('td')
        errorMsgTd.colSpan = 5

        errorMsgTd.appendChild(errorMsgH)
        errorMsgTr.appendChild(errorMsgTd)
        dishesTable.insertBefore(errorMsgTr, editedDishTr.nextElementSibling)
    } else {
        if (editedDishTr.nextElementSibling == errorMsgTr) {
            dishesTable.removeChild(errorMsgTr);
        }

        dishNameEl.innerHTML = dish.name
        dishDescriptionEl.innerHTML = dish.description
        dishPriceEl.innerHTML = dish.price.toFixed(1)
        let dishPhotoLink = document.querySelector(`#photoLink${dish.id}_${categoryID}`)
        dishPhotoLink.src = dish.photoLink

        editedDishTr.hidden = false;
        createDishTr.remove();
    }
}

function validatePrice(evt) {
    var theEvent = evt || window.event;
    var key = theEvent.keyCode || theEvent.which;
    key = String.fromCharCode(key);
    var keyRegex = /[0-9]|\./

    if (!keyRegex.test(key)) {
        theEvent.returnValue = false;
        if (theEvent.preventDefault) theEvent.preventDefault();
    } else {
        let price = evt.target.value
        let priceAfterEnterKey = price + key
        let priceRegex = /^[\d]+\.?[\d]*$/
        if (!priceRegex.test(priceAfterEnterKey)) {
            theEvent.returnValue = false;
            if (theEvent.preventDefault) theEvent.preventDefault();
        }
    }
}