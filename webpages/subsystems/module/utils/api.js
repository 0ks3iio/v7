import "whatwg-fetch"

const host = "/api"

let Request = (url, body, method = 'get') => {
    return new Promise((resolve, reject) => {
        let isSuccess = true;
        let path = host + url
        if(method === 'get' && body) {
            path += '?'
            for (let i in body) {
                path += `${i}=${body[i]}&`
            }
        }
        fetch(path, {
            method,
            headers: {
            },
            body: (body && (method === 'post')) ? JSON.stringify(body) : body
        }).then((response) => {
            isSuccess = response.ok;
            return response.json();
        }).then((response) => {
            console.log('返回',response)                 
            if (isSuccess) {
                resolve(response);
            } else {
                reject(response);
            }
        }).catch((error) => {
            reject(error);
        });
    })
}



const getTimeUrl = '/eclasscard/standard/get/system/nowtime'

export let getTime = (body) => {
    return  Request(getTimeUrl,body);
}
