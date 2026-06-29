const totalSavedSongsInput = document.getElementById("total-songs-value");
const mostSavedArtistInput = document.getElementById("most-saved-artist-value");
const lastSavedSongInput = document.getElementById("last-saved-song-value");

async function loadDashboard() {
    const url = "http://localhost:8080/dashboard"

    try{
        const response = await fetch(url, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });
        if(!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const dashboard = await response.json();

        totalSavedSongsInput.textContent = dashboard.totalMusicsSaved;

        if(dashboard.mostSavedArtist === null) {
            mostSavedArtistInput.textContent = "No songs registered";
        } else {
            mostSavedArtistInput.textContent = dashboard.mostSavedArtist;
        }

        if(dashboard.lastSavedSong === null) {
            lastSavedSongInput.textContent = "No songs registered";
        } else {
            lastSavedSongInput.textContent = dashboard.lastSavedSong.title + " - " + dashboard.lastSavedSong.artistName;
        }

        const labels = dashboard.monthCountList.map(item => item.month);
        const values = dashboard.monthCountList.map(item => item.count);

        new Chart(document.getElementById("monthly-chart"), {
            type: "bar",
            data: {     
            labels: labels,
            datasets: [{
            label: "Month Graphic",
            data: values, 
            backgroundColor: "#6c63ff",
            borderColor: "#1105f7"
        }]
    },
    options: {
        scales: {
            x: {
                ticks: { color: "#ffffff" }
            },
            y: {
                ticks: { color: "#ffffff" }
            }
        },
        plugins: {
            legend: {
                labels: {
                    color: "#ffffff"
                }
            }
        }
    }
});

        return dashboard;

    } catch(error) {
        console.error("Failed sending GET:", error)
    }
}

loadDashboard();

