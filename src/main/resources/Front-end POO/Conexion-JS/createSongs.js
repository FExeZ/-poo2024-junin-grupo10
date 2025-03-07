// hideSongs.js (o el archivo que corresponda)

document.addEventListener('DOMContentLoaded', function() {
    const createSongBtn = document.querySelector('#addSongModal .btn-playlist');
    const songTitle = document.querySelector('#songTitle');
    const songGenre = document.querySelector('#songGenre');
    const songDuration = document.querySelector('#songDuration');
    const token = localStorage.getItem('authToken');  // Asegúrate de que el token esté en el localStorage

    createSongBtn.addEventListener('click', function() {
        // Obtener los valores de los campos del formulario
        const title = songTitle.value.trim();
        const genre = songGenre.value.trim();
        const duration = songDuration.value.trim();

        // Validar los datos
        if (!title || !genre || !duration) {
            alert('Por favor, complete todos los campos.');
            return;
        }

        // Crear el objeto que será enviado al backend
        const newSong = {
            title: title,
            genre: genre,
            duration: parseFloat(duration)
        };

        // Obtener el tipo de usuario desde localStorage (esto es sólo si necesitas validarlo en el frontend)
        const userType = localStorage.getItem('userType');

        // Validar si el usuario es de tipo 'artist' antes de permitir la creación
        if (userType !== 'artist') {
            alert('Solo los artistas pueden crear canciones.');
            return;
        }

        // Hacer la solicitud POST para crear la canción
        fetch('http://localhost:8080/users/{userId}/createSong', {  // Cambia la URL a la correcta
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(newSong)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Hubo un error al crear la canción.');
            }
            return response.json();
        })
        .then(data => {
            alert('Canción creada exitosamente!');
            // Aquí puedes agregar la lógica para cerrar el modal si lo deseas
            $('#addSongModal').modal('hide');
        })
        .catch(error => {
            alert(error.message);
        });
    });
});
