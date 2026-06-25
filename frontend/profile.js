const firstNameInput = document.getElementById("firstName");
const lastNameInput = document.getElementById("lastName");
const birthDateInput = document.getElementById("birthDate");
const emailInput = document.getElementById("email");

const editButtonInput = document.getElementById("edit-button");
const deleteAccountBtnInput = document.getElementById("deleteAccountbutton");

async function loadProfile () {
    const url = "http://localhost:8080/user"

    try {
        const response = await fetch(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        if(!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const userSaved = await response.json();

        firstNameInput.value = userSaved.firstName;
        lastNameInput.value = userSaved.lastName;
        birthDateInput.value = userSaved.birthDate;
        emailInput.value = userSaved.email;

        return userSaved;
    } catch (error) {
        const errorMsg = document.querySelector("#error-credentials");
        errorMsg.style.display = "block";

        console.error("Failed sending GET:", error)
    }
}

editButtonInput.addEventListener("click", function() {
    const firstName = firstNameInput.value;
    const lastName = lastNameInput.value;
    const birthDate = birthDateInput.value;
    const email = emailInput.value;

    if(firstName.trim() === "") {
        alert("First name cannot be empty");
        return;
    }
    if(lastName.trim() === "") {
        alert("Last name cannot be empty");
        return;
    }
    if(birthDate.trim() === "") {
        alert("Please insert a Birthdate");
        return;
    }
    if(email.trim() === "") {
        alert("Email cannot be empty");
        return;
    }

    editProfile();
})

async function editProfile () {
    const url = "http://localhost:8080/user"

    const userData = {
        firstName: firstNameInput.value,
        lastName: lastNameInput.value,
        birthDate: birthDateInput.value,
        email: emailInput.value
    };

    try {
        const token = localStorage.getItem("token");

        const reponse = await fetch(url, {
            method: "PATCH",
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + token
            },
            body: JSON.stringify(userData)
        });
        if(!reponse.ok) {
            throw new Error(`Server error: ${response.status}`)
        }

        const editUserSaved = await reponse.json();

        firstNameInput.value = editUserSaved.firstName;
        lastNameInput.value = editUserSaved.lastName;
        birthDateInput.value = editUserSaved.birthDate;
        emailInput.value = editUserSaved.email;

        return editUserSaved;
    } catch (error) {
        const errorMsg = document.querySelector("#error-credentials");
        errorMsg.style.display = "block";

        console.error("Failed sending PATCH:", error)
    }
}

deleteAccountBtnInput.addEventListener("click", async function() {

    const url = "http://localhost:8080/user"
    
    if (confirm("Are you sure you want to delete your account?")) {
        try {
            const token = localStorage.getItem("token");
            
            const response = await fetch(url, {
                method: "DELETE",
                headers: {
                    "Authorization": "Bearer " + token
                }
            });
            if(!response.ok) {
                throw new Error(`Server error: ${response.status}`)
            }

            window.location.href = "index.html";

        } catch (error) {
            const errorMsg = document.querySelector("#error-credentials");
            errorMsg.style.display = "block";

            console.error("Failed sending DELETE:", error)
        }
    }
})

loadProfile();