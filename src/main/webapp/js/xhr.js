function sendRequest(url, method, body = null) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest()
        xhr.open(method, url)

        xhr.responseType = 'json'
        xhr.onload = () => {
            if (xhr.status >= 400) {
                reject(xhr)
            } else {
                resolve(xhr.response)
            }

        }

        xhr.onerror = () => {
            reject(xhr)
        }

        xhr.setRequestHeader("Content-Type", 'application/x-www-form-urlencoded; charset=UTF-8')
        xhr.send(body)
    })
}