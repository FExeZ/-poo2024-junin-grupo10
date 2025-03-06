document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.querySelector("form");

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault(); // Evita el envío por defecto del formulario

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const userType = document.getElementById("userType").value; // Captura el tipo de usuario seleccionado

        const loginData = {
            username: username,
            password: password,
            userType: userType // Incluye el tipo de usuario aquí
        };

        let url = "";
        if (userType === "artist") {
            url = "http://localhost:8080/users/artist/auth"; // URL para artistas
        } else if (userType === "enthusiast") {
            url = "http://localhost:8080/users/enthusiast/auth"; // URL para entusiastas
        } else {
            alert("Tipo de usuario no válido.");
            return; // Detiene el proceso si el tipo de usuario no es válido
        }

        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(loginData),
                credentials: "include"
            });

            if (!response.ok) {
                throw new Error("Error en la autenticación");
            }

            const token = await response.text(); // Usamos response.text() para obtener el token como texto

            // Si el token no es válido o está vacío, no continuar
            if (!token || token.trim() === "") {
                alert("Error: Token no recibido.");
                return;
            }

            // Guarda el token y el tipo de usuario en localStorage
            localStorage.setItem("token", token);
            localStorage.setItem("userType", userType);

            // 🔹 Nueva petición para obtener el userId y guardarlo en localStorage
            const userResponse = await fetch("http://localhost:8080/users/me", {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (!userResponse.ok) {
                throw new Error("Error al obtener la información del usuario");
            }

            const userData = await userResponse.json();
            localStorage.setItem("userId", userData.id); // Guarda el userId

            alert("Inicio de sesión exitoso");
            window.location.href = "index.html"; // Redirige a la página principal
        } catch (error) {
            alert("Error: " + error.message);
        }
    });
});
