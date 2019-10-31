package com.rsv.samfun;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class View_class extends Fragment {

    Spinner spinner2;
    TextView M1,M2,M3,M4,T1,T2,T3,W1,W2,W3,W4,Th1,Th2,Th3,F1,F2,F3,F4;
    public View_class() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_viewclass, container, false);
        spinner2 = rootView.findViewById(R.id.CLASSspin2);
        M1 = rootView.findViewById(R.id.CLASStextView9);
        M2 = rootView.findViewById(R.id.CLASStextView15);
        M3 = rootView.findViewById(R.id.CLASStextView16);
        M4 = rootView.findViewById(R.id.CLASStextView20);
        T1 = rootView.findViewById(R.id.CLASStextView29);
        T2 = rootView.findViewById(R.id.CLASStextView25);
        T3 = rootView.findViewById(R.id.CLASStextView26);
        W1 = rootView.findViewById(R.id.CLASStextView39);
        W2 = rootView.findViewById(R.id.CLASStextView35);
        W3 = rootView.findViewById(R.id.CLASStextView36);
        W4 = rootView.findViewById(R.id.CLASStextView31);
        Th1 = rootView.findViewById(R.id.CLASStextView49);
        Th2 = rootView.findViewById(R.id.CLASStextView45);
        Th3 = rootView.findViewById(R.id.CLASStextView40);
        F1 = rootView.findViewById(R.id.CLASStextView59);
        F2 = rootView.findViewById(R.id.CLASStextView55);
        F3 = rootView.findViewById(R.id.CLASStextView56);
        F4 = rootView.findViewById(R.id.CLASStextView50);

        List<String> list2 = new ArrayList<>();
        list2.add("Choose Section:");
        list2.add("4th_Year_Regular_Sec_1");
        list2.add("4th_Year_Regular_Sec_2");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, list2);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTable(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateTable(int pos){
        if (pos == 1){
            M1.setText("ITec4112\n" + "T14-E5");
            M2.setText("MGTMT3191\n" + "T14-E5");
            M3.setText("ITec4113\n" + "Lab101");
            M4.setText("");
            T1.setText("ITec4113\n" + "T14-E5");
            T2.setText("ITec4112\n" + "LabMsc");
            T3.setText("ITec4112\n" + "LabMsc");
            W1.setText("ITec4143\n" + "T14-E5");
            W2.setText("ITec4113\n" + "Lab107");
            W3.setText("ITec4112\n" + "LabMsc");
            W4.setText("");
            Th1.setText("ITec4163\n" + "Lab001");
            Th2.setText("ITec4143\n" + "LabMsc");
            Th3.setText("");
            F1.setText("ITec4163\n" + "T14-E5");
            F2.setText("ITec4163\n" + "Lab101");
            F3.setText("ITec4143\n" + "Lab101");
            F4.setText("");
        }else if(pos == 2){
            M1.setText("ITec4113\n" + "Lab107");
            M2.setText("ITec4143\n" + "Lab101");
            M3.setText("ITec4143\n" + "LabMsc");
            M4.setText("ITec4112\n" + "LabMsc");
            T1.setText("ITec4112\n" + "T14-E6");
            T2.setText("MGTMT3191\n" + "T14-E5");
            T3.setText("");
            W1.setText("ITec4163\n" + "Lab001");
            W2.setText("");
            W3.setText("");
            W4.setText("ITec4112\n" + "LabMsc");
            Th1.setText("ITec4143\n" + "T14-E6");
            Th2.setText("ITec4163\n" + "T14-E5");
            Th3.setText("ITec4113\n" + "Lab101");
            F1.setText("ITec4113\n" + "T14-E6");
            F2.setText("ITec4112\n" + "LabMsc");
            F3.setText("");
            F4.setText("ITec4163\n" + "Lab101");
        }
    }

}
