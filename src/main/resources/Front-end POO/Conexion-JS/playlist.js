//Funcion para ver las playlists del usuario activo

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

    // Función para extraer el ID del usuario desde el token JWT
    function getUserIdFromToken(token) {
        const payload = JSON.parse(atob(token.split('.')[1]));  // Decodificar el payload del JWT
        return payload.userId;  // Asumir que el campo userId está presente en el payload
    }

    const userId = getUserIdFromToken(token); // Obtener el ID del usuario

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
                    <button class="delete-btn" data-id="${playlist.id}">Eliminar</button>
                </div>
            `;
            playlistsContainer.appendChild(playlistElement);
        });

        // Agregar el evento de eliminación a los botones
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', (event) => {
                deletePlaylist(event, userId);  // Pasar userId al evento de eliminación
            });
        });
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Hubo un problema al cargar las playlists.");
    });
});


/* ------------------------------------------------------------------------------------------------------ */


// Función para eliminar una playlist
function deletePlaylist(event, userId) {
    const playlistId = event.target.getAttribute('data-id');  // Obtener el ID de la playlist
    const playlistItem = event.target.closest('.playlist-container');  // Obtener el elemento de la playlist

    // Asegúrate de que el userId y playlistId están definidos correctamente
    if (!userId || !playlistId) {
        alert("No se pudo obtener el ID del usuario o de la playlist.");
        return;
    }

    fetch(`http://localhost:8080/playlists/user/${userId}/playlist/${playlistId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (response.ok) {
            // Eliminar el elemento de la interfaz
            playlistItem.remove();
            alert('Playlist eliminada correctamente');
        } else {
            alert('Hubo un error al eliminar la playlist');
        }
    })
    .catch(error => {
        console.error('Error al eliminar la playlist:', error);
        alert('Ocurrió un error inesperado');
    });
}


/* ------------------------------------------------------------------------------------------------------ */


