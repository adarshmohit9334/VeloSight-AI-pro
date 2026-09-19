# VeloSight AI — Intelligent Traffic Management & Analytics Platform

**Tagline:** Intelligent Traffic. Smarter Decisions.  
**Category:** Full-Stack AI Microservices Architecture (MCA Production-Grade Project)

---

## 1. Project Overview

**VeloSight AI** is a complete, production-grade intelligent traffic monitoring and decision-support analytics platform. The application ingests traffic-camera video feeds, executes real-time vehicle detection and centroid object tracking using Computer Vision (YOLOv8 & OpenCV), computes traffic density and congestion scores, and generates decision-support signal timing recommendations for smart city control centers.

---

## 2. Technology Stack

### Frontend
- **Framework:** React.js 18 (Vite)
- **Routing:** React Router v6
- **Styling:** Custom Vanilla CSS Dark Smart-City Control Center Design System
- **Visualizations:** Recharts (Area, Bar, Pie charts)
- **Icons:** Lucide React
- **HTTP Client:** Axios with JWT Bearer Token interceptor

### Backend
- **Framework:** Java 17+, Spring Boot 3.2.3
- **Security:** Spring Security + Stateless JWT Token Authentication + BCrypt Password Encoding
- **Persistence:** Spring Data JPA
- **Database:** PostgreSQL (with embedded H2 database fallback for instant local execution)
- **Validation:** Bean Validation (`jakarta.validation`)

### AI Computer Vision Service
- **Framework:** Python 3.14 + FastAPI + Uvicorn
- **Computer Vision Engine:** OpenCV + Ultralytics YOLOv8 / Centroid Tracking
- **Analytics Engines:** Custom Density Engine, Congestion Engine, Signal Recommendation Engine
- **Resilience:** Automatic fallback to OpenCV MOG2 background subtractor if YOLO model downloads are unavailable in offline environments.

---

## 3. System Architecture & Pipeline Flow

```
Traffic Camera Video Feed / File Upload (MP4, AVI, MOV, MKV)
                      ↓
          React Vite Control Center (Port 5173)
                      ↓ REST API / Multipart
         Spring Boot Backend API (Port 8080)
          ├── Spring Security JWT Auth
          ├── PostgreSQL / H2 Persistence
          └── Async AI Pipeline Handler
                      ↓ HTTP JSON Request
         Python FastAPI AI Service (Port 8000)
          ├── Video Frame Decoder (OpenCV)
          ├── YOLO Vehicle Detector (Car, Bike, Bus, Truck)
          ├── Centroid Tracker & Virtual Line Counter
          ├── Traffic Density & Congestion Engines
          └── Annotated Video HUD Exporter
                      ↓ Structured Results
         Spring Boot Data Persistence & Analytics
                      ↓ JSON Response
          React Command Center Dashboard & Charts
```

---

## 4. Key Monorepo Directory Structure

```
velosight-ai/
├── frontend/                 # React + Vite UI (Dashboard, Video Analysis, Analytics, Alerts, Settings)
│   ├── src/
│   │   ├── components/       # StatCard, TrafficStatusBadge, AlertCard, VideoUploader, etc.
│   │   ├── context/          # AuthContext with Demo Mode toggle
│   │   ├── pages/            # Dashboard, VideoAnalysis, Analytics, Intersections, Cameras, etc.
│   │   ├── services/         # Axios API Client
│   │   ├── App.jsx           # Protected Routes & Layout
│   │   └── main.jsx          # Entrypoint
│   └── package.json
│
├── backend/                  # Spring Boot REST API & JWT Security
│   ├── src/main/java/com/velosight/
│   │   ├── config/           # Security & DataInitializer seed runner
│   │   ├── controller/       # Auth, Dashboard, Analysis, Analytics, Alerts, Recommendations
│   │   ├── dto/              # Request/Response DTOs
│   │   ├── entity/           # JPA Entities (User, Intersection, Camera, AnalysisSession, Alert)
│   │   ├── repository/       # Spring Data JPA Repositories
│   │   ├── security/         # JWT Token Provider & Filter
│   │   └── service/          # Business Logic & AI Service REST Integration
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── ai-service/               # Python Computer Vision Service
│   ├── app/
│   │   ├── main.py           # FastAPI Web Application & Async Task Runner
│   │   ├── detector.py       # YOLOv8 Object Detector + OpenCV MOG2 Fallback
│   │   ├── tracker.py        # Centroid Vehicle Tracker
│   │   ├── analyzer.py       # Video Processing Pipeline & HUD Overlay Writer
│   │   ├── density.py        # Traffic Density Calculator
│   │   ├── congestion.py     # Congestion & Speed Decay Engine
│   │   ├── signal_recommendation.py # Green Phase Optimization Engine
│   │   └── utils/            # Sample synthetic video generator
│   └── requirements.txt
│
├── uploads/                  # Input traffic video recordings
├── processed/                # Annotated output videos with vehicle bounding boxes
├── docker-compose.yml        # Docker Multi-container Composition
├── README.md                 # Project Documentation
└── .env.example              # Environment variables template
```

