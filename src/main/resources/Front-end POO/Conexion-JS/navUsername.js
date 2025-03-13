async function fetchAndDisplayUsername() {
    const token = localStorage.getItem("token");

    if (!token) {
        console.error("No se encontró el token JWT");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/users/me", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.error("Respuesta del servidor:", response);
            throw new Error("Error al obtener los datos del usuario");
        }

        const userData = await response.json();
        console.log("Datos del usuario recibidos:", userData);

        const username = userData.username; // Cambiado aquí
        console.log("Nombre de usuario extraído:", username);

        const usernamePlaceholder = document.getElementById("usernamePlaceholder");
        if (usernamePlaceholder) {
            usernamePlaceholder.textContent = username;
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

document.addEventListener("DOMContentLoaded", fetchAndDisplayUsername);