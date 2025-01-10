package com.example.vladbistrov;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {
    private List<Double> xValues = new ArrayList<>();
    private List<Double> zValues = new ArrayList<>();
    private TableLayout tableLayout;
    private TextInputEditText editTextX;
    private TextInputEditText editTextZ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        tableLayout = view.findViewById(R.id.tableLayout);
        editTextX = view.findViewById(R.id.editTextX);
        editTextZ = view.findViewById(R.id.editTextZ);
        Button buttonAddX = view.findViewById(R.id.buttonAddX);
        Button buttonAddZ = view.findViewById(R.id.buttonAddZ);

        buttonAddX.setOnClickListener(v -> addXValue());
        buttonAddZ.setOnClickListener(v -> addZValue());

        return view;
    }

    private void addXValue() {
        try {
            double value = Double.parseDouble(editTextX.getText().toString());
            if (!xValues.contains(value)) {
                xValues.add(value);
                updateTable();
                editTextX.setText("");
            }
        } catch (NumberFormatException e) {
            editTextX.setError("Введите корректное число");
        }
    }

    private void addZValue() {
        try {
            double value = Double.parseDouble(editTextZ.getText().toString());
            if (!zValues.contains(value)) {
                zValues.add(value);
                updateTable();
                editTextZ.setText("");
            }
        } catch (NumberFormatException e) {
            editTextZ.setError("Введите корректное число");
        }
    }

    private void updateTable() {
        tableLayout.removeAllViews();

        // Создаем заголовок таблицы
        TableRow headerRow = new TableRow(getContext());
        headerRow.addView(createHeaderCell("X/Z"));

        // Добавляем заголовки Z, если они есть
        if (!zValues.isEmpty()) {
            for (Double z : zValues) {
                headerRow.addView(createHeaderCell(String.valueOf(z)));
            }
        }
        tableLayout.addView(headerRow);

        // Создаем строки таблицы, если есть значения X
        if (!xValues.isEmpty()) {
            for (Double x : xValues) {
                TableRow row = new TableRow(getContext());
                row.addView(createHeaderCell(String.valueOf(x)));
                
                // Добавляем ячейки со значениями, если есть Z
                if (!zValues.isEmpty()) {
                    for (Double z : zValues) {
                        row.addView(createCell(calculateFunction(x, z)));
                    }
                }
                tableLayout.addView(row);
            }
        }
    }

    private TextView createHeaderCell(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setBackgroundResource(android.R.color.darker_gray);
        textView.setTextColor(Color.WHITE);
        textView.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        ));
        // Минимальная ширина для выравнивания
        textView.setMinWidth(100);
        // Выравнивание текста по центру
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }

    private TextView createCell(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setBackgroundResource(android.R.color.white);
        textView.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        ));
        // Минимальная ширина для выравнивания
        textView.setMinWidth(100);
        // Выравнивание текста по центру
        textView.setGravity(android.view.Gravity.CENTER);
        return textView;
    }

    private String calculateFunction(double x, double z) {
        // Здесь реализуем вычисление функции e^(√(x+z))(√(|/z+x))
        try {
            double sqrtXPlusZ = Math.sqrt(x + z);
            double sqrtAbsZPlusX = Math.sqrt(Math.abs(z + x));
            double result = Math.exp(sqrtXPlusZ) * sqrtAbsZPlusX;
            return String.format("%.4f", result);
        } catch (Exception e) {
            return "Error";
        }
    }
} 