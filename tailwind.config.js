/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.{html,thymeleaf}",
    "./src/main/resources/static/**/*.js",
  ],
  theme: {
    extend: {},
  },
  plugins: [require("daisyui")],

  daisyui: {
    themes: [
      "light", 
      "dark", 
      {
        coordinador: {
          "primary": "#1e40af",        // Azul más profesional y serio
          "secondary": "#3b82f6",      // Azul más claro
          "accent": "#60a5fa",         // Azul claro para acentos
          "neutral": "#1e3a8a",        // Azul oscuro
          "base-100": "#eff6ff",       // Azul muy claro de fondo
          "base-200": "#dbeafe",
          "base-300": "#bfdbfe",
          "info": "#3b82f6",
          "success": "#10b981",
          "warning": "#f59e0b",
          "error": "#dc2626",
        },
        admin: {
          "primary": "#7f1d1d",        // Rojo oscuro carmesí
          "primary-focus": "#991b1b",  // Rojo más intenso al hacer focus
          "primary-content": "#ffffff", // Texto blanco sobre botones rojos
          "secondary": "#dc2626",      // Rojo vibrante
          "secondary-focus": "#b91c1c",
          "secondary-content": "#ffffff",
          "accent": "#ef4444",         // Rojo más claro para acentos
          "accent-focus": "#dc2626",
          "accent-content": "#ffffff",
          "neutral": "#0f172a",        // Negro azulado muy oscuro
          "neutral-focus": "#1e293b",
          "neutral-content": "#f1f5f9",
          "base-100": "#fef2f2",       // Fondo blanco rosado sutil
          "base-200": "#fecaca",       // Fondo secundario
          "base-300": "#fca5a5",       // Fondo terciario
          "base-content": "#1f2937",   // Texto oscuro sobre fondos claros
          "info": "#3b82f6",
          "success": "#10b981", 
          "warning": "#f59e0b",
          "error": "#b91c1c",  
        },
        impulsador: {
          "primary": "#7c3aed",        // Morado más vibrante
          "secondary": "#8b5cf6",      // Morado medio
          "accent": "#a78bfa",         // Morado claro
          "neutral": "#4c1d95",        // Morado oscuro
          "base-100": "#faf5ff",       // Lila muy claro de fondo
          "base-200": "#f3e8ff",
          "base-300": "#e9d5ff",
          "info": "#8b5cf6",
          "success": "#10b981",
          "warning": "#f59e0b",
          "error": "#dc2626",
        },
        vendedor: {
          "primary": "#047857",
          "secondary": "#10b981",
          "accent": "#6ee7b7",
          "neutral": "#064e3b",
          "base-100": "#ecfdf5",
          "info": "#3b82f6",
          "success": "#10b981",
          "warning": "#fbbf24",
          "error": "#ef4444",
        }
      }
    ],
    darkTheme: "admin", // ⬅️ Esto hace que el tema oscuro sea admin también
  base: true,
  styled: true,
  utils: true,
  prefix: "",
  logs: true,
  themeRoot: ":root",
  },
};