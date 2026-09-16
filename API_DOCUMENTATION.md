# File Uploader Microservice: API Documentation & Frontend Integration Guide

This document provides a comprehensive specification of the `file-service` microservice APIs, details the core workflow patterns (such as the two-stage dynamic file uploading), and provides detailed guidance for integrating a frontend administration dashboard.

---

## 🏗️ Architecture & Core Concepts

### 1. Two-Stage File Lifecycle
To prevent storage clutter and orphan files, the service implements a decoupled **Client-Server Temp-to-Permanent** pattern:

1. **Upload to Temporary Buffer (Client/Frontend):** The client uploads a file via `POST /api/upload`. The file-service saves it under a `/temp` folder and returns a temporary access `url` and a relative `staticPath` (e.g. `/lms/employeeimage/png/myphoto_1716948271a2d3f.png`). The client uses the temporary URL to display the image on the UI immediately and passes the `staticPath` to its primary backend.
2. **Confirm Upload (Server/Backend):** The primary backend receives the `staticPath`, extracts the unique filename (`myphoto_1716948271a2d3f.png`), and requests confirmation by calling `GET /api/confirm?filename=[extractedName]` on the file-service. Upon successful confirmation, the file-service moves the file into its final structured permanent path (`/[application]/[doctype]/[filetype]/[uniqueName]`), and the primary backend saves the relative `staticPath` in its database.
3. **Retrieval Construction:** When rendering resources, the primary backend constructs the absolute asset URL on-the-fly by prepending the file-service base URL to the stored `staticPath` (e.g., `http://192.168.1.170:9090/files` + `staticPath`) before returning it to the client.

### 2. Request Routing & Security Config
* **Public Route Group:** `/api/auth/login`, `/api/upload`, `/api/confirm`, `/files/**` (Static File Serving).
* **Guarded Admin Route Group:** `/api/admin/**` (Requires valid JWT passed directly as the `Authorization: <token>` header, **without** the `Bearer ` prefix).

---

## 🔒 Authentication Flow

To access any admin dashboard capabilities (file exploring, server usage statistics, configurations), the frontend must perform authentication.

### **POST** `/api/auth/login`
* **Access Level:** Public
* **Request Header:** `Content-Type: application/json`
* **Request Payload:**
  ```json
  {
    "username": "admin",
    "password": "admin-password"
  }
  ```
* **Response (Success - 200 OK):**
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "admin"
  }
  ```
* **Frontend Implementation Tip:** Store the `token` in securely structured storage (`localStorage` or reactive state). Include the raw token directly in the `Authorization` header for all requests prefixing `/api/admin/` (do **not** prepend `Bearer `):
  ```javascript
  headers: {
    'Authorization': token, // Passed directly
    'Content-Type': 'application/json'
  }
  ```

---

## 📤 File Integration Workflow (Core APIs)

### 1. Upload File (Stage 1)
Uploads the raw multipart file directly to the temporary folder.
* **HTTP Method:** `POST`
* **Endpoint:** `/api/upload`
* **Access Level:** Public
* **Request Type:** `multipart/form-data`
* **Request Parameters:**
  * `file` (Multipart Binary File, Required). Allowed file extensions: `.png`, `.jpeg`, `.jpg`, `.gif`, `.pdf`, `.doc`, `.docx`.
  * `application` (Integer, Required) — Matches the Application numerical ID (e.g. `1` for `lms`).
  * `doctype` (Integer, Required) — Matches the Document Type numerical ID (e.g. `2` for `employeeimage`).
* **Response (Success - 200 OK):**
  ```json
  {
    "staticPath": "/lms/employeeimage/png/myphoto_1716948271a2d3f.png",
    "url": "http://192.168.1.170:9090/files/temp/lms-employeeimage-png-myphoto_1716948271a2d3f.png",
    "statusCode": 200,
    "message": "File uploaded successfully",
    "success": true
  }
  ```
  * *Note:* The file resides temporarily in the static folder `/files/temp/...`. Use the returned `url` to preview or inspect the file before committing. 
  * *Important:* Extract the filename trailing suffix (e.g., `myphoto_1716948271a2d3f.png`) from the URL or path to use during the confirmation step.

### 2. Confirm File (Stage 2)
Commits the temporary file into structured permanent storage. Run this immediately after successfully binding the path to your entity database records.
* **HTTP Method:** `GET`
* **Endpoint:** `/api/confirm`
* **Access Level:** Public
* **Request Query Parameters:**
  * `filename` (String, Required) — The exact unique suffix filename generated in Stage 1 (e.g., `myphoto_1716948271a2d3f.png`).
* **Response (Success - 200 OK):**
  ```json
  {
    "staticPath": "/lms/employeeimage/png/myphoto_1716948271a2d3f.png",
    "url": "http://192.168.1.170:9090/files/lms/employeeimage/png/myphoto_1716948271a2d3f.png",
    "statusCode": 200,
    "message": "File confirmed successfully",
    "success": true
  }
  ```
  * The file is now safely moved to standard application/doctype subdirectories and won't be cleared by the temp folder automatic cleanup tasks.

---

## ⚙️ Administration Mappings (JSON Configs)

These endpoints manage application categories and doctype categories loaded from `/config/mapping.json`.

### 1. Get Application Mappings
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/config/applications`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    { "id": 1, "name": "lms" },
    { "id": 2, "name": "opd" }
  ]
  ```

### 2. Add New Application
* **HTTP Method:** `POST`
* **Endpoint:** `/api/admin/config/applications`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Request Body:**
  ```json
  {
    "name": "new_app"
  }
  ```
* **Response (Success - 200 OK):**
  ```json
  {
    "id": 3,
    "name": "new_app"
  }
  ```

### 3. Get Document Type Mappings
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/config/doctypes`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    { "id": 1, "name": "patientimage" },
    { "id": 2, "name": "employeeimage" }
  ]
  ```

### 4. Add New Document Type
* **HTTP Method:** `POST`
* **Endpoint:** `/api/admin/config/doctypes`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Request Body:**
  ```json
  {
    "name": "new_doctype"
  }
  ```
* **Response (Success - 200 OK):**
  ```json
  {
    "id": 3,
    "name": "new_doctype"
  }
  ```

---

## 📂 File Explorer Endpoints

These endpoints allow the Admin UI to explore files stored hierarchically across Applications, Document Types, and File Extensions.

```
📁 Root
└── 📁 [application]
    └── 📁 [doctype]
        └── 📁 [filetype] (png, pdf, jpeg...)
            └── 📄 [files]
```

### 1. List Applications Folders on Server
Lists all directories containing files in the root upload folder.
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/files/applications`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    {
      "name": "lms",
      "path": "C:\\Shiwang\\uploaded_files\\lms"
    }
  ]
  ```

### 2. List Document Type Folders
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/files/applications/{app}/doctypes`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    {
      "name": "employeeimage",
      "path": "C:\\Shiwang\\uploaded_files\\lms\\employeeimage"
    }
  ]
  ```

### 3. List File Type Folders
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/files/applications/{app}/doctypes/{doctype}/filetypes`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    {
      "name": "png",
      "path": "C:\\Shiwang\\uploaded_files\\lms\\employeeimage\\png"
    }
  ]
  ```

### 4. Fetch Actual Files
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/files/applications/{app}/doctypes/{doctype}/filetypes/{filetype}`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    {
      "name": "myphoto_1716948271a2d3f.png",
      "size": 1048576,
      "lastModified": 1716948271000,
      "staticUrl": "http://192.168.1.170:9090/files/lms/employeeimage/png/myphoto_1716948271a2d3f.png"
    }
  ]
  ```

### 5. View Buffer Temporary Uploads
Enables review of files sitting in the transient `/temp` directory.
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/files/temp`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    {
      "name": "lms-employeeimage-png-draftdoc_1716948222.png",
      "originalName": "draftdoc_1716948222.png",
      "application": "lms",
      "doctype": "employeeimage",
      "fileType": "png",
      "size": 204850,
      "lastModified": 1716948222000,
      "staticUrl": "http://192.168.1.170:9090/files/temp/lms-employeeimage-png-draftdoc_1716948222.png"
    }
  ]
  ```

---

## 📊 Drive Space & System Metrics

Used to drive system dashboards, gauge current disk utilization, and review individual application footprint sizes.

### 1. General Disk Space Metrics
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/disk/stats`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  {
    "totalSpace": 512110190592,
    "freeSpace": 128522301440,
    "usableSpace": 128522301440,
    "usedSpace": 383587889152
  }
  ```
  *(Sizes returned in Bytes)*

### 2. Disk Space Used Grouped by Application
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/disk/applications`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  [
    {
      "applicationName": "lms",
      "totalSize": 104857600,
      "fileCount": 120
    },
    {
      "applicationName": "opd",
      "totalSize": 52428800,
      "fileCount": 42
    }
  ]
  ```

### 3. Temp Directory Disk Usage Metrics
* **HTTP Method:** `GET`
* **Endpoint:** `/api/admin/temp/stats`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  {
    "totalSize": 15482930,
    "fileCount": 8
  }
  ```

---

## 🧹 Buffer Temp Maintenance & Cleanup Policies

These APIs manage manual sweeps and background cron configuration for temporary folder optimization.

### 1. Configure Cleanup Policy
Retrieves or changes automatic cleanup tasks configured in `/config/temp-cleanup-config.json`.
* **HTTP Method:** `GET` / `PUT`
* **Endpoints:** 
  * `GET /api/admin/temp-cleanup/getConfig`
  * `PUT /api/admin/temp-cleanup/updateConfig`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **PUT Request Payload:**
  ```json
  {
    "enabled": true,
    "deleteOlderThanMinutes": 120
  }
  ```
* **Response (Success - 200 OK):**
  ```json
  {
    "enabled": true,
    "deleteOlderThanMinutes": 120
  }
  ```

### 2. Force Clear All Temp Files (Immediate Clean)
* **HTTP Method:** `DELETE`
* **Endpoint:** `/api/admin/temp/clear`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Response (Success - 200 OK):**
  ```json
  {
    "deletedCount": 12,
    "freedSpace": 4519280,
    "success": true,
    "message": "Temporary folder cleared successfully"
  }
  ```

### 3. Force Clear Temp Files Older than $N$ Hours
* **HTTP Method:** `DELETE`
* **Endpoint:** `/api/admin/temp/clear/{hours}`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Example:** `DELETE /api/admin/temp/clear/24`
* **Response (Success - 200 OK):**
  ```json
  {
    "deletedCount": 4,
    "freedSpace": 890120,
    "success": true,
    "message": "Files older than 24 hours cleared successfully"
  }
  ```

### 4. Delete Single Specific Temp File
Allows removing a specific item from the temporary buffer.
* **HTTP Method:** `DELETE`
* **Endpoint:** `/api/admin/temp/files/{fileName}`
* **Access Level:** **JWT Admin Only** (Requires `Authorization: <token>`)
* **Example:** `DELETE /api/admin/temp/files/lms-employeeimage-png-bad_photo.png`
* **Response (Success - 200 OK):**
  ```json
  {
    "deletedCount": 1,
    "freedSpace": 125000,
    "success": true,
    "message": "File deleted successfully"
  }
  ```

---

## 💡 Key Tips for Frontend Implementation

1. **Stateful Base URL Construction**
   * Do not hardcode the API host. In your React/Vue/Angular environment, export a dynamic base API environment variable:
     ```env
     VITE_API_BASE_URL=http://192.168.1.170:9090
     ```
   * Build API clients (e.g. using `axios` or native `fetch`) to append `/api` as necessary.

2. **File Explorer Hierarchy UX**
   * Since explorer APIs are broken down tier-by-tier (`/applications` ➔ `/doctypes` ➔ `/filetypes` ➔ files), implement a nested navigation layout (e.g., Breadcrumbs or a sidebar tree explorer component).
   * Cache fetched options (like applications and doctype dropdown arrays) to minimize network request overhead during dynamic upload configurations.

3. **Disk space visualization**
   * Keep chart elements responsive by parsing drive stats into Megabytes (`MB`) or Gigabytes (`GB`) dynamically:
     ```javascript
     const gigabytes = bytes / (1024 * 1024 * 1024);
     ```
   * Utilize sleek progress rings or donut charts to display "Used" vs "Free" disk space clearly inside your dashboard.
