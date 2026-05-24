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
            
            const artistImage = document.createElement("img");
            artistImage.src = item.imageURL;

            const artistName = document.createElement("h3");
            artistName.textContent = item.name;

            const listenners = document.createElement("p");
            listenners.textContent = item.totalListeners;
            
            artistCard.appendChild(artistImage);
            artistCard.appendChild(artistName);
            artistCard.appendChild(listenners);

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

            const trackImage = document.createElement("img");
            trackImage.src = item.imageURL;

            const trackName = document.createElement("h3");
            trackName.textContent = item.musicName;

            const listenners = document.createElement("p");
            listenners.textContent = item.totalListeners;

            tracksCard.appendChild(trackImage);
            tracksCard.appendChild(trackName);
            tracksCard.appendChild(listenners);

            document.getElementById("results").appendChild(tracksCard);
        })

        return tracksSaved;

    } catch(error) {
        console.error("Failed sendind POST:", error)
    }
}