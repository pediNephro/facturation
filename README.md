# 💳 Invoice Management Microservice

## 📌 Overview

This project is a **Spring Boot microservice** designed to handle:

* Invoice (Facture) generation
* Medical actions / services management
* Dynamic price calculation with **insurance-based reductions**

It is part of a **microservices-based healthcare platform**, where each service is independent and communicates via REST APIs.

---

## 🚀 Features

### 🧾 1. Invoice Generation

* Create invoices linked to patients
* Automatic calculation of total amount
* Support for multiple actions/services per invoice
* Generate invoice as **PDF document**

### ⚙️ 2. Action Management

* CRUD operations for medical actions (consultation, analysis, etc.)
* Each action includes:

  * Name
  * Base price
  * Description
* Actions can be reused across multiple invoices

### 🧠 3. Dynamic Pricing (Insurance-Based)

* Apply **discounts automatically** based on patient insurance plan


* Flexible rule system for price calculation

### 💰 4. Payment Integration (Optional)

* Generate payment link (Stripe / PayPal)
* Include link in email sent to patient
* Embed QR code in PDF invoice for quick payment

### 📧 5. Email Notifications

* Send invoice directly to patient via email
* Attach generated PDF
* Include payment link

---

## 🏗️ Architecture

This microservice follows a **layered architecture**:

```
Controller → Service → Repository → Database
```

### Components

* **Controller Layer**

  * Exposes REST APIs
  * Handles HTTP requests/responses

* **Service Layer**

  * Business logic
  * Invoice calculation
  * Insurance reduction logic

* **Repository Layer**

  * Spring Data JPA
  * Database interaction

* **Entities**

  * Facture (Invoice)
  * Action


---

## 🧮 Pricing Logic

The final invoice amount is calculated as:

```
Total = Σ(Action Price) - Insurance Reduction
```

### Example

| Action       | Price |
| ------------ | ----- |
| Consultation | 100   |
| Analysis     | 200   |

Subtotal = 300
Insurance (20%) = -60

👉 Final Total = 240

---

## 🗂️ Project Structure

```
src/main/java/
 ├── controller/
 ├── service/
 ├── repository/
 ├── entity/
 ├── dto/
 └── config/
```

---

## 🔌 API Endpoints (Examples)

### 📄 Invoice

* `POST /api/factures` → Create invoice
* `GET /api/factures/{id}` → Get invoice by ID
* `GET /api/factures` → Get all invoices
* `DELETE /api/factures/{id}` → Delete invoice

### ⚙️ Actions

* `POST /api/actions` → Create action
* `GET /api/actions` → List actions

---

## 🛠️ Technologies Used

* **Java 17**
* **Spring Boot**
* Spring Data JPA
* Hibernate
* MySQL (or MongoDB optional)
* OpenHTMLtoPDF (PDF generation)
* JavaMailSender (email service)

---

## ⚡ Setup & Installation

### 1. Clone the repository

```bash
git clone https://github.com/your-repo/invoice-microservice.git
cd invoice-microservice
```

### 2. Configure application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/invoice_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### 3. Run the application

```bash
mvn spring-boot:run
```

---

## 🔄 Future Improvements

* Microservice communication via **Eureka & API Gateway**
* Kafka for event-driven invoice generation
* Advanced insurance rule engine
* Dashboard analytics (revenue, payments)
* Multi-currency s
