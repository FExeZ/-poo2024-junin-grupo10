function deleteSongFromPlaylist(playlistId, songId) {
    const userId = 1; // Cambiar por el ID del usuario actual
  
    fetch(`/playlists/playlist/${playlistId}/song/${songId}/user/${userId}`, {
      method: 'DELETE',
    })
      .then(response => response.json())
      .then(data => {
        if (data.message) {
          alert(data.message); // Mostrar mensaje de éxito
          getPlaylists(); // Recargar las playlists
        }
      })
      .catch(error => console.error('Error al eliminar la canción:', error));
  }
  