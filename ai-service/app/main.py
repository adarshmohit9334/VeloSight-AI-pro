import os
import asyncio
import logging
from typing import Dict, Any
from fastapi import FastAPI, BackgroundTasks, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

from app.analyzer import VideoAnalyzer

logging.basicConfig(level=logging.INFO, format="%(asctime)s [%(levelname)s] %(name)s: %(message)s")
logger = logging.getLogger("velosight.api")

app = FastAPI(
    title="VeloSight AI Service",
    description="Computer Vision Traffic Analysis Engine powered by OpenCV and YOLO",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# In-memory store for analysis jobs
analysis_jobs: Dict[str, Dict[str, Any]] = {}

class AnalysisRequest(BaseModel):
    analysisId: str
    videoPath: str
    confidenceThreshold: float = Field(default=0.40, ge=0.1, le=0.9)
    frameSkip: int = Field(default=2, ge=1, le=10)

def run_analysis_task(request: AnalysisRequest):
    analysis_id = request.analysisId
    logger.info(f"Starting async analysis task for ID: {analysis_id}")
    analysis_jobs[analysis_id] = {
        "status": "PROCESSING",
        "progress": 5.0,
        "step": "Initializing computer vision models...",
        "results": None,
        "error": None
    }

    try:
        analyzer = VideoAnalyzer(
            confidence_threshold=request.confidenceThreshold,
            frame_skip=request.frameSkip
        )

        output_dir = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "..", "processed")

        def update_progress(pct: float, step_desc: str):
            analysis_jobs[analysis_id]["progress"] = pct
            analysis_jobs[analysis_id]["step"] = step_desc

        results = analyzer.process_video(
            analysis_id=analysis_id,
            input_video_path=request.videoPath,
            output_video_dir=output_dir,
            progress_callback=update_progress
        )

        analysis_jobs[analysis_id]["status"] = "COMPLETED"
        analysis_jobs[analysis_id]["progress"] = 100.0
        analysis_jobs[analysis_id]["step"] = "Completed"
        analysis_jobs[analysis_id]["results"] = results
        logger.info(f"Analysis task completed successfully for ID: {analysis_id}")

    except Exception as e:
        logger.error(f"Analysis failed for ID {analysis_id}: {str(e)}", exc_info=True)
        analysis_jobs[analysis_id]["status"] = "FAILED"
        analysis_jobs[analysis_id]["error"] = str(e)

@app.get("/")
def read_root():
    return {"name": "VeloSight AI Engine", "status": "ONLINE", "version": "1.0.0"}

@app.get("/ai/health")
def health_check():
    return {
        "status": "ONLINE",
        "engine": "OpenCV + YOLO/Centroid Tracker",
        "supported_codecs": ["MP4", "AVI", "MOV", "MKV"]
    }

@app.post("/ai/analyze")
def start_analysis(request: AnalysisRequest, background_tasks: BackgroundTasks):
    if not os.path.exists(request.videoPath):
        raise HTTPException(status_code=404, detail=f"Video file not found at path: {request.videoPath}")

    analysis_jobs[request.analysisId] = {
        "status": "QUEUED",
        "progress": 0.0,
        "step": "Queued for processing...",
        "results": None,
        "error": None
    }

    background_tasks.add_task(run_analysis_task, request)
    return {
        "message": "Analysis session initialized successfully",
        "analysisId": request.analysisId,
        "status": "QUEUED"
    }

@app.get("/ai/status/{analysis_id}")
def get_status(analysis_id: str):
    if analysis_id not in analysis_jobs:
        raise HTTPException(status_code=404, detail="Analysis ID not found")
    job = analysis_jobs[analysis_id]
    return {
        "analysisId": analysis_id,
        "status": job["status"],
        "progress": job["progress"],
        "step": job["step"],
        "error": job["error"]
    }

@app.get("/ai/results/{analysis_id}")
def get_results(analysis_id: str):
    if analysis_id not in analysis_jobs:
        raise HTTPException(status_code=404, detail="Analysis ID not found")
    job = analysis_jobs[analysis_id]
    if job["status"] != "COMPLETED":
        raise HTTPException(status_code=400, detail=f"Analysis is not completed yet. Current status: {job['status']}")
    return job["results"]
