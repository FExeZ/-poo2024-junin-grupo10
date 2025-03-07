document.getElementById("saveSongButton").addEventListener("click", async function() {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    if (!token || !userId) {
        alert("No se encontró el token o el ID del usuario. Inicia sesión nuevamente.");
        return;
    }

    const songTitle = document.getElementById("songTitle").value;
    const songGenre = document.getElementById("songGenre").value;
    const songDuration = parseInt(document.getElementById("songDuration").value);  // Usamos parseInt

    // Verificar que los campos no estén vacíos y la duración sea un número válido
    if (!songTitle || !songGenre || isNaN(songDuration) || songDuration <= 0) {
        alert("Por favor, ingresa todos los detalles de la canción correctamente (duración positiva).");
        return;
    }

    const songData = {
        name: songTitle,
        genre: songGenre,  // Asegúrate de que coincida con el tipo del enum en el backend
        duration: songDuration
    };

    console.log("Datos de la canción a enviar:", songData);  // Verifica los datos antes de enviarlos

    try {
        const response = await fetch(`http://localhost:8080/users/user/${userId}/createSong`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,  // Enviar el token correctamente
            },
            body: JSON.stringify(songData),
        });

        if (response.ok) {
            alert("Canción creada con éxito");
            const modal = bootstrap.Modal.getInstance(document.getElementById('addSongModal'));
            modal.hide();  // Cerrar el modal
            window.location.reload();  // Recargar la página para mostrar la nueva canción
        } else {
            const errorDetails = await response.json();
            console.error("Error al crear la canción:", errorDetails);
            alert(`Error: ${errorDetails.error || 'Error desconocido'}`);
        }
    } catch (error) {
        console.error("Error al crear la canción:", error);
        alert("Hubo un problema al crear la canción. Intenta nuevamente.");
    }
});
