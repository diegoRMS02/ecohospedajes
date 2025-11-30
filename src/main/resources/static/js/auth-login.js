// auth-login.js

document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.querySelector("form"); // Asumimos que es el único form en login.html

  if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
      e.preventDefault(); // Evita que la página se recargue sola

      // 1. Capturar datos del formulario
      const email = document.getElementById("email")?.value;
      const password = document.getElementById("password")?.value;

      if (!email || !password) {
        alert("Por favor, completa todos los campos.");
        return;
      }

      try {
        // 2. Enviar petición al Backend (Endpoint que acabamos de proteger)
        const response = await fetch("/api/usuarios/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ email, password }),
        });

        if (response.ok) {
          const data = await response.json();

          // 3. ¡ÉXITO! Guardamos el Token
          const token = data.token;
          localStorage.setItem("jwt", token);

          // 4. Decodificamos el token para sacar el nombre y rol (Truco para compatibilidad)
          // El token tiene 3 partes separadas por puntos. La segunda es el payload.
          const payloadBase64 = token.split(".")[1];
          const decodedJson = atob(payloadBase64);
          const payload = JSON.parse(decodedJson);

          // Guardamos usuarioLogueado para que tu logic-navbar.js siga funcionando
          const usuarioInfo = {
            id: payload.id,
            nombre: payload.nombre, // Asegúrate que TokenService incluya esto
            apellidos: payload.apellidos,
            rol: payload.rol,
            email: payload.sub,
          };
          localStorage.setItem("usuarioLogueado", JSON.stringify(usuarioInfo));

          alert("¡Bienvenido!");

          // 5. Redirigir según el rol
          if (payload.rol === "ADMIN") {
            window.location.href = "/admin/panel.html";
          } else if (payload.rol === "DUENO") {
            window.location.href = "/dashboard-dueno.html";
          } else {
            window.location.href = "/index.html";
          }
        } else {
          alert("Error: Credenciales incorrectas");
        }
      } catch (error) {
        console.error("Error de conexión:", error);
        alert("Hubo un problema al intentar conectar con el servidor.");
      }
    });
  }
});
