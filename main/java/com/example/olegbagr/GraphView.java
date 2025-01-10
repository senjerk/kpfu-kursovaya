package com.example.vladbistrov;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View {
    private Paint axisPaint;
    private Paint graphPaint;
    private Paint gridPaint;
    private Paint textPaint;
    private Path graphPath;
    private float scale = 40; // Устанавливаем масштаб 40

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Настройка кисти для осей
        axisPaint = new Paint();
        axisPaint.setColor(Color.BLACK);
        axisPaint.setStrokeWidth(2);
        axisPaint.setStyle(Paint.Style.STROKE);

        // Настройка кисти для графика
        graphPaint = new Paint();
        graphPaint.setColor(Color.BLUE);
        graphPaint.setStrokeWidth(3);
        graphPaint.setStyle(Paint.Style.STROKE);

        // Настройка кисти для сетки
        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);
        gridPaint.setStrokeWidth(1);
        gridPaint.setStyle(Paint.Style.STROKE);

        // Настройка кисти для текста
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);
        textPaint.setTextAlign(Paint.Align.CENTER);

        graphPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();
        float centerX = width / 2;
        float centerY = height / 2;

        // Выравниваем центр с сеткой
        centerX = Math.round(centerX / scale) * scale;
        centerY = Math.round(centerY / scale) * scale;

        // Рисуем сетку
        // Вертикальные линии
        for (float i = centerX; i < width; i += scale) {
            canvas.drawLine(i, 0, i, height, gridPaint);
        }
        for (float i = centerX; i > 0; i -= scale) {
            canvas.drawLine(i, 0, i, height, gridPaint);
        }
        // Горизонтальные линии
        for (float i = centerY; i < height; i += scale) {
            canvas.drawLine(0, i, width, i, gridPaint);
        }
        for (float i = centerY; i > 0; i -= scale) {
            canvas.drawLine(0, i, width, i, gridPaint);
        }

        // Рисуем оси координат
        canvas.drawLine(0, centerY, width, centerY, axisPaint); // Ось X
        canvas.drawLine(centerX, 0, centerX, height, axisPaint); // Ось Y

        // Подписываем оси
        canvas.drawText("X", width - 30, centerY - 10, textPaint);
        canvas.drawText("Y", centerX + 20, 30, textPaint);

        // Рисуем числа на осях
        for (int i = -10; i <= 10; i++) {
            if (i != 0) {
                float xPos = centerX + i * scale;
                canvas.drawText(String.valueOf(i), xPos, centerY + 30, textPaint);
            }
        }

        for (int i = -10; i <= 10; i++) {
            if (i != 0) {
                float yPos = centerY - i * scale;
                canvas.drawText(String.valueOf(i), centerX - 30, yPos + 10, textPaint);
            }
        }

        // Рисуем график
        graphPath.reset();
        
        // Линия от (-3, 0) до (-6, -3)
        graphPath.moveTo(centerX - 3 * scale, centerY); // Начинаем в точке (-3, 0)
        graphPath.lineTo(centerX - 6 * scale, centerY + 3 * scale); // Идем к точке (-6, -3)

        // Четверть круга от (-6, -3) до (-9, 0)
        RectF ovalSecond = new RectF(
            centerX - 9 * scale, // left: x = -9
            centerY - 3 * scale, // top: y = 3
            centerX - 3 * scale, // right: x = -3
            centerY + 3 * scale  // bottom: y = -3
        );
        graphPath.arcTo(ovalSecond, 90, 90, true); // Начинаем снизу (270) и идем влево на 90 градусов
        // Линия от (3, 0) до (0, 3)
        graphPath.moveTo(centerX + 3 * scale, centerY); // Начинаем в точке (3, 0)
        graphPath.lineTo(centerX, centerY - 3 * scale); // Идем к точке (0, 3)

        // Четверть круга от (0, 3) до (-3, 0)
        RectF ovalFirst = new RectF(
            centerX - 3 * scale, // left: x = -3
            centerY - 3 * scale, // top: y = 3
            centerX + 3 * scale, // right: x = 3
            centerY + 3 * scale  // bottom: y = -3
        );
        graphPath.arcTo(ovalFirst, 180, 90, true); // Добавляем true для forceMoveTo

        // Линия от (3, 0) до (9, 3)
        graphPath.moveTo(centerX + 3 * scale, centerY); // Начинаем в точке (3, 0)
        graphPath.lineTo(centerX + 9 * scale, centerY - 3 * scale); // Заканчиваем в точке (9, 3)
        

        // Создаем пунктирный эффект
        Paint dashPaint = new Paint();
        dashPaint.setColor(Color.DKGRAY);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(2);
        dashPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        dashPaint.setAlpha(255);

        // Рисуем пунктирные линии
        // Вертикальная линия x = -6
        canvas.drawLine(
            centerX - 6 * scale, centerY,  // от (-6, 0)
            centerX - 6 * scale, centerY + 3 * scale,  // до (-6, -3)
            dashPaint
        );

        // Горизонтальная линия y = -3
        canvas.drawLine(
            centerX - 6 * scale, centerY + 3 * scale,  // от (-6, -3)
            centerX, centerY + 3 * scale,  // до (0, -3)
            dashPaint
        );

        // Вертикальная линия x = 0
        canvas.drawLine(
            centerX, centerY + 3 * scale,  // от (0, -3)
            centerX, centerY,  // до (0, 0)
            dashPaint
        );

        // Новые пунктирные линии
        // Вертикальная линия x = 9
        canvas.drawLine(
            centerX + 9 * scale, centerY,  // от (9, 0)
            centerX + 9 * scale, centerY - 3 * scale,  // до (9, 3)
            dashPaint
        );

        // Горизонтальная линия y = 3
        canvas.drawLine(
            centerX, centerY - 3 * scale,  // от (0, 3)
            centerX + 9 * scale, centerY - 3 * scale,  // до (9, 3)
            dashPaint
        );

        // Рисуем стрелку радиуса для первой четверти круга
        float startArrowX = centerX;  // начало в (0,0)
        float startArrowY = centerY;
        float endArrowX = centerX - (3 * scale * (float)Math.cos(Math.PI/4));  // меняем + на - для движения влево
        float endArrowY = centerY - (3 * scale * (float)Math.sin(Math.PI/4));  // оставляем - для движения вверх

        // Рисуем линию стрелки
        canvas.drawLine(startArrowX, startArrowY, endArrowX, endArrowY, axisPaint);

        // Рисуем наконечник стрелки
        float arrowHeadSize = 20;
        float angle = (float) Math.toRadians(225); // меняем угол на 225 градусов для направления влево
        
        float arrowHead1X = endArrowX - arrowHeadSize * (float)(Math.cos(angle - Math.PI/6));
        float arrowHead1Y = endArrowY - arrowHeadSize * (float)(Math.sin(angle - Math.PI/6));
        float arrowHead2X = endArrowX - arrowHeadSize * (float)(Math.cos(angle + Math.PI/6));
        float arrowHead2Y = endArrowY - arrowHeadSize * (float)(Math.sin(angle + Math.PI/6));

        canvas.drawLine(endArrowX, endArrowY, arrowHead1X, arrowHead1Y, axisPaint);
        canvas.drawLine(endArrowX, endArrowY, arrowHead2X, arrowHead2Y, axisPaint);

        // Добавляем надпись R
        textPaint.setTextSize(30);
        float textX = centerX - (1.5f * scale * (float)Math.cos(Math.PI/4)); // меняем + на - для текста слева
        float textY = centerY - (1.5f * scale * (float)Math.sin(Math.PI/4)) - 10;
        canvas.drawText("R", textX, textY, textPaint);
        textPaint.setTextSize(25);

        // Рисуем вторую стрелку радиуса для второй четверти круга
        float startArrow2X = centerX - 6 * scale;  // начало в (-6, 0)
        float startArrow2Y = centerY;              // y = 0
        float endArrow2X = centerX - (6 + 3 * (float)Math.cos(Math.PI/4)) * scale;  // конец на дуге
        float endArrow2Y = centerY + (3 * (float)Math.sin(Math.PI/4)) * scale;      // под углом 45 градусов вниз

        // Рисуем линию второй стрелки
        canvas.drawLine(startArrow2X, startArrow2Y, endArrow2X, endArrow2Y, axisPaint);

        // Рисуем наконечник второй стрелки
        float angle2 = (float) Math.toRadians(135); // меняем угол на 135 градусов для направления вдоль линии
        
        float arrowHead3X = endArrow2X - arrowHeadSize * (float)(Math.cos(angle2 - Math.PI/6));
        float arrowHead3Y = endArrow2Y - arrowHeadSize * (float)(Math.sin(angle2 - Math.PI/6));
        float arrowHead4X = endArrow2X - arrowHeadSize * (float)(Math.cos(angle2 + Math.PI/6));
        float arrowHead4Y = endArrow2Y - arrowHeadSize * (float)(Math.sin(angle2 + Math.PI/6));

        canvas.drawLine(endArrow2X, endArrow2Y, arrowHead3X, arrowHead3Y, axisPaint);
        canvas.drawLine(endArrow2X, endArrow2Y, arrowHead4X, arrowHead4Y, axisPaint);

        // Добавляем вторую надпись R
        textPaint.setTextSize(30);
        float text2X = centerX - (7.0f * scale); // еще правее (было 7.2f)
        float text2Y = centerY + (2.1f * scale); // чуть выше (было 2.5f)
        canvas.drawText("R", text2X, text2Y, textPaint);
        textPaint.setTextSize(25);

        canvas.drawPath(graphPath, graphPaint);
    }
}
