document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.querySelector("form"); 

  if (loginForm) {
    loginForm.addEventListener("submit", async (e) => {
      e.preventDefault(); 

      const email = document.getElementById("email")?.value;
      const password = document.getElementById("password")?.value;

      if (!email || !password) {
        alert("Por favor, completa todos los campos.");
        return;
      }

      try {
        const response = await fetch("/api/usuarios/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ email, password }),
        });

        if (response.ok) {
          const data = await response.json();

          const token = data.token;
          localStorage.setItem("jwt", token);

          const payloadBase64 = token.split(".")[1];
          const decodedJson = atob(payloadBase64);
          const payload = JSON.parse(decodedJson);

          const usuarioInfo = {
            id: payload.id,
            nombre: payload.nombre, 
            apellidos: payload.apellidos,
            rol: payload.rol,
            email: payload.sub,
          };
          localStorage.setItem("usuarioLogueado", JSON.stringify(usuarioInfo));

          alert("¡Bienvenido!");

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
