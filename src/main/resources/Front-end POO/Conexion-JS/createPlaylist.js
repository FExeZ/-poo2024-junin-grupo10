// Este código verifica el token y extrae el userId del token.
function checkUserAuthentication() {
    const token = localStorage.getItem("token");
    const userType = localStorage.getItem("userType");

    if (token) {
        // Decodificar el token
        const decodedToken = jwt_decode(token.replace('Bearer ', '')); // Asegurarse de quitar "Bearer " del token
        const userId = decodedToken.userId;

        console.log("Decoded userId:", userId);

        // Verificar si el userId y userType existen en el localStorage
        if (!userId || !userType) {
            alert("No se encontró el ID del usuario o el tipo de usuario. Inicia sesión nuevamente.");
            return;
        }

        // Guardamos el userId en localStorage si no está presente
        if (!localStorage.getItem("userId")) {
            localStorage.setItem("userId", userId);
        }

        console.log("UserType:", userType);
    } else {
        alert("No se encontró el token. Inicia sesión.");
    }
}

// Llamamos a esta función cuando la página cargue para verificar la autenticación del usuario
window.onload = function() {
    checkUserAuthentication();
};

/* --------------------------------------------------------------------------------------------- */

// Función para crear la playlist
document.getElementById("createPlaylistBtn").addEventListener("click", async function() {
    const token = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    if (!token || !userId) {
        alert("No se encontró el token o el ID del usuario. Inicia sesión nuevamente.");
        return;
    }

    const playlistTitle = document.getElementById("playlistTitle").value;

    if (!playlistTitle) {
        alert("Por favor, ingresa un título para la playlist.");
        return;
    }

    const playlistData = {
        name: playlistTitle,
    };

    try {
        const response = await fetch(`http://localhost:8080/users/user/${userId}/createPlaylist`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token,  // Enviar el token JWT en los headers
            },
            body: JSON.stringify(playlistData),
        });

        if (response.ok) {
            alert("Playlist creada con éxito");
            window.location.reload();  // Recargar la página para mostrar la nueva playlist
        } else {
            const errorMessage = await response.text();
            alert(`Error: ${errorMessage}`);
        }
    } catch (error) {
        console.error("Error al crear la playlist:", error);
        alert("Hubo un problema al crear la playlist. Intenta nuevamente.");
    }
});
