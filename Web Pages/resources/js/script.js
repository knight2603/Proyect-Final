function scrollToElement(elementSelector, instance = 0) {
    // Select all elements that match the given selector
    const elements = document.querySelectorAll(elementSelector);
    // Check if there are elements matching the selector and if the requested instance exists
    if (elements.length > instance) {
        // Scroll to the specified instance of the element
        elements[instance].scrollIntoView({ behavior: 'smooth' });
    }
}

const link1 = document.getElementById("link1");
const link2 = document.getElementById("link2");
const link3 = document.getElementById("link3");

link1.addEventListener('click', () => {
    scrollToElement('.header');
});

link2.addEventListener('click', () => {
    // Scroll to the second element with "header" class
    scrollToElement('.header', 1);
});

link3.addEventListener('click', () => {
    scrollToElement('.column');
});
// Esperar a que el DOM se cargue completamente
document.addEventListener('DOMContentLoaded', function() {
    // 1. FUNCIONALIDAD DEL BOTÓN DE TEMA (MODO OSCURO/CLARO)
    const themeToggle = document.getElementById('themeToggle');
    const themeIcon = document.querySelector('.theme-icon');
    const body = document.body; // O document.documentElement si prefieres cambiar en <html>

    // Función para cargar el tema guardado en localStorage
    function loadTheme() {
        const savedTheme = localStorage.getItem('theme') || 'dark'; // Por defecto oscuro
        if (savedTheme === 'light') {
            body.setAttribute('data-theme', 'light');
            themeIcon.textContent = '☀️'; // Sol para modo claro
        } else {
            body.removeAttribute('data-theme');
            themeIcon.textContent = '🌙'; // Luna para modo oscuro
        }
    }

    // Función para alternar el tema
    function toggleTheme() {
        const currentTheme = body.getAttribute('data-theme');
        if (currentTheme === 'light') {
            body.removeAttribute('data-theme');
            themeIcon.textContent = '🌙';
            localStorage.setItem('theme', 'dark');
        } else {
            body.setAttribute('data-theme', 'light');
            themeIcon.textContent = '☀️';
            localStorage.setItem('theme', 'light');
        }
    }

    // Event listener para el botón de tema
    if (themeToggle) {
        themeToggle.addEventListener('click', toggleTheme);
    }

    // Cargar el tema al inicio
    loadTheme();

    // 2. ANIMACIONES DE FADE-IN AL HACER SCROLL
    const fadeInSections = document.querySelectorAll('.fade-in-section');

    const observerOptions = {
        threshold: 0.1, // Activa cuando el 10% de la sección es visible
        rootMargin: '0px 0px -50px 0px' // Un poco antes de que entre completamente en vista
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                // Opcional: Desconectar el observer una vez visible para optimizar rendimiento
                // observer.unobserve(entry.target);
            }
        });
    }, observerOptions);

    // Observar todas las secciones
    fadeInSections.forEach(section => {
        observer.observe(section);
    });

    // 3. EFECTO PARALLAX SUTIL EN LA IMAGEN DEL HEADER
    const parallaxImage = document.querySelector('.parallax img');
    let scrollPosition = 0;

    function updateParallax() {
        if (parallaxImage) {
            scrollPosition = window.pageYOffset;
            const speed = 0.5; // Velocidad del parallax (ajusta si quieres más/menos movimiento)
            parallaxImage.style.transform = `translateY(${scrollPosition * speed}px)`;
        }
    }

    // Escuchar el scroll para actualizar el parallax
    window.addEventListener('scroll', updateParallax);

    // Opcional: Suavizar el scroll en los enlaces de navegación (para un diseño más llamativo)
    const navLinks = document.querySelectorAll('.nav-links a');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href');
            if (targetId.startsWith('#')) {
                const targetSection = document.querySelector(targetId);
                if (targetSection) {
                    targetSection.scrollIntoView({ behavior: 'smooth' });
                }
            } else {
                window.location.href = targetId;
            }
        });
    });

    // Opcional: Animación adicional en botones al hover (ya manejada en CSS, pero JS para más interactividad si quieres)
    const buttons = document.querySelectorAll('.btn, .btn-ingresar');
    buttons.forEach(btn => {
        btn.addEventListener('mouseenter', function() {
            this.style.transform = 'scale(1.05)';
        });
        btn.addEventListener('mouseleave', function() {
            this.style.transform = 'scale(1)';
        });
    });
});
