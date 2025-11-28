document.addEventListener("DOMContentLoaded", () => {
  // 1. Obtener datos de sesión
  const usuarioStorage = localStorage.getItem("usuarioLogueado");
  const nav = document.getElementById("menuNav");

  // Solo ejecutamos si hay usuario logueado y el menú existe en el HTML
  if (usuarioStorage && nav) {
    const usuario = JSON.parse(usuarioStorage);

    let botonRol = "";

    // 2. Definir el botón especial según el Rol
    if (usuario.rol === "DUENO") {
      // Botón para ir al Dashboard de Ganancias
      botonRol = `<a href="/dashboard-dueno.html" style="color:#205c3b; font-weight:bold; margin-right:15px;">Mi Panel</a>`;
    } else if (usuario.rol === "CLIENTE") {
      // Botón para ver sus reservas
      botonRol = `<a href="/mis-viajes.html" style="color:#205c3b; font-weight:bold; margin-right:15px;">Mis Viajes</a>`;
    } else if (usuario.rol === "ADMIN") {
      // Botón para borrar usuarios
      botonRol = `<a href="/admin/panel.html" style="color:#d63031; font-weight:bold; margin-right:15px;">Admin</a>`;
    }

    // 3. Reconstruir el HTML del Navbar
    nav.innerHTML = `
            <a href="/index.html">Inicio</a>
            <a href="/catalogo.html">Catálogo</a>
            
            ${botonRol} <a href="/perfil-usuario.html" style="margin-left:10px; color:#555; font-size:0.9em; text-decoration:none; display:inline-flex; align-items:center; gap:5px;" title="Editar mi perfil">
                <span>Hola, <b>${usuario.nombre}</b></span>
                <span style="font-size:1.2em">✏️</span>
            </a>

            <a href="#" onclick="logoutGlobal()" style="color:#d63031; margin-left:15px; font-weight:600;">Salir</a>
        `;
  }
});

// Función Global para Cerrar Sesión
function logoutGlobal() {
  if (confirm("¿Deseas cerrar sesión?")) {
    localStorage.clear(); // Borra los datos del navegador
    window.location.href = "/index.html"; // Redirige al inicio
  }
}