---

## 5. Local Setup & Execution Guide

### Prerequisites
- Java 17 or Java 21+
- Apache Maven 3.8+
- Node.js v18+ & npm
- Python 3.10+

---

### Step 1: Python AI Service Setup

```bash
cd ai-service

# Create virtual environment
python3 -m venv venv
source venv/bin/activate    # On Windows: venv\Scripts\activate

# Install dependencies
pip install -r requirements.txt

# Run FastAPI server on port 8000
uvicorn app.main:app --reload --port 8000
```
- **API Docs:** `http://localhost:8000/docs`
- **Health Check:** `http://localhost:8000/ai/health`

---

### Step 2: Spring Boot Backend Setup

```bash
cd backend

# Compile & Run Spring Boot Application
mvn spring-boot:run
```
- **Backend API:** `http://localhost:8080/api`
- **H2 Console:** `http://localhost:8080/h2-console`
- On initial startup, `DataInitializer.java` automatically seeds sample users, intersections, cameras, time-series metrics, alerts, and recommendations!

---

### Step 3: React Frontend Setup

```bash
cd frontend

# Install node dependencies
npm install

# Start Vite development server
npm run dev
```
- **Control Center Dashboard UI:** `http://localhost:5173`

---

## 6. Demo Credentials & Presentation Guide

### Pre-configured Login Accounts
| Role | Email | Password |
|---|---|---|
| **System Administrator** | `admin@velosight.ai` | `Admin@123` |
| **Traffic Analyst** | `analyst@velosight.ai` | `Analyst@123` |
| **Control Room Operator** | `operator@velosight.ai` | `Operator@123` |

### Presentation Demonstration Steps (MCA Viva Flow)
1. **Login:** Access `http://localhost:5173/login` using `admin@velosight.ai`.
2. **Dashboard Overview:** Review top KPI cards (Total Vehicles, Density Level, Congestion, Active Alerts), hourly traffic volume area chart, and vehicle class pie chart.
3. **Live Monitoring:** Open `/live-monitoring` to inspect real-time stream status and camera feeds.
4. **Video Upload & AI Processing:** Navigate to `/video-analysis`. Upload a traffic video clip or enable Demo Mode. Click **Start AI Video Analysis**.
5. **Real-time Pipeline Tracker:** Observe the processing timeline steps (`Uploading` → `Initializing YOLO` → `Tracking & Counting` → `Generating HUD Overlay`).
6. **Results & Vehicle Breakdown:** Inspect classified vehicle counts (Cars, Motorcycles, Buses, Trucks), average speed estimation, density level, and signal split recommendation.
7. **Signal AI Optimization:** View `/signal-recommendations` to explain the North-South vs East-West green light phase split rationale.
8. **Alerts & Incidents:** Open `/alerts` to acknowledge or resolve active congestion warnings.
9. **Analytics & Exports:** Visit `/analytics` and export CSV data.
10. **Executive Reports:** Navigate to `/reports` and generate/print formal traffic PDF reports.

---

## 7. REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/login` | User login & JWT token generation |
| `POST` | `/api/auth/register` | Register new analyst/operator |
| `GET` | `/api/dashboard/summary` | Fetch command center KPIs & trends |
| `POST` | `/api/analysis/upload` | Upload video & trigger AI analysis pipeline |
| `GET` | `/api/analysis/{id}` | Get analysis session metrics |
| `GET` | `/api/analysis/{id}/processed-video` | Stream annotated output video |
| `GET` | `/api/analytics/traffic` | Fetch hourly traffic analytics |
| `GET` | `/api/intersections` | Intersections CRUD operations |
| `GET` | `/api/cameras` | Cameras CRUD operations |
| `GET` | `/api/alerts` | List system traffic alerts |
| `PUT` | `/api/alerts/{id}/acknowledge` | Acknowledge active alert |
| `PUT` | `/api/alerts/{id}/resolve` | Resolve active alert |
| `GET` | `/api/recommendations` | Get signal timing recommendations |
| `GET` | `/api/reports/traffic` | Generate traffic report |

---

## 8. Docker Multi-Container Deployment

To spin up the complete microservices stack with PostgreSQL, Backend, AI Service, and Frontend using Docker:

```bash
docker-compose up --build
```

---

## 9. License & Project Rights

Developed for MCA Computer Science & Artificial Intelligence Viva Presentation. All rights reserved.
