document.addEventListener("DOMContentLoaded", () => {
    const playlistsContainer = document.getElementById("playlistsContainer");
    let currentPlaylistId = null; // Definimos currentPlaylistId aquí

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
                    <ul id="song-list-${playlist.id}">
                        <!-- Aquí se agregarán las canciones -->
                    </ul>
    
                    <!-- Modificar los botones de Agregar Canción -->
                    <a href="#" class="btn btn-playlist" data-bs-toggle="modal" data-bs-target="#addSongModal" data-playlist-id="${playlist.id}">Agregar Canción</a>
                </div>
            `;
            playlistsContainer.appendChild(playlistElement);
    
            // Obtener las canciones de la playlist
            fetch(`http://localhost:8080/playlists/playlist/${playlist.id}/details`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {
                console.log(data); // Verificar qué datos se reciben

                const songList = document.getElementById(`song-list-${playlist.id}`);
                songList.innerHTML = ''; // Limpiar la lista antes de agregar nuevas canciones

                if (data.songNames && data.songNames.length > 0) {
                    data.songNames.forEach((song, index) => {
                        const li = document.createElement('li');
                        li.textContent = song; // Aquí se asume que song es solo el nombre, necesitas songId

                        // Crear botón "Quitar"
                        const removeLink = document.createElement('a');
                        removeLink.href = "#";
                        removeLink.textContent = ' Quitar';
                        removeLink.classList.add('remove-song-btn');

                        // Agregar los atributos necesarios
                        removeLink.setAttribute('data-song-id', data.songIds[index]); // Se necesita un array de IDs en el DTO
                        removeLink.setAttribute('data-playlist-id', playlist.id);
                        removeLink.setAttribute('data-user-id', userId);

                        // Asignar evento al botón
                        removeLink.addEventListener('click', (event) => {
                            event.preventDefault();
                            const songId = event.target.getAttribute('data-song-id');
                            deleteSongFromPlaylist(playlist.id, songId); // ✅ Nombre correcto de la función
                        });
                        

                        li.appendChild(removeLink);
                        songList.appendChild(li);
                    });
                } else {
                    const li = document.createElement('li');
                    li.textContent = 'No hay canciones disponibles';
                    songList.appendChild(li);
                }
            })
            .catch(error => {
                console.error("Error al cargar las canciones:", error);
            });


            // Función para eliminar una canción de la playlist
            function deleteSongFromPlaylist(playlistId, songId) {
                fetch(`http://localhost:8080/playlists/playlist/${playlistId}/song/${songId}/user/${userId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Error al eliminar la canción de la playlist.");
                    }
                    return response.text();
                })
                .then(message => {
                    alert(message); // Mostrar mensaje de éxito
                    location.reload(); // Recargar la página para reflejar cambios
                })
                .catch(error => {
                    console.error("Error al eliminar la canción:", error);
                    alert("No se pudo eliminar la canción.");
                });
            }

    
            // Agregar el evento para cuando se hace clic en "Agregar Canción"
            document.querySelectorAll('.btn-playlist').forEach(button => {
                button.addEventListener('click', (event) => {
                    currentPlaylistId = event.target.getAttribute('data-playlist-id'); // Guardamos el ID de la playlist
                });
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
        });
    })
    .catch(error => {
        console.error("Error(no hay playlists para mostrar):", error);
        /* alert("No hay playlists para cargar."); */
    });
    

    // --------------------------------------------------------------------------------------------- //

    function loadSongs() {
        const songSelect = document.getElementById("songSelect");
    
        fetch('http://localhost:8080/songs', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('No autorizado o error en la solicitud. Status: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            songSelect.innerHTML = '';
    
            if (data && data.length > 0) {
                data.forEach(song => {
                    const option = document.createElement('option');
                    option.value = song.id;
                    option.textContent = song.name;
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

    document.getElementById('addSongModal').addEventListener('show.bs.modal', loadSongs);

    // --------------------------------------------------------------------------------------------- //

    // Función para agregar canción a la playlist
    function addSongToPlaylist() {
        // Verificamos que currentPlaylistId no sea null
        if (!currentPlaylistId) {
            console.error('No se ha seleccionado una playlist');
            alert('Por favor, selecciona una playlist.');
            return;
        }

        const songId = document.getElementById('songSelect').value;

        if (!token) {
            console.error('No se encontró el token de autorización.');
            return;
        }

        if (!songId) {
            console.error('Por favor, selecciona una canción.');
            return;
        }

        // Realizamos la solicitud POST para agregar la canción a la playlist
        fetch(`http://localhost:8080/users/user/${userId}/playlists/${currentPlaylistId}/songs/${songId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al agregar la canción a la playlist.');
            }
            return response.text(); // Si la respuesta es exitosa, obtenemos el mensaje
        })
        .then(message => {
            alert(message); // Mostramos el mensaje de éxito
            $('#addSongModal').modal('hide'); // Ocultamos el modal
            location.reload();  // Recargamos la página para ver los cambios
        })
        .catch(error => {
            console.error('Error al agregar canción:', error);
            alert('Hubo un problema al agregar la canción a la playlist.');
        });
    }

    // Asignamos la función al botón de agregar canción en el modal
    document.getElementById('addSongBtn').addEventListener('click', addSongToPlaylist);


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
                location.reload(); // Recargar la página después de la eliminación
            } /* else {  
                alert('Hubo un error al eliminar la playlist');
            } */
        })
        .catch(error => {
            console.error('Error al eliminar la playlist:', error);
            alert('Ocurrió un error inesperado');
        });
    }


});
