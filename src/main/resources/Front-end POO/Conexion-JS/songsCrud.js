document.addEventListener('DOMContentLoaded', function() {
    const songListContainer = document.querySelector('#songListContainer');
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('authToken');

    let selectedSongId = null; // ID de la canción seleccionada para editar

    // 🔹 Función para obtener las canciones del usuario
    function fetchSongs() {
        fetch(`http://localhost:8080/songs/user/${userId}/created-songs`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        })
        .then(response => response.json())
        .then(songs => {
            songListContainer.innerHTML = ''; // Limpiar contenedor

            songs.forEach(song => {
                const songCard = document.createElement('div');
                songCard.classList.add('col');

                songCard.innerHTML = `
                    <div class="playlist-container">
                        <h3>${song.name}</h3>
                        <ul>
                            <li>Género: ${song.genre}</li>
                            <li>Duración: ${song.duration} min</li>
                        </ul>
                        <a href="#" class="btn btn-playlist edit-song" data-bs-toggle="modal" data-bs-target="#addSongModal" data-song-id="${song.id}">Editar</a>
                        <a href="#" class="btn btn-playlist delete-song" data-song-id="${song.id}">Eliminar</a>
                    </div>
                `;

                songListContainer.appendChild(songCard);
            });

            // Agregar eventos a los botones de editar y eliminar
            document.querySelectorAll('.edit-song').forEach(button => {
                button.addEventListener('click', function() {
                    selectedSongId = this.getAttribute('data-song-id');
                    loadSongDataForEdit(selectedSongId);
                });
            });

            document.querySelectorAll('.delete-song').forEach(button => {
                button.addEventListener('click', function() {
                    const songId = this.getAttribute('data-song-id');
                    deleteSong(songId);
                });
            });
        })
        .catch(error => console.error('Error al obtener las canciones:', error));
    }

    // 🔹 Función para cargar los datos de la canción en el modal
    function loadSongDataForEdit(songId) {
        fetch(`http://localhost:8080/songs/song/${songId}`, {
            method: 'GET',
            headers: { 'Authorization': `Bearer ${token}` }
        })
        .then(response => response.json())
        .then(song => {
            document.getElementById('songTitle').value = song.name;
            document.getElementById('songGenre').value = song.genre;
            document.getElementById('songDuration').value = song.duration;
            document.getElementById('saveSongButton').textContent = 'Actualizar';
        })
        .catch(error => console.error('Error al cargar la canción:', error));
    }

    // 🔹 Función para eliminar canción
    function deleteSong(songId) {
        if (!confirm("¿Estás seguro de que deseas eliminar esta canción?")) return;

        fetch(`http://localhost:8080/songs/${songId}/user/${userId}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        })
        .then(response => {
            if (!response.ok) throw new Error("Error al eliminar la canción");
            return response.text();
        })
        .then(message => {
            alert(message);
            fetchSongs(); // Actualizar lista
        })
        .catch(error => console.error('Error al eliminar la canción:', error));
    }

    // 🔹 Evento para crear o actualizar canción
    document.getElementById('saveSongButton').addEventListener('click', function () {
        const title = document.getElementById('songTitle').value.trim();
        const genre = document.getElementById('songGenre').value.trim();
        const duration = parseFloat(document.getElementById('songDuration').value);

        if (!title || !genre || isNaN(duration) || duration <= 0) {
            alert('⚠️ Por favor, complete todos los campos correctamente.');
            return;
        }

        const songData = { name: title, genre: genre, duration: duration };

        if (selectedSongId) {
            // 🔄 **Actualizar canción**
            fetch(`http://localhost:8080/songs/user/${userId}/songs/${selectedSongId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(songData)
            })
            .then(response => {
                if (!response.ok) throw new Error("Error al actualizar la canción");
                return response.text();
            })
            .then(message => {
                alert(message);
                $('#addSongModal').modal('hide');
                fetchSongs(); // Recargar lista
            })
            .catch(error => console.error('Error al actualizar la canción:', error));
        } else {
            // 🎵 **Crear nueva canción**
            fetch(`http://localhost:8080/users/user/${userId}/createSong`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(songData)
            })
            .then(response => {
                if (!response.ok) throw new Error("Error al crear la canción");
                return response.text();
            })
            .then(message => {
                alert(message);
                $('#addSongModal').modal('hide');
                fetchSongs(); // Recargar lista
            })
            .catch(error => console.error('Error al crear la canción:', error));
        }

        // 🔄 **Resetear el formulario**
        selectedSongId = null;
        document.getElementById('saveSongButton').textContent = 'Guardar';
    });

    // 🔹 Cargar las canciones al iniciar
    fetchSongs();
});
