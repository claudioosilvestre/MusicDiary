const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");

const loginBtn = document.getElementById("login");


loginBtn.addEventListener("click", function() {
    const userName = usernameInput.value;
    const passWord = passwordInput.value;

    if(userName.trim() === "") {
        alert("Username cannot be empty");
    }
    if(passWord.trim() === "") {
        alert("Password cannot be empty");
    }

    userLogin(userName, passWord);

})

async function userLogin(userName, passWord) {
    const url = "http://localhost:8080/auth/login"

    const formData = {
        email: userName,
        password: passWord
};

    try {
        const response = await fetch(url, {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        if(!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }
        
        const userSaved = await response.json();

        localStorage.setItem("token", userSaved.token);
        window.location.href = "search.html"
        
        return userSaved;
    } catch (error) {
        const errorMsg = document.querySelector("#error-credentials");
        errorMsg.style.display = "block";

        console.error("Failed sending POST:", error)
    }
}