package com.example.vladbistrov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ParabolaGraphView extends View {
    private Paint axisPaint;
    private Paint functionPaint;
    private Paint textPaint;
    private Paint gridPaint;
    private float scale = 50; // Масштаб для отображения
    
    // Коэффициенты параболы y = a(x-b)² + c
    private float k = 1; // числитель
    private float a = 1; // коэффициент при x
    private float b = 0; // свободный член
    private float c = 0; // смещение по y
    
    public ParabolaGraphView(Context context) {
        super(context);
        init();
    }

    public ParabolaGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(2);
        axisPaint.setStyle(Paint.Style.STROKE);

        functionPaint = new Paint();
        functionPaint.setColor(Color.BLUE);
        functionPaint.setStrokeWidth(3);
        functionPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.CENTER);

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1);
    }

    public void setCoefficients(float k, float a, float b, float c) {
        this.k = k;
        this.a = a;
        this.b = b;
        this.c = c;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Рисуем сетку со смещением
        drawGrid(canvas, centerX, centerY);
        
        // Рисуем оси со смещением
        drawAxes(canvas, centerX, centerY);
        
        // Рисуем параболу без смещения centerY
        drawParabola(canvas, centerX, centerY);
    }

    private void drawGrid(Canvas canvas, int centerX, int centerY) {
        // Смещаем сетку по Y в противоположном направлении
        int shiftedCenterY = centerY + (int)(c * scale);
        
        // Вычисляем видимый диапазон для сетки
        float visibleStartX = (0 - centerX) / scale + b;  // Начало видимой области с учетом смещения
        float visibleEndX = (getWidth() - centerX) / scale + b;  // Конец видимой области с учетом смещения
        
        // Находим ближайшие целые значения для сетки
        int startGridX = (int)Math.floor(visibleStartX);
        int endGridX = (int)Math.ceil(visibleEndX);
        
        // Вертикальные линии
        for (int i = startGridX; i <= endGridX; i++) {
            float x = centerX + (i - b) * scale;  // Вычитаем b для правильного позиционирования
            canvas.drawLine(x, 0, x, getHeight(), gridPaint);
        }
        
        // Вычисляем видимый диапазон по Y
        float visibleStartY = (0 - centerY) / scale;
        float visibleEndY = (getHeight() - centerY) / scale;

        // Находим ближайшие целые значения для сетки
        int startGridY = (int)Math.floor(visibleStartY);
        int endGridY = (int)Math.ceil(visibleEndY);
        
        // Горизонтальные линии
        for (int i = startGridY; i <= endGridY; i++) {
            float y = centerY + i * scale;
            canvas.drawLine(0, y, getWidth(), y, gridPaint);
        }
    }

    private void drawAxes(Canvas canvas, int centerX, int centerY) {
        // Смещаем оси по Y в противоположном направлении
        int shiftedCenterY = centerY + (int)(c * scale);
        float yAxisX = centerX + b * scale;
        
        // Проверяем, видны ли оси
        boolean isXAxisVisible = shiftedCenterY >= 0 && shiftedCenterY <= getHeight();
        boolean isYAxisVisible = yAxisX >= 0 && yAxisX <= getWidth();
        
        // Рисуем основные оси, если они видны
        if (isXAxisVisible) {
            canvas.drawLine(0, shiftedCenterY, getWidth(), shiftedCenterY, axisPaint);
            canvas.drawLine(getWidth() - 20, shiftedCenterY - 10, getWidth(), shiftedCenterY, axisPaint);
            canvas.drawLine(getWidth() - 20, shiftedCenterY + 10, getWidth(), shiftedCenterY, axisPaint);
        }
        
        if (isYAxisVisible) {
            canvas.drawLine(yAxisX, 0, yAxisX, getHeight(), axisPaint);
            canvas.drawLine(yAxisX - 10, 20, yAxisX, 0, axisPaint);
            canvas.drawLine(yAxisX + 10, 20, yAxisX, 0, axisPaint);
        }
        
        // Подписи осей
        canvas.drawText("X", getWidth() - 30, shiftedCenterY - 20, textPaint);
        canvas.drawText("Y", yAxisX + 20, 30, textPaint);
        
        // Значения по X
        float leftValue = -10 - b;
        float rightValue = 10 - b;
        
        for (float i = Math.round(leftValue); i <= rightValue; i += 1) {
            float xPos = centerX + (i + b) * scale;
            if (xPos >= 0 && xPos <= getWidth()) {
                canvas.drawText(String.format("%.0f", i), xPos, shiftedCenterY + 25, textPaint);
            }
        }
        
        // Значения по Y со смещением
        float visibleStartY = (0 - centerY) / scale;
        float visibleEndY = (getHeight() - centerY) / scale;

        for (float i = Math.round(visibleStartY); i <= visibleEndY; i += 1) {
            float screenY = centerY + i * scale;
            if (screenY >= 0 && screenY <= getHeight()) {
                float value = -i + c;  // Добавляем смещение c к значению
                canvas.drawText(String.format("%.0f", value), yAxisX - 25, screenY + 7, textPaint);
            }
        }
    }

    private void drawParabola(Canvas canvas, int centerX, int centerY) {
        float leftX = 0;
        float rightX = getWidth();
        
        float startX = (leftX - centerX) / scale;
        float endX = (rightX - centerX) / scale;
        
        float step = (endX - startX) / 400;
        
        boolean isFirstPoint = true;
        float prevScreenX = 0;
        float prevScreenY = 0;

        for (float x = startX; x <= endX; x += step) {
            // Пропускаем точку разрыва
            if (Math.abs(a * x + b) < 0.1) {
                isFirstPoint = true;
                continue;
            }

            float y = calculateY(x);
            
            // Пропускаем слишком большие значения
            if (Math.abs(y) > 100) {
                isFirstPoint = true;
                continue;
            }

            float screenX = centerX + x * scale;
            float screenY = centerY - y * scale;

            if (!isFirstPoint) {
                if (Math.abs(screenY - prevScreenY) < getHeight()) {
                    canvas.drawLine(prevScreenX, prevScreenY, screenX, screenY, functionPaint);
                }
            }

            prevScreenX = screenX;
            prevScreenY = screenY;
            isFirstPoint = false;
        }
    }

    private float calculateY(float x) {
        // Формула гиперболы: y = k/(ax + b) + c
        float denominator = a * x + b;
        if (Math.abs(denominator) < 0.1) {
            return Float.NaN;
        }
        return k / denominator + c;
    }
} 