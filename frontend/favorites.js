let selectedSavedSongId = null;
let selectedFilter = "all";

listFavorites();

const filterDropdown = document.getElementById("filter-category");
const filterValue = document.getElementById("filterValue");
const filterValueFrom = document.getElementById("filterValueFrom");
const filterValueTo = document.getElementById("filterValueTo");

filterValue.placeholder = "";
filterValue.style.display = "none";
filterValue.disabled = true;
filterValueFrom.style.display = "none";
filterValueFrom.disabled = true;
filterValueTo.style.display = "none";
filterValueTo.disabled = true;

const searchFilterBtn = document.getElementById("search-Filter-Btn");

document.getElementById("logoutBtn").addEventListener("click", function() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
});

filterDropdown.addEventListener("change", function() {
            const categorySelected = this.value;
            selectedFilter = categorySelected; 

            if(selectedFilter === "title") {
                showTextInput("Enter song title");
            } else if(selectedFilter === "artistName") {
                showTextInput("Enter artist name");
            } else if (selectedFilter === "dateRange") {
                showDateInputs();
            } else {
                hideAllInputs();
            }

            clearFilters();
        });

    
searchFilterBtn.addEventListener("click", function() {

    if (selectedFilter === "dateRange") {
        if (!filterValueFrom.value || !filterValueTo.value) {
            alert("Please select both dates.");
            return;
        }
        if (filterValueFrom.value > filterValueTo.value) {
        alert("'From' date cannot be after the 'To' date.");
        return;
        }
    } else if (selectedFilter !== "all") {
        if (filterValue.value.trim() === "") {
            alert("Please enter a value to search.");
            return;
        }
    }
    listFavorites();
});

async function listFavorites() {
    let url = "http://localhost:8080/saved-songs/filter"

    if(selectedFilter !== "all" && selectedFilter != "dateRange") {
        url += `?${selectedFilter}=${encodeURIComponent(filterValue.value)}`;
    } else if(selectedFilter === "dateRange") {
        const from = filterValueFrom.value;
        const to = filterValueTo.value;
        url += `?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`;
    }

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
            document.getElementById("favorites").appendChild(createFavoriteCard(item));
        })
    } catch(error) {
        console.error("Error loading favorites:", error);
    }
}

document.getElementById("saveNote-btn").addEventListener("click", async function() {
    const token = localStorage.getItem("token");
    const newNote = document.getElementById("noteInput").value;

    const response = await fetch(`http://localhost:8080/saved-songs/${selectedSavedSongId}`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
            note: newNote
        })
    });

    if(response.ok) {
        bootstrap.Modal.getInstance(document.getElementById("modalNote")).hide();
        listFavorites();
    } else {
        alert("Error updating note");
    }
});

function createFavoriteCard(item) { 
    const artistCard = document.createElement("div");
            artistCard.className = "col-md-3 mb-4";
            artistCard.innerHTML = `
            <div class="card h-100">
            <img src="${item.imageUrl}" class="card-img-top" onerror="this.src='https://placehold.co/300x300/1a1a2e/6c63ff?text=🎵'">
                <div class="card-body">
                    <h5 class="card-title">${item.title}</h5>
                    <h4 class="card-artist">${item.artistName}</h4>
                    <p class="card-note">${item.note ? item.note : 'No note added'}</p>
                </div>
            </div>
            `;
            
            const viewNote = document.createElement("button");
            viewNote.textContent="View Note";
            viewNote.className="btn btn-primary mt-2";
            viewNote.addEventListener("click", async function() {

                selectedSavedSongId = item.id;

                document.getElementById("noteInput").value = item.note || "";
                
                const noteModalElement = document.getElementById("modalNote");
                const modalNote = new bootstrap.Modal(noteModalElement);

                modalNote.show();
            })

            const deleteFavBtn = document.createElement("button");
            deleteFavBtn.textContent="Delete";
            deleteFavBtn.className="btn btn-danger mt-2";
            deleteFavBtn.addEventListener("click", async function() {
                const confirmed = confirm("Are you sure you want to remove this from favorites?");
                if(confirmed) {
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
        }
    });
    const btnContainer = document.createElement("div");
    btnContainer.className = "d-flex justify-content-between mt-auto flex-wrap gap-1"
    btnContainer.appendChild(viewNote);
    btnContainer.appendChild(deleteFavBtn);
    artistCard.querySelector(".card-body").appendChild(btnContainer);

    return artistCard;
}

function showTextInput(placeholder) {
    filterValue.style.display = "inline-block";
    filterValue.disabled = false;
    filterValue.type = "text";
    filterValue.placeholder = placeholder;

    filterValueFrom.style.display = "none";
    filterValueTo.style.display = "none";
    filterValueFrom.disabled = true;
    filterValueTo.disabled = true;
}

function showDateInputs() {
    filterValue.style.display = "none";
    filterValue.disabled = true;

    filterValueFrom.type = "date";
    filterValueTo.type = "date";

    filterValueFrom.style.display = "inline-block";
    filterValueTo.style.display = "inline-block";
    filterValueFrom.disabled = false;
    filterValueTo.disabled = false;
}

function hideAllInputs() {
    filterValue.style.display = "none";
    filterValue.disabled = true;

    filterValueFrom.type = "date";
    filterValueTo.type = "date";

    filterValueFrom.style.display = "none";
    filterValueTo.style.display = "none";
    filterValueFrom.disabled = true;
    filterValueTo.disabled = true;
}

function clearFilters() {
    filterValue.value = "";
    filterValueFrom.value = "";
    filterValueTo.value = "";
}
