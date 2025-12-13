# OuijaGames Mobile
**Aplicación Android para tienda de juegos de mesa con Kotlin + Jetpack Compose + API REST**

Este proyecto tiene como objetivo crear una aplicación móvil completa con **arquitectura MVVM**, **integración con API REST**, **autenticación con Firebase** y **visualización de productos** usando Kotlin y Jetpack Compose.

---

## Objetivo

Aprender paso a paso cómo:
- Implementar arquitectura MVVM en Android
- Consumir APIs REST con Retrofit
- Integrar autenticación con Firebase
- Crear interfaces modernas con Jetpack Compose
- Gestionar estado reactivo con StateFlow
- Navegar entre pantallas con Navigation Compose
- Cargar imágenes de forma eficiente con Coil

---

## Equipo

- **Michelle Diaz**
- **Kevin Morales**
- **Veronica Verde**

**Equipo:** OuijaGames Mobile  
**Sección:** 003D  
**Asignatura:** DSY1105 - Desarrollo de Aplicaciones Móviles  
**Institución:** DuocUC - 2025

---

## Requisitos previos

Antes de comenzar, asegúrate de tener:

**Android Studio** Hedgehog (2023.1.1) o superior  
**JDK 11** o superior  
**Android SDK 24+** (Android 7.0 Nougat o superior)  
**Emulador Android** o dispositivo físico

---

## Instalación

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/ouijagames-mobile.git
cd ouijagames-mobile
```

### 2️⃣ Abrir en Android Studio
- Abre Android Studio
- Ve a File → Open
- Selecciona la carpeta del proyecto
- Espera a que termine el Sync Gradle (puede tardar unos minutos)

### 3️⃣ Verificar configuración de Gradle
Asegúrate de que tu archivo build.gradle.kts (Module: app) tenga estas dependencias:
```kotlin
dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.material3:material3")
    
    // Retrofit para API REST
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    
    // Coil (imágenes)
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
}
```

### 4️⃣ Configurar permisos en AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 5️⃣ Ejecutar la aplicación
- Conecta un dispositivo físico o inicia un emulador Android
- Click en el botón Run ▶️ (o presiona Shift + F10)
- Selecciona tu dispositivo
- Espera a que compile e instale

---

## 📱 Funcionalidades principales

### ✅ Autenticación de usuarios
- Registro de nuevos usuarios con Firebase Auth
- Inicio de sesión con email y contraseña
- Gestión de sesión persistente
- Cierre de sesión

### ✅ Catálogo de productos
- Visualización de juegos de mesa desde API REST
- Imágenes cargadas dinámicamente con Coil
- Información detallada: nombre, descripción, precio, stock
- Organización por categorías (Rápidos, Lentos, Familiar, TCG, etc.)

### ✅ Gestión de productos (Administrador)
- Agregar nuevos productos a la tienda
- Formulario con validación en tiempo real
- Integración con API backend (Spring Boot)
- Actualización automática del catálogo

### ✅ Interfaz moderna
- Diseño con Material Design 3
- Animaciones fluidas
- Responsive design
- Modo claro optimizado

---

## Arquitectura del proyecto
```
app/
├── data/
│   ├── api/           # Servicios REST con Retrofit
│   ├── model/         # Modelos de datos (Product, Categoria)
│   └── repository/    # Repositorios para acceso a datos
├── ui/
│   ├── screens/       # Pantallas de la aplicación
│   ├── components/    # Componentes reutilizables
│   └── theme/         # Configuración de tema
├── viewmodel/         # ViewModels con lógica de negocio
└── navigation/        # Configuración de navegación
```

---

## 🔌 Integración con API

La aplicación se conecta a la API REST de OuijaGames:

**Base URL:** `https://ouijagames-back.onrender.com/api/`

**Endpoints utilizados:**
- `GET /products` - Obtener todos los productos
- `GET /products/{id}` - Obtener producto por ID
- `POST /products` - Crear nuevo producto
- `GET /categories` - Obtener todas las categorías

---

## 🎨 Tecnologías utilizadas

- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - UI moderna declarativa
- **Retrofit** - Cliente HTTP para consumir API REST
- **Coil** - Carga de imágenes optimizada
- **Firebase Auth** - Autenticación de usuarios
- **Firebase Firestore** - Base de datos en la nube
- **Navigation Compose** - Navegación entre pantallas
- **StateFlow** - Manejo de estado reactivo
- **MVVM** - Patrón de arquitectura

---

## 📄 Licencia

Este proyecto es desarrollado con fines educativos para la asignatura DSY1105 de DuocUC.

---

## 👥 Contribuciones

Este es un proyecto académico. Para sugerencias o mejoras, contacta al equipo de desarrollo.
