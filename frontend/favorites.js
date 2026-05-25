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
            <img src="${item.imageUrl}" class="card-img-top" onerror="this.src='https://via.placeholder.com/150'">
                <div class="card-body">
                    <h5 class="card-title">${item.title}</h5>
                    <h4 class="card-artist">${item.artistName}</h4>
                    <p class="card-text text-muted">Add to favorites: ${item.createdAt}</p>
                </div>
            </div>
            `;

            document.getElementById("favorites").appendChild(artistCard);
        })
    } catch(error) {
        console.error("Error loading favorites:", error);
    }  
}