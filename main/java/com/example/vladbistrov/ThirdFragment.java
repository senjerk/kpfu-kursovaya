package com.example.vladbistrov;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ThirdFragment extends Fragment {
    private TableLayout tableLayout;
    private EditText editText1, editText2, editText3, editText4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        editText4 = view.findViewById(R.id.editText4);
        Button addButton = view.findViewById(R.id.addButton);
        tableLayout = view.findViewById(R.id.tableLayout);

        addButton.setOnClickListener(v -> {
            try {
                double xStart = Double.parseDouble(editText1.getText().toString());
                double xEnd = Double.parseDouble(editText2.getText().toString());
                double dx = Double.parseDouble(editText3.getText().toString());
                double epsilon = Double.parseDouble(editText4.getText().toString());

                calculateAndDisplayResults(xStart, xEnd, dx, epsilon);
                
                // Clear input fields
                editText1.setText("");
                editText2.setText("");
                editText3.setText("");
                editText4.setText("");
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void calculateAndDisplayResults(double xStart, double xEnd, double dx, double epsilon) {
        for (double x = xStart; x <= xEnd; x += dx) {
            double result = 0.0;
            int terms = 0;
            double term;
            
            do {
                term = calculateTerm(x, terms);
                result += term;
                terms++;
            } while (Math.abs(term) > epsilon);

            addRowToTable(new String[]{
                String.format("%.4f", x),
                String.format("%.10f", result),
                String.valueOf(terms)
            });
        }
    }

    private double calculateTerm(double x, int n) {
        return 2 * (1.0 / ((2 * n + 1) * Math.pow(x, 2 * n + 1)));
    }

    private void addRowToTable(String[] values) {
        TableRow row = new TableRow(getContext());
        row.setPadding(8, 8, 8, 8);

        for (String value : values) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f
            ));
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setText(value);
            row.addView(textView);
        }

        tableLayout.addView(row);
    }
} 