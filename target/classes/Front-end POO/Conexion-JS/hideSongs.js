document.addEventListener("DOMContentLoaded", function () {
    console.log("El script hideSongs.js se está ejecutando...");

    // Obtener el tipo de usuario desde localStorage
    const userType = localStorage.getItem("userType");
    console.log("Tipo de usuario obtenido:", userType);

    // Verificar si el tipo de usuario es 'enthusiast'
    if (userType === "enthusiast") {
        console.log("Usuario es entusiasta, ocultando la opción de Canciones...");

        // Intentar obtener el enlace con id 'ocultar'
        const cancionesLink = document.getElementById("ocultar");

        // Comprobar si el enlace existe
        if (cancionesLink) {
            // Eliminar el elemento del DOM
            cancionesLink.remove();
            console.log("Se eliminó el enlace de Canciones.");
        } else {
            console.log("No se encontró el enlace de Canciones con id 'ocultar'.");
        }
    }
});
