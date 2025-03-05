// Esto lo podrías poner en un archivo JS que se cargue en tu página de playlists
const userId = 1; // Cambia esto por el ID real del usuario si es necesario

function getPlaylists() {
  fetch(`/playlists/user/${userId}/CurrentPlaylists`)
    .then(response => response.json())
    .then(playlists => {
      // Aquí llenamos el HTML con las playlists
      const playlistContainer = document.getElementById('icon-grid');
      playlistContainer.innerHTML = ''; // Limpiamos cualquier contenido previo
      playlists.forEach(playlist => {
        const playlistCard = document.createElement('div');
        playlistCard.classList.add('col');
        
        playlistCard.innerHTML = `
          <div class="playlist-container">
            <h3>${playlist.name}</h3>
            <p>${playlist.description || 'Descripción breve.'}</p>
            <ul>
              <li><a href="#">Editar</a></li>
              <li><a href="#">Eliminar</a></li>
            </ul>

            <h4>Canciones</h4>
            <ul>
              ${playlist.songs.map(song => `<li>${song.title} <a href="#">Quitar</a></li>`).join('')}
            </ul>

            <a href="#" class="btn btn-playlist" data-bs-toggle="modal" data-bs-target="#addSongModal">Agregar Canción</a>
          </div>
        `;
        
        playlistContainer.appendChild(playlistCard);
      });
    })
    .catch(error => console.error('Error al obtener las playlists:', error));
}

// Llamamos a la función para cargar las playlists cuando la página se cargue
document.addEventListener('DOMContentLoaded', getPlaylists);
