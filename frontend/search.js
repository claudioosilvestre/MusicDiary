const searchInput = document.getElementById("searchInput");

const searchBtn = document.getElementById("searchButton");

const dropdown = document.getElementById("searchType");
const result = document.getElementById("result");

    dropdown.addEventListener("change", function() {
        const selectedValue = dropdown.value;
    })


searchBtn.addEventListener("click", function() {
    const searchValue = searchInput.value;

    if(searchValue.trim() === "") {
        alert("Cannot be empty");
    }

    if(dropdown.value === "artists") {
        searchArtists(searchValue);
    }

    if(dropdown.value === "tracks") {
        searchTracks(searchValue);
    }
    

})



async function searchArtists(artist) {
    const url = `http://localhost:8080/search/artists?name=${artist}`;

    try {
        const response = await fetch(url);

        if(!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }
        
        const artistSaved = await response.json();
        

        document.getElementById("results").innerHTML = "";
        
        artistSaved.forEach(item => {
            const artistCard = document.createElement("div");
            artistCard.className = "col-md-3 mb-4";
            artistCard.innerHTML = `
            <div class="card h-100">
            <img src="${item.imageURL}" class="card-img-top" onerror="this.src='https://placehold.co/300x300/1a1a2e/6c63ff?text=🎵'">
                <div class="card-body">
                    <h5 class="card-title">${item.name}</h5>
                    <p class="card-text text-muted">${item.totalListeners.toLocaleString()} listeners</p>
                </div>
            </div>
            `;
            const addFavBtn = document.createElement("button");
            addFavBtn.textContent = "Add to Favorites";
            addFavBtn.className = "btn btn-primary mt-2";
            addFavBtn.addEventListener("click", async function() {
                const token = localStorage.getItem("token");

                const response = await fetch("http://localhost:8080/saved-songs", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + token
                    },
                    body: JSON.stringify({
                        title: item.name,
                        artistName: item.name,
                        imageUrl: item.imageURL,
                        lastFmUrl: item.profileURL
                    })
            });
            if(response.ok) {
                alert("Saved!");
            }
        });

            const cardBody = artistCard.querySelector(".card-body");
            cardBody.appendChild(addFavBtn);
            
        document.getElementById("results").appendChild(artistCard);
        });

        return artistSaved;
    } catch (error) {
        console.error("Failed sending POST:", error)
    }
}


async function searchTracks(track) {
    const url = `http://localhost:8080/search/tracks?name=${track}`;
    
    try{

        const response = await fetch(url);

        if(!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const tracksSaved = await response.json();

        document.getElementById("results").innerHTML = "";

        tracksSaved.forEach(item => {
            const tracksCard = document.createElement("div");

            tracksCard.className = "col-md-3 mb-4";
            tracksCard.innerHTML = `
            <div class="card h-100">
            <img src="${item.imageURL}" class="card-img-top" onerror="this.src='https://placehold.co/300x300/1a1a2e/6c63ff?text=🎵'">
                <div class="card-body">
                    <h5 class="card-title">${item.musicName}</h5>
                    <h5 class="artist-title">${item.artistName}</h5>
                    <p class="card-text text-muted">${item.totalListeners.toLocaleString()} listeners</p>
                </div>
            </div>
            `;

            const addFavTrackBtn = document.createElement("button");
            addFavTrackBtn.textContent = "Add to Favorites";
            addFavTrackBtn.className = "btn btn-primary mt-2";;

            addFavTrackBtn.addEventListener("click", async function() {
                const token = localStorage.getItem("token");

                const response = await fetch("http://localhost:8080/saved-songs", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": "Bearer " + token
                    },
                    body: JSON.stringify({
                        title: item.musicName,
                        artistName: item.artistName,
                        imageURL: item.imageURL,
                        lastFmUrl: ""
                    })
                });
                if(response.ok) {
                    alert("Saved!");
                }
            });

            const cardBody = tracksCard.querySelector(".card-body");
            cardBody.appendChild(addFavTrackBtn);

            document.getElementById("results").appendChild(tracksCard);
        })

        return tracksSaved;

    } catch(error) {
        console.error("Failed sendind POST:", error)
    }
}

document.getElementById("logoutBtn").addEventListener("click", function() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
});