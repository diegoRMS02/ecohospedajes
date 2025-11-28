// Se ejecuta cuando carga la página
document.addEventListener("DOMContentLoaded", () => {
  const usuarioStorage = localStorage.getItem("usuarioLogueado");
  const nav = document.getElementById("menuNav");

  if (usuarioStorage && nav) {
    const usuario = JSON.parse(usuarioStorage);

    let botonRol = "";

    // Definimos el botón especial según el rol
    if (usuario.rol === "DUENO") {
      botonRol = `<a href="/dashboard-dueno.html" style="color:#205c3b; font-weight:bold;">Mi Panel</a>`;
    } else if (usuario.rol === "CLIENTE") {
      botonRol = `<a href="/mis-viajes.html" style="color:#205c3b; font-weight:bold;">Mis Viajes</a>`;
    } else if (usuario.rol === "ADMIN") {
      botonRol = `<a href="/admin/panel.html">Admin</a>`;
    }

    // Reconstruimos el menú
    nav.innerHTML = `
            <a href="/index.html">Inicio</a>
            <a href="/catalogo.html">Catálogo</a>
            ${botonRol}
            <span style="margin-left:15px; color:#555; font-size:0.9em;">
                Hola, <b>${usuario.nombre}</b> (${usuario.rol})
            </span>
            <a href="#" onclick="logoutGlobal()" style="color:#d63031; margin-left:15px; font-weight:600;">Salir</a>
        `;
  }
});

// Función global para cerrar sesión
function logoutGlobal() {
  if (confirm("¿Deseas cerrar sesión?")) {
    localStorage.clear();
    window.location.href = "/index.html";
  }
}
