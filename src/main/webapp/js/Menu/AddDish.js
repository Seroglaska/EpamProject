let addDishTr;
let dishNameInp;
let descriptionInp;
let priceInp;
let photoLinkInp;

function showCreateDishFrom(categoryID, saveFmt, addFmt) {
    // close previos one if it was open
    Array.prototype.slice.call(document.querySelectorAll("tr.CreateDishRow")).forEach(el => {
        el.nextElementSibling.hidden = false;
        el.remove()
    })
    // close previous error msg if it is
    Array.prototype.slice.call(document.querySelectorAll("#errorMsg")).forEach(el => {
        el.remove()
    })

    // hide row with add content btn
    addDishTr = document.querySelector(`#addNewDishTr${categoryID}`)
    addDishTr.hidden = true

    // show create dish row
    let createDishTr = document.createElement('tr')
    createDishTr.className = "CreateDishRow"

    let td1 = document.createElement('td')

    let dishNameH = document.createElement('h3')
    dishNameH.className = "DishName"

    let li = document.createElement('li')
    let dishNameInp = document.createElement('input')
    dishNameInp.id = "dishNameInp"
    dishNameInp.type = "text"
    dishNameInp.name = "dishName"
    dishNameInp.placeholder = "Dish name"
    dishNameInp.required = true
    li.appendChild(dishNameInp)
    dishNameH.appendChild(li)
    td1.appendChild(dishNameH)

    let descriptionTextArea = document.createElement('textarea')
    descriptionTextArea.id = "descriptionInp"
    descriptionTextArea.cols = 60
    descriptionTextArea.rows = 3
    descriptionTextArea.placeholder = "Description..."
    descriptionTextArea.required = true
    td1.appendChild(descriptionTextArea)

    let saveDishBtn = document.createElement('input')
    saveDishBtn.type = "button"
    saveDishBtn.value = saveFmt
    saveDishBtn.addEventListener("click", () =>
        checkInput()
            .then(() => { saveDishChanges(categoryID, saveFmt, addFmt) })
            .catch((message) => { alert(message) }))
    td1.appendChild(saveDishBtn)

    let td2 = document.createElement('td')

    let priceInp = document.createElement('input')
    priceInp.id = "editedPrice"
    priceInp.type = "text"
    priceInp.name = "price"
    priceInp.placeholder = "Price"
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

    createDishTr.append(td1, td2, td3)

    let table = addDishTr.parentElement
    table.insertBefore(createDishTr, addDishTr)
}

function saveDishChanges(categoryID, saveFmt, addFmt) {
    let url = 'http://localhost:8888/ajaxController'
    let body = `command=add_dish&categoryForAdd=${categoryID}&dishName=${dishNameInp.value}&description=${descriptionInp.value}&price=${priceInp.value}&photoLink=${photoLinkInp.files[0].name}`

    const promise = sendRequest(url, 'POST', body)
    promise.then((response) => onSavedChanges(response, saveFmt, addFmt))
        .catch((response) => {
            var error = response.statusText
            window.location = `http://localhost:8888/errorPage?errorMsg=${error}`
        })
}

function onSavedChanges(dish, saveFmt, addFmt) {
    let errorMsgTr = document.querySelector('#errorMsg');
    let dishesTable = document.querySelector(".CreateDishRow").parentElement

    if (dish.validationError) {
        if (dishesTable.lastElementChild == errorMsgTr) {
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
        dishesTable.appendChild(errorMsgTr)
    } else {
        if (dishesTable.lastElementChild == errorMsgTr) {
            dishesTable.removeChild(errorMsgTr);
        }

        let td1 = document.createElement('td')
        td1.className = "col1"
        let td2 = document.createElement('td')
        td2.className = "col2"
        let td3 = document.createElement('td')
        td3.className = "col3"
        let td4 = document.createElement('td')
        td4.className = "col4"
        let td5 = document.createElement('td')
        td5.className = "col5"

        td1.innerHTML = `<li>
                            <strong id="dishName${dish.id}_${dish.categoryId}"
                                style="font-size: 15px;">${dish.name}</strong>
                            (<span id="description${dish.id}_${dish.categoryId}">${dish.description}</span>)
                        </li>`
        td2.innerHTML = `<strong id="price${dish.id}_${dish.categoryId}">${dish.price.toFixed(1)}</strong>`
        td3.innerHTML = `<img id="photoLink${dish.id}_${dish.categoryId}" src="${dish.photoLink}" alt="photo of ${dish.name}" class="dishPhoto">`
        td4.innerHTML = `<button onclick="reduceOne(event, ${dish.categoryId}, ${dish.id})">-</button> 
                         <input id="quantityOf${dish.id}_${dish.categoryId}" type="text" name="quantity" value="1" onkeypress='validate(event)' required>
                         <button onclick="addOne(event, ${dish.categoryId}, ${dish.id})">+</button> <br> 
                         <input type="submit" value="${addFmt}" class="addToOrderBtn" id="addToOrder${dish.id}_${dish.categoryId}" onclick="addToOrder(this, ${dish.categoryId}, ${dish.id})">`
        td5.innerHTML = `<input type="image" src="../../images/remove.png" alt="remove" class="imgInTd" onclick="removeDishFromMenu(${dish.id}, event)">
                         <input type="image" src="../../images/edit.png" alt="edit" class="imgInTd" onclick="editDish(${dish.id}, ${dish.categoryId}, \`${saveFmt}\`)">`

        let dishTr = document.createElement('tr')
        dishTr.id = `dish${dish.id}row`
        dishTr.append(td1, td2, td3, td4, td5)

        let table = addDishTr.parentElement
        table.insertBefore(dishTr, addDishTr)
        addDishTr.hidden = false;
        document.querySelector('tr.CreateDishRow').remove();
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