document.addEventListener("DOMContentLoaded", () => {
  const navMenu = document.getElementById("menuNav");
  const usuarioLogueado = JSON.parse(localStorage.getItem("usuarioLogueado"));

  const createLink = (href, text, extraStyle = "") => {
    const link = document.createElement("a");
    link.href = href;
    link.innerText = text;
    if (extraStyle) {
      link.style.cssText = extraStyle;
    }
    return link;
  };

  const renderNav = () => {
    if (!navMenu) return;

    navMenu.innerHTML = "";

    navMenu.innerHTML += `<a href="/index.html">Inicio</a>`;
    navMenu.innerHTML += `<a href="/catalogo.html">Catálogo</a>`;

    if (usuarioLogueado) {
      let menuExtraHTML = "";

      if (usuarioLogueado.rol === "DUENO") {
        menuExtraHTML = `
                    <a href="/dashboard-dueno.html" style="color:#205c3b; font-weight:bold; margin-right:10px;">Dashboard</a>
                    <a href="/dueno/gestion-hospedajes.html" style="color:#205c3b; font-weight:bold; margin-right:15px;">Mis Hospedajes</a>
                `;
      } else if (usuarioLogueado.rol === "CLIENTE") {
        menuExtraHTML = `<a href="/mis-viajes.html" style="color:#205c3b; font-weight:bold; margin-right:15px;">Mis Viajes</a>`;
      } else if (usuarioLogueado.rol === "ADMIN") {
        menuExtraHTML = `<a href="/admin/panel.html" style="color:#d63031; font-weight:bold; margin-right:15px;">Admin</a>`;
      }

      navMenu.innerHTML += menuExtraHTML;

      navMenu.innerHTML += `
                <a href="/perfil-usuario.html" style="margin-left:10px; color:#555; font-size:0.9em; text-decoration:none; display:inline-flex; align-items:center; gap:5px;" title="Editar Perfil">
                    <img src="https://ui-avatars.com/api/?name=${usuarioLogueado.nombre}+${usuarioLogueado.apellidos}&background=205c3b&color=fff&rounded=true" width="30" style="border-radius:50%; margin-right:5px; box-shadow: 0 0 0 2px rgba(32, 92, 59, 0.2);">
                    <span>Hola, <b>${usuarioLogueado.nombre}</b></span>
                    <span title="Editar Perfil" style="font-size: 1.2em; color: #d63031;">✏️</span>
                </a>
            `;

      const logoutLink = createLink(
        "#",
        "Salir",
        "color:#d63031; font-weight:600; margin-left: 15px;"
      );
      logoutLink.addEventListener("click", logoutGlobal);
      navMenu.appendChild(logoutLink);
    } else {
      navMenu.appendChild(createLink("/login.html", "Iniciar Sesión"));
    }
  };

  const logoutGlobal = () => {
    if (confirm("¿Deseas cerrar sesión?")) {
      localStorage.clear();
      window.location.href = "/index.html";
    }
  };

  renderNav();

  const footer = document.querySelector("footer");
  if (footer) {
    footer.innerHTML = `
            <div class="footer-content">
                <div class="footer-section">
                    <div class="footer-brand">
                        <img src="/img/logo.jpg" alt="Logo EcoHospedajes">
                        <h3>EcoHospedajes</h3>
                    </div>
                    <p class="footer-description">Conectamos viajeros con la naturaleza y la cultura local de Perú, promoviendo el turismo sostenible y el desarrollo de comunidades rurales.</p>
                </div>
                <div class="footer-section footer-links">
                    <h4>Navegación</h4>
                    <ul>
                        <li><a href="/index.html">Inicio</a></li>
                        <li><a href="/catalogo.html">Catálogo de Hospedajes</a></li>
                        <li><a href="/registrate.html">Registrarse</a></li>
                        <li><a href="/login.html">Iniciar Sesión</a></li>
                    </ul>
                </div>
                <div class="footer-section footer-links">
                    <h4>Contáctanos</h4>
                    <ul>
                        <li><a href="#">📍 Lima, Perú</a></li>
                        <li><a href="#">📧 contacto@eco.com</a></li>
                        <li><a href="#">📞 +51 900 000 000</a></li>
                        <li style="margin-top: 15px;">
                            <a href="#">Facebook</a> | <a href="#">Instagram</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                &copy; 2025 EcoHospedajes Rurales — Todos los derechos reservados.
            </div>
        `;
  }
});
