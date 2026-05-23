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
        
        const listElements = document.createElement("ul");

        document.getElementById("results").innerHTML = "";
        
        artistSaved.forEach(item => {
            const listItems = document.createElement("li");
            listItems.textContent = item.name;

            listElements.appendChild(listItems);
        });

        document.getElementById("results").appendChild(listElements);

        return artistSaved;
    } catch (error) {
        console.error("Failed sending POST:", error)
    }
}


async function searchTracks(track) {
    const url = "http://localhost:8080/search/tracks"


}