listFavorites();

document.getElementById("logoutBtn").addEventListener("click", function() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
});

async function listFavorites() {
    const url = "http://localhost:8080/saved-songs"
    
    try{
        const token = localStorage.getItem("token");

        const response = await fetch(url, {
            headers: {
                "Authorization": "Bearer " + token
            }
        });

        if(!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const favoritesSaved = await response.json();
        console.log(favoritesSaved);

        document.getElementById("favorites").innerHTML="";

        favoritesSaved.forEach(item => {
            const artistCard = document.createElement("div");
            artistCard.className = "col-md-3 mb-4";
            artistCard.innerHTML = `
            <div class="card h-100">
            <img src="${item.imageUrl}" class="card-img-top" onerror="this.src='https://placehold.co/300x300/1a1a2e/6c63ff?text=🎵'">
                <div class="card-body">
                    <h5 class="card-title">${item.title}</h5>
                    <h4 class="card-artist">${item.artistName}</h4>
                </div>
            </div>
            `;
            const deleteFavBtn = document.createElement("button");
            deleteFavBtn.textContent="Delete";
            deleteFavBtn.className="btn btn-danger mt-2";
            deleteFavBtn.addEventListener("click", async function() {
                const token = localStorage.getItem("token");

                const response = await fetch(`http://localhost:8080/saved-songs/${item.id}`, {
                    method: "DELETE",
                    headers: {
                        "Authorization": "Bearer " + token 
                    }
            });
            if(response.ok) {
                listFavorites();
            }
        });

            artistCard.querySelector(".card-body").appendChild(deleteFavBtn);
            document.getElementById("favorites").appendChild(artistCard);
        });

    } catch(error) {
        console.error("Error loading favorites:", error);
    }
}
