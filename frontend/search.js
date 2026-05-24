const searchInput = document.getElementById("searchInput");

const searchBtn = document.getElementById("searchButton");



const dropdown = document.getElementById("searchType");
const result = document.getElementById("result");

    dropdown.addEventListener("change", function() {
        const selectedValue = dropdown.value;
        
        result.textContent = selectedValue;
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
            <img src="${item.imageURL}" class="card-img-top" onerror="this.src='https://via.placeholder.com/150'">
                <div class="card-body">
                    <h5 class="card-title">${item.name}</h5>
                    <p class="card-text text-muted">${item.totalListeners.toLocaleString()} listeners</p>
                </div>
            </div>
            `;
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
            <img src="${item.imageURL}" class="card-img-top" onerror="this.src='https://via.placeholder.com/150'">
                <div class="card-body">
                    <h5 class="card-title">${item.musicName}</h5>
                    <h5 class="artist-title">${item.artistName}</h5>
                    <p class="card-text text-muted">${item.totalListeners.toLocaleString()} listeners</p>
                </div>
            </div>
            `;

            document.getElementById("results").appendChild(tracksCard);
        })

        return tracksSaved;

    } catch(error) {
        console.error("Failed sendind POST:", error)
    }
}