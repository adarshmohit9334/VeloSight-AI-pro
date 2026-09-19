package com.velosight.dto;

public class AnalysisUploadResponse {
    private String analysisId;
    private String filename;
    private String status;
    private String message;

    public AnalysisUploadResponse() {}

    public AnalysisUploadResponse(String analysisId, String filename, String status, String message) {
        this.analysisId = analysisId;
        this.filename = filename;
        this.status = status;
        this.message = message;
    }

    public String getAnalysisId() { return analysisId; }
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static AnalysisUploadResponseBuilder builder() {
        return new AnalysisUploadResponseBuilder();
    }

    public static class AnalysisUploadResponseBuilder {
        private String analysisId;
        private String filename;
        private String status;
        private String message;

        public AnalysisUploadResponseBuilder analysisId(String analysisId) { this.analysisId = analysisId; return this; }
        public AnalysisUploadResponseBuilder filename(String filename) { this.filename = filename; return this; }
        public AnalysisUploadResponseBuilder status(String status) { this.status = status; return this; }
        public AnalysisUploadResponseBuilder message(String message) { this.message = message; return this; }

        public AnalysisUploadResponse build() {
            return new AnalysisUploadResponse(analysisId, filename, status, message);
        }
    }
}
