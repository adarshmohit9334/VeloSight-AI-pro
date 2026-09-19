import React, { useRef, useState } from 'react';
import { Upload, FileVideo, CheckCircle, AlertCircle } from 'lucide-react';

export const VideoUploader = ({ onFileSelected, isUploading }) => {
  const fileInputRef = useRef(null);
  const [selectedFile, setSelectedFile] = useState(null);
  const [dragActive, setDragActive] = useState(false);

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setSelectedFile(file);
      if (onFileSelected) onFileSelected(file);
    }
  };

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      const file = e.dataTransfer.files[0];
      setSelectedFile(file);
      if (onFileSelected) onFileSelected(file);
    }
  };

  return (
    <div
      onDragEnter={handleDrag}
      onDragOver={handleDrag}
      onDragLeave={handleDrag}
      onDrop={handleDrop}
      onClick={() => fileInputRef.current?.click()}
      style={{
        border: `2px dashed ${dragActive ? 'var(--primary-blue)' : 'var(--border-color)'}`,
        borderRadius: '12px',
        padding: '36px 20px',
        textAlign: 'center',
        backgroundColor: dragActive ? 'var(--primary-glow)' : 'var(--bg-sidebar)',
        cursor: isUploading ? 'not-allowed' : 'pointer',
        transition: 'all 0.2s ease'
      }}
    >
      <input
        ref={fileInputRef}
        type="file"
        accept=".mp4,.avi,.mov,.mkv"
        style={{ display: 'none' }}
        onChange={handleFileChange}
        disabled={isUploading}
      />

      <div style={{ width: '56px', height: '56px', borderRadius: '50%', background: 'var(--primary-glow)', color: 'var(--primary-blue)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 16px auto' }}>
        <Upload size={28} />
      </div>

      {selectedFile ? (
        <div>
          <div style={{ fontSize: '0.95rem', fontWeight: 600, color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '8px' }}>
            <FileVideo size={18} color="#38bdf8" />
            {selectedFile.name}
          </div>
          <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', marginTop: '4px' }}>
            {(selectedFile.size / (1024 * 1024)).toFixed(2)} MB • Ready to analyze
          </div>
        </div>
      ) : (
        <div>
          <h3 style={{ fontSize: '1rem', fontWeight: 600, color: '#fff' }}>
            Upload Traffic Video Stream
          </h3>
          <p style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '4px' }}>
            Drag and drop or browse video files (MP4, AVI, MOV, MKV) up to 500MB
          </p>
        </div>
      )}
    </div>
  );
};
