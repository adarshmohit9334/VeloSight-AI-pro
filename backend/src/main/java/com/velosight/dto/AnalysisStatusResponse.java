package com.velosight.dto;

public class AnalysisStatusResponse {
    private String analysisId;
    private String status;
    private Double progress;
    private String step;
    private String error;
    private AnalysisResultDto result;

    public AnalysisStatusResponse() {}

    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getProgress() { return progress; }
    public void setProgress(Double progress) { this.progress = progress; }

    public String getStep() { return step; }
    public void setStep(String step) { this.step = step; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public AnalysisResultDto getResult() { return result; }
    public void setResult(AnalysisResultDto result) { this.result = result; }
}
