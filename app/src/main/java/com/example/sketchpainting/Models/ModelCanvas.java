package com.example.sketchpainting.Models;

import android.graphics.Paint;
import android.graphics.Path;

public class ModelCanvas {
    public Path getPath() {
        return path;
    }

    public ModelCanvas() {
    }

    public ModelCanvas(Path path, Paint paint) {
        this.path = path;
        this.paint = paint;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    private Path path;
    private Paint paint;
}
