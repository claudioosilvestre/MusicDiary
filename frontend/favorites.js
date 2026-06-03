listFavorites();

let selectedSavedSongId = null;

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
                    <p class="card-note">${item.note ? item.note : 'No note added'}</p>
                </div>
            </div>
            `;
            
            const viewNote = document.createElement("button");
            viewNote.textContent="View Note";
            viewNote.className="btn btn-primary";
            viewNote.addEventListener("click", async function() {

                selectedSavedSongId = item.id;

                document.getElementById("noteInput").value = item.note || "";
                
                const noteModalElement = document.getElementById("modalNote");
                const modalNote = new bootstrap.Modal(noteModalElement);

                modalNote.show();
            })

            artistCard.querySelector(".card-body").appendChild(viewNote);

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

            artistCard.querySelector(".card-body").appendChild(deleteFavBtn);
            document.getElementById("favorites").appendChild(artistCard);
        });

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
