document.addEventListener("DOMContentLoaded", () => {
    const playlistsContainer = document.getElementById("playlistsContainer");

    // Obtener el token desde localStorage
    const token = localStorage.getItem("token");

    // Verificar si el token existe en localStorage
    if (!token) {
        alert("No estás autenticado. Redirigiendo al inicio de sesión.");
        window.location.href = "login.html";  // O redirige según tu flujo de inicio de sesión
        return;  // Si no hay token, terminar la ejecución
    }

    // Hacer la solicitud al backend con el token en el header Authorization
    fetch("http://localhost:8080/playlists/user/playlists", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`  // Asegúrate de que el token se pasa correctamente
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al obtener las playlists");
        }
        return response.json();
    })
    .then(playlists => {
        playlistsContainer.innerHTML = ""; // Limpiar antes de agregar nuevas playlists
        playlists.forEach(playlist => {
            const playlistElement = document.createElement("div");
            playlistElement.classList.add("col");
            playlistElement.innerHTML = `
                <div class="playlist-container">
                    <h3>${playlist.name}</h3>
                    <button class="btn btn-playlist" onclick="viewPlaylistDetails(${playlist.id})">Ver detalles</button>
                </div>
            `;
            playlistsContainer.appendChild(playlistElement);
        });
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Hubo un problema al cargar las playlists.");
    });
});
