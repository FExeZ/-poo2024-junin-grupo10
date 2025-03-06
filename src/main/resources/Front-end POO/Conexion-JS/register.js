document.addEventListener("DOMContentLoaded", function () {
    const registerForm = document.querySelector("form");

    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault(); // Evita el envío del formulario por defecto

        // Capturar valores del formulario
        const username = document.getElementById("fullName").value.trim();
        const password = document.getElementById("password").value.trim();
        const userType = document.getElementById("userType").value;

        // Validación básica
        if (!username || !password || !userType) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        // Definir la URL según el tipo de usuario
        const apiUrl =
            userType === "artista"
                ? "http://localhost:8080/users/artist/registrar"
                : "http://localhost:8080/users/enthusiast/registrar";

        // Crear objeto con los datos del usuario
        const userData = {
            username: username,
            password: password, // El backend lo encripta, así que enviamos en texto plano
        };

        try {
            // Enviar solicitud al backend
            const response = await fetch(apiUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                alert("Usuario registrado con éxito. Ahora puedes iniciar sesión.");
                window.location.href = "login.html"; // Redirigir al login
            } else if (response.status === 409) {
                alert("El nombre de usuario ya está en uso. Intenta con otro.");
            } else {
                alert("Error al registrar usuario. Inténtalo nuevamente.");
            }
        } catch (error) {
            console.error("Error en el registro:", error);
            alert("Error de conexión con el servidor.");
        }
    });
});
