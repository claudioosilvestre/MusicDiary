const firstNameInput = document.getElementById("firstName");
const lastNameInput = document.getElementById("lastName");
const birthDateInput = document.getElementById("birthDate");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");

const registerButton = document.getElementById("register-button");
const cancelButton = document.getElementById("cancel-button");

registerButton.addEventListener("click", function() {
    const firstName = firstNameInput.value;
    const lastName = lastNameInput.value;
    const birthDate = birthDateInput.value;
    const email = emailInput.value;
    const password = passwordInput.value;

    if(firstName.trim() === "") {
        alert("First Name cannot be empty");
        return;
    }
    if(lastName.trim() === "") {
        alert("Last Name cannot be empty");
        return;
    }
    if(birthDate.trim() === "") {
        alert("Please insirt a birth date");
        return;
    }
    if(email.trim() === "") {
        alert("Email cannot be empty");
        return;
    }
    if(password.trim() === "") {
        alert("Password cannot be empty");
        return;
    }

    register(firstName, lastName, birthDate, email, password);
})

async function register(firstName, lastName, birthDate, email, password) {
    const url = "http://localhost:8080/auth/register"

    const formData = {
        firstName: firstName,
        lastName: lastName,
        birthDate: birthDate,
        email: email,
        password: password
    };

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        })
        if(!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const userSaved = await response.json();

        const successMsg = document.querySelector("#success-message");
        successMsg.style.display = "block";

        setTimeout(function() {
            window.location.href = "index.html"
        }, 2000)
        
        return userSaved;

    } catch (error) {
        const errorMsg = document.querySelector("#error-credentials");
        errorMsg.style.display = "block";

        console.error("Failed sending POST:", error)
    }

}

cancelButton.addEventListener("click", function() {
    window.location.href = "index.html";
})