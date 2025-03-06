
document.getElementById("logoutBtn").addEventListener("click", () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userType");
    localStorage.removeItem("userId"); // Asegura que el userId también se borre

    alert("Sesión cerrada correctamente.");
    window.location.href = "login.html"; // Redirige al login
});

