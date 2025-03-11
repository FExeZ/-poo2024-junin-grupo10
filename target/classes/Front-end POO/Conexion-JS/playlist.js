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
        return payload.userId;
    }

    const userId = getUserIdFromToken(token);

    // Mostrar las playlists
    fetch("http://localhost:8080/playlists/user/playlists", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(playlists => {
        playlistsContainer.innerHTML = ""; // Limpiar antes de agregar nuevas playlists
        playlists.forEach(playlist => {
            const playlistElement = document.createElement("div");
            playlistElement.classList.add("col");
            playlistElement.innerHTML = `
                <div class="playlist-container">
                    <h3>${playlist.name}</h3>
                    <button class="edit-btn" data-id="${playlist.id}" data-name="${playlist.name}">Editar</button>
                    <button class="delete-btn" data-id="${playlist.id}">Eliminar</button>
                    <h4>Canciones</h4>
                    <ul>
                        <li>Canción G <a href="#">Quitar</a></li>
                        <li>Canción H <a href="#">Quitar</a></li>
                    </ul>

                    <!-- Modificar los botones de Agregar Canción -->
                    <a href="#" class="btn btn-playlist" data-bs-toggle="modal" data-bs-target="#addSongModal">Agregar Canción</a>
                </div>
            `;
            playlistsContainer.appendChild(playlistElement);
        });

        // Agregar los eventos de edición
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', (event) => {
                const playlistId = event.target.getAttribute('data-id');
                const playlistName = event.target.getAttribute('data-name');
                openEditModal(playlistId, playlistName);  // Abrir el modal con los datos de la playlist
            });
        });

        // Agregar los eventos de eliminación
        document.querySelectorAll('.delete-btn').forEach(button => {
            button.addEventListener('click', (event) => {
                deletePlaylist(event, userId);  // Llamar a la función de eliminación
            });
        });
    })
    .catch(error => {
        console.error("Error:", error);
        alert("Hubo un problema al cargar las playlists.");
    });


    // --------------------------------------------------------------------------------------------- //

    function loadSongs() {
        // Recupera el token de localStorage (ajusta si usas otro lugar)
        const token = localStorage.getItem('token');  // Asegúrate de que el token esté aquí
    
        // Si no hay token, muestra un error y termina la ejecución
        if (!token) {
            console.error('No se encontró el token de autorización.');
            return;
        }
    
        const songSelect = document.getElementById("songSelect");
    
        // Hacemos la solicitud al endpoint para obtener las canciones
        fetch('http://localhost:8080/songs', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`  // Enviamos el token como Bearer
            }
        })
        .then(response => {
            // Verificamos si la respuesta es exitosa (200-299)
            if (!response.ok) {
                throw new Error('No autorizado o error en la solicitud. Status: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            // Limpiamos las opciones previas en el select
            songSelect.innerHTML = '';
    
            // Si la respuesta tiene canciones, las agregamos al select
            if (data && data.length > 0) {
                data.forEach(song => {
                    const option = document.createElement('option');
                    option.value = song.id;  // Suponiendo que 'id' es el identificador de la canción
                    option.textContent = song.name;  // Suponiendo que 'name' es el nombre de la canción
                    songSelect.appendChild(option);
                });
            } else {
                const option = document.createElement('option');
                option.textContent = 'No hay canciones disponibles';
                songSelect.appendChild(option);
            }
        })
        .catch(error => {
            console.error('Error al cargar canciones:', error);
        });
    }
    
    // Llamamos a la función cuando se abre el modal
    document.getElementById('addSongModal').addEventListener('show.bs.modal', loadSongs);


    // --------------------------------------------------------------------------------------------- //
    
    // Función para abrir el modal de edición
    function openEditModal(playlistId, playlistName) {
        const modal = document.getElementById('editModal');
        const playlistNameInput = document.getElementById('newPlaylistName');
        const editPlaylistForm = document.getElementById('editPlaylistForm');

        // Llenar el campo con el nombre actual
        playlistNameInput.value = playlistName;

        // Mostrar el modal
        modal.style.display = "block";

        // Manejar el envío del formulario
        editPlaylistForm.onsubmit = async (e) => {
            e.preventDefault(); // Prevenir el envío normal del formulario

            const newName = playlistNameInput.value;

            // Hacer la petición PUT para actualizar el nombre de la playlist
            const response = await fetch(`http://localhost:8080/playlists/playlist/${playlistId}/user/${userId}`, {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ name: newName })
            });

            if (!response.ok) {
                alert("Error al actualizar el nombre de la playlist");
            } else {
                alert("Nombre de la playlist actualizado");
                modal.style.display = "none"; // Cerrar el modal
                location.reload(); // Recargar la página para mostrar los cambios
            }
        };
    }

    // Cerrar el modal cuando se hace clic en el botón de cerrar
    document.querySelector('.close-btn').addEventListener('click', () => {
        document.getElementById('editModal').style.display = 'none';
    });

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
});
