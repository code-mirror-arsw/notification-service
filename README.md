# Notification Service (Microservicio de Notificaciones)

Este microservicio, desarrollado en Java con Spring Boot, actúa como un sistema centralizado para el envío de notificaciones. Está diseñado para operar de manera asíncrona, escuchando eventos de un topic de Apache Kafka y despachando notificaciones a través de múltiples canales, como correo electrónico y notificaciones push (Firebase Cloud Messaging).

## ✨ Características Principales

*   **Arquitectura Orientada a Eventos:** El servicio es un consumidor de Kafka, lo que lo desacopla completamente de los servicios que originan las notificaciones.
*   **Soporte Multicanal:**
    *   **Correo Electrónico:** Envía emails a través de un servidor SMTP (configurable para Gmail u otros).
    *   **Notificaciones Push:** Envía notificaciones push a dispositivos móviles a través de Firebase Cloud Messaging (FCM).
*   **Resiliente y Escalable:** Al ser un consumidor de Kafka, puede escalar horizontalmente para manejar un mayor volumen de eventos y reintentar el procesamiento en caso de fallos temporales.
*   **Configuración Centralizada:** Toda la configuración se gestiona a través de un archivo `application.yml`, facilitando la adaptación a diferentes entornos.

## 🛠️ Tecnologías Utilizadas

*   **Lenguaje:** Java 17+
*   **Framework:** Spring Boot 3
*   **Mensajería:** Apache Kafka (Spring for Kafka)
*   **Notificaciones por Email:** Spring Boot Starter Mail (JavaMailSender)
*   **Notificaciones Push:** Firebase Admin SDK
*   **Build Tool:** Maven / Gradle

## 🏗️ Arquitectura de Despliegue

El servicio está diseñado para un despliegue aislado y robusto, donde sus dependencias críticas de mensajería se gestionan localmente para garantizar la máxima disponibilidad y control.

*   **Entorno de Ejecución:** El microservicio se ejecuta en su propio entorno, ya sea una máquina virtual (VM) o un contenedor Docker.
*   **Dependencias Dockerizadas:** Para asegurar un entorno consistente y fácil de gestionar, las dependencias clave, **Apache Kafka y Zookeeper**, se ejecutan como **contenedores Docker en la misma máquina host** que el servicio.
*   **Flujo de Comunicación:**
    1.  Otros microservicios publican eventos de notificación en un topic de Kafka.
    2.  El `notification-service` consume estos eventos.
    3.  Según el contenido del evento, el servicio se comunica con los proveedores externos (servidor SMTP, API de Firebase) para enviar la notificación final al usuario.


## Diagrama de clases


![image](https://github.com/user-attachments/assets/5e602149-784d-4992-8ec2-5290d64aa140)
