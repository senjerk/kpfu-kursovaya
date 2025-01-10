package com.example.vladbistrov;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;

public class FourthFragment extends Fragment {
    private ParabolaGraphView graphView;
    private TextInputEditText editTextK, editTextA, editTextB, editTextC;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        graphView = view.findViewById(R.id.parabolaGraphView);
        editTextK = view.findViewById(R.id.editTextK);
        editTextA = view.findViewById(R.id.editTextA);
        editTextB = view.findViewById(R.id.editTextB);
        editTextC = view.findViewById(R.id.editTextC);

        // Установка значений по умолчанию
        editTextK.setText("1");
        editTextA.setText("1");
        editTextB.setText("0");
        editTextC.setText("0");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateGraph();
            }
        };

        editTextK.addTextChangedListener(textWatcher);
        editTextA.addTextChangedListener(textWatcher);
        editTextB.addTextChangedListener(textWatcher);
        editTextC.addTextChangedListener(textWatcher);

        updateGraph();
        return view;
    }

    private void updateGraph() {
        try {
            String kStr = editTextK.getText().toString();
            String aStr = editTextA.getText().toString();
            String bStr = editTextB.getText().toString();
            String cStr = editTextC.getText().toString();

            if (kStr.isEmpty() || aStr.isEmpty() || bStr.isEmpty() || cStr.isEmpty()) {
                return;
            }

            float k = Float.parseFloat(kStr);
            float a = Float.parseFloat(aStr);
            float b = Float.parseFloat(bStr);
            float c = Float.parseFloat(cStr);

            // Нужно будет обновить ParabolaGraphView для поддержки новой формулы
            graphView.setCoefficients(k, a, b, c);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Введите корректные значения", Toast.LENGTH_SHORT).show();
        }
    }
} 