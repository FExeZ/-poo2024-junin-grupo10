document.addEventListener('DOMContentLoaded', function() {
    const songListContainer = document.querySelector('#songListContainer');  // Contenedor de las canciones
    const userId = localStorage.getItem('userId');  // ID del usuario
    const token = localStorage.getItem('authToken');  // Token de autenticación

    // Función para obtener las canciones creadas por el usuario
    function fetchSongs() {
        fetch(`http://localhost:8080/songs/user/${userId}/created-songs`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => response.json())
        .then(songs => {
            songListContainer.innerHTML = '';  // Limpiar el contenedor antes de agregar las canciones
            songs.forEach(song => {
                // Crear un nuevo div para cada canción con la clase 'col'
                const songCard = document.createElement('div');
                songCard.classList.add('col');  // Agregar la clase 'col' para que se ajuste a la cuadrícula de Bootstrap

                // Crear el contenido de la tarjeta de canción respetando la estructura original
                songCard.innerHTML = `
                    <div class="playlist-container">
                        <h3>${song.name}</h3>
                        <ul>
                            <li>Genero: ${song.genre}</li>
                            <!-- Puedes agregar más detalles aquí si lo deseas -->
                        </ul>
                        <a href="#" class="btn btn-playlist" data-bs-toggle="modal" data-bs-target="#addSongModal">Editar</a>
                        <a href="#" class="btn btn-playlist">Eliminar</a>
                    </div>
                `;
                
                // Agregar la tarjeta al contenedor de canciones
                songListContainer.appendChild(songCard);
            });
        })
        .catch(error => {
            console.error('Error al obtener las canciones:', error);
        });
    }

    // Llamar a la función para cargar las canciones al cargar la página
    fetchSongs();
});
