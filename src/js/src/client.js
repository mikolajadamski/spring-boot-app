import fetch from 'unfetch';

const checkStatus = response => {
    if(response.ok) {
        return response;
    }
    else {
        const error = new Error(response.statusText);
        error.response = response;
        return Promise.reject(error);
    }
}

export const getAllStudents = () => 
fetch('api/students').then(checkStatus);
export const addNewStudent = student => 
    fetch('api/students', {
        headers: {
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(student)
    })
    .then(checkStatus);