package com.rsv.samfun;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewGrade extends Fragment {

    public ViewGrade() {
        // Required empty public constructor
    }

    String myValue, uname, pword, ICSID;
    TextView name , id , cuml , termG , enroll, termC, unit, grade, gradep;
    Spinner spinner1;
    Button vg_btn;
    List<String> list = new ArrayList<>();
    View rootView;
    Context ctx;
    public HashMap<String, String> cookies = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        myValue = bundle.getString("name");
        if(bundle.getSerializable("hashMap") != null)
            cookies = (HashMap<String,String>) bundle.getSerializable("hashMap");
        uname = bundle.getString("uname");
        pword = bundle.getString("pword");
        rootView = inflater.inflate(R.layout.content_view_grade, container, false);
        name = rootView.findViewById(R.id.Gradesname);
        vg_btn = rootView.findViewById(R.id.vg_btn);
        spinner1 = rootView.findViewById(R.id.Gradespin);
        id = rootView.findViewById(R.id.Gradesid);
        cuml = rootView.findViewById(R.id.GradetextView6);
        termG = rootView.findViewById(R.id.GradetextView9);
        enroll = rootView.findViewById(R.id.GradetextView49);
        termC = rootView.findViewById(R.id.Gradetext10);
        unit = rootView.findViewById(R.id.Gradetext11);
        grade = rootView.findViewById(R.id.Gradetext12);
        gradep = rootView.findViewById(R.id.Gradetext13);

        String array[] = myValue.split(" ");
        name.setText(array[0]+" "+array[1]);
        id.setText(array[3]);
        ctx = getActivity();


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewGrade2 vg2 = new ViewGrade2();
        vg2.pdialog = new ProgressDialog(ctx);
        vg2.context = ctx;
        vg2.uname = uname;
        vg2.pword = pword;
        //vg2.cookies = cookies;
        try{
            vg2.execute().get();

            ICSID = vg2.ICSID;
        }catch (Exception e){
            System.err.println(e);
        }

        cookies.clear();
        cookies.putAll(vg2.cookies);
        for (String row :  vg2.list) {
            list.add(row);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        vg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getGrade(String.valueOf(spinner1.getSelectedItemPosition()));
                }catch (Exception e){
                    System.err.println(e);
                }
            }
        });
    }

    private void getGrade(String pos){
        try{
            ViewGrade3 vg3 = new ViewGrade3();
            vg3.SSR_DUMMY_RECV1$sels$0 = pos;
            vg3.pdialog = new ProgressDialog(ctx);
            vg3.context = ctx;
            vg3.cookies = cookies;
            vg3.ICSID = ICSID;
            vg3.execute().get();
            cuml.setText(vg3.cuml);
            termG.setText(vg3.termG);
            enroll.setText(vg3.enroll);

            termC.setText("");
            for (String row : vg3.termC) {
                termC.append(row+"\n");
            }
            unit.setText("");
            for (String row : vg3.ects) {
                unit.append(row+"\n");
            }
            grade.setText("");
            for (String row : vg3.grades) {
                grade.append(row+"\n");
            }
            gradep.setText("");
            for (String row : vg3.gradep) {
                gradep.append(row+"\n");
            }
            Toast.makeText(getContext(),spinner1.getSelectedItem().toString(),Toast.LENGTH_LONG);

        }catch (Exception e){
            System.err.println(e+" Appending");
        }
    }
}

class ViewGrade2 extends AsyncTask<Void, Void, Void> {

        final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
        final String tzone = "-180";
        final String loginFormUrl = "http://10.139.8.225/psp/SIS/?cmd=login";
        final String loginActionUrl = "http://10.139.8.225/psp/SIS/?cmd=login&amp;languageCd=ENG";
        final String viewgrade = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL";
        public HashMap<String, String> cookies = new HashMap<>();
        public HashMap<String, String> formData = new HashMap<>();
        public String uname, pword, ICSID;
        List<String> list = new ArrayList<>();
        Exception exception;
        ProgressDialog pdialog;
        Context context;

    @Override
    protected void onPreExecute() {
        pdialog.setMessage("Fetching Terms...");
        pdialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            Connection.Response loginForm = Jsoup.connect(loginFormUrl)
                    .followRedirects(true)
                    .method(Connection.Method.GET)
                    .userAgent(USER_AGENT).execute();
            cookies.putAll(loginForm.cookies());
            formData.put("userid", uname);
            formData.put("pwd", pword);
            formData.put("timezoneOffset", tzone);
            Connection.Response login = Jsoup.connect(loginActionUrl)
                    .cookies(loginForm.cookies())
                    .data(formData)
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .followRedirects(true)
                    .execute();

            cookies.clear();
            cookies.putAll(login.cookies());

            Document getTerm = Jsoup.connect(viewgrade)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();
            //System.out.println(getTerm);

            String html = getTerm.text()+"\n"+getTerm.html();
            Document doc = Jsoup.parse(html);
            Element IC = doc.selectFirst("input#ICSID");
            ICSID = IC.attr("value");

            list = doc.select("span[id*='TERM_CAR$']").eachText();
            if (list.isEmpty() || list == null){
                formData.clear();
                formData.put("ICType", "Panel");
                formData.put("ICElementNum", "0");
                formData.put("ICStateNum", "1");
                formData.put("ICAction", "DERIVED_SSS_SCT_SSS_TERM_LINK");
                formData.put("ICXPos", "0");
                formData.put("ICYPos", "0");
                formData.put("ResponsetoDiffFrame", "-1");
                formData.put("TargetFrameName", "None");
                formData.put("FacetPath", "None");
                formData.put("ICFocus", "");
                formData.put("ICSaveWarningFilter", "0");
                formData.put("ICChanged", "-1");
                formData.put("ICResubmit", "0");
                formData.put("ICSID", ICSID);
                formData.put("ICAPPCLSDATA", "");
                formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$", "9999");
                formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$92$", "9999");

                Document getTerm2 = Jsoup.connect(viewgrade)
                        .userAgent(USER_AGENT)
                        .cookies(cookies)
                        .data(formData)
                        .followRedirects(true)
                        .get();
                String html2 = getTerm2.text()+"\n"+getTerm2.html();
                Document doc2 = Jsoup.parse(html2);
                System.out.println(doc2);

                list = doc2.select("span[id*='TERM_CAR$']").eachText();
            }

        } catch (Exception e) {
            System.err.println(e);
            exception = e;

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (exception != null && !exception.getMessage().isEmpty()){

            String Msg = exception.toString();
            if (Msg.contains("ENETUNREACH")) {
                Msg = "Network is Unreachable";
            } else if (Msg.contains("EHOSTUNREACH")) {
                Msg = "No Route to Host, Connect to UoG Campus Network";
            }
            final String finalMsg = Msg;
            makeToast(context, finalMsg, Toast.LENGTH_SHORT);
        }
        if (pdialog.isShowing()) {
                pdialog.dismiss();
            }

    }

    private void makeToast(final Context ctx, final String msg, final int dur) {

                Toast toast = Toast.makeText(ctx, msg, dur);
                View view = toast.getView();
                view.setBackgroundColor(Color.TRANSPARENT);

                TextView text = view.findViewById(android.R.id.message);
                text.setShadowLayer((float) 0.75, 2, 4, Color.BLACK);
                text.setTextColor(Color.WHITE);
                toast.show();
    }
}

class ViewGrade3 extends AsyncTask<Void,Void, Void>{

    //static String username = "Samson.mis1";
    //static String password = "Arcadia1675";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    //final String tzone = "-180";
    //final String loginFormUrl = "http://10.139.8.225/psp/SIS/?cmd=login";
    //final String loginActionUrl = "http://10.139.8.225/psp/SIS/?cmd=login&amp;languageCd=ENG";
    final String viewgrade = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_GRADE.GBL";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    String cuml, termG, enroll;
    List<String> termC, ects, grades, gradep;
    Exception exception3;
    ProgressDialog pdialog;
    Context context;

    //static String ICAJAX = "1";
    //static String ICNAVTYPEDROPDOWN = "0";
    static String ICType = "Panel";
    static String ICElementNum = "0";
    static String ICStateNum = "1";
    static String ICAction = "DERIVED_SSS_SCT_SSR_PB_GO";
    static String ICXPos = "0";
    static String ICYPos = "0";
    static String ResponsetoDiffFrame = "-1";
    static String TargetFrameName = "None";
    static String FacetPath = "None";
    static String ICFocus = "";
    static String ICSaveWarningFilter = "0";
    static String ICChanged = "-1";
    static String ICResubmit = "0";
    static String ICSID = "";
    //static String ICActionPrompt = "false";
    //static String ICTypeAheadID = "";
    //static String ICFind = "";
    //static String ICAddCount = "";
    static String ICAPPCLSDATA = "";
    static String DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$ = "9999";
    static String SSR_DUMMY_RECV1$sels$0 = "";
    static String DERIVED_SSTSNAV_SSTS_MAIN_GOTO$70$ = "9999";

    @Override
    protected void onPreExecute() {
        pdialog.setMessage("Fetching Grades...");
        pdialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Document getTerm = Jsoup.connect(viewgrade)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();
            String html3 = getTerm.text()+"\n"+getTerm.html();
            Document doc3 = Jsoup.parse(html3);
            Element page1 = doc3.select("a[id='DERIVED_SSS_SCT_SSS_TERM_LINK']").first();

            if (page1 != null) {
                formData.clear();
                formData.put("ICType", "Panel");
                formData.put("ICElementNum", "0");
                formData.put("ICStateNum", "1");
                formData.put("ICAction", "DERIVED_SSS_SCT_SSS_TERM_LINK");
                formData.put("ICXPos", "0");
                formData.put("ICYPos", "0");
                formData.put("ResponsetoDiffFrame", "-1");
                formData.put("TargetFrameName", "None");
                formData.put("FacetPath", "None");
                formData.put("ICFocus", "");
                formData.put("ICSaveWarningFilter", "0");
                formData.put("ICChanged", "-1");
                formData.put("ICResubmit", "0");
                formData.put("ICSID", ICSID);
                formData.put("ICAPPCLSDATA", "");
                formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$", "9999");
                formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$92$", "9999");

                Document getGrade0 = Jsoup.connect(viewgrade)
                        .userAgent(USER_AGENT)
                        .cookies(cookies)
                        .data(formData)
                        .followRedirects(true)
                        .get();
                String html2 = getGrade0.text() + "\n" + getGrade0.html();
                Document doc2 = Jsoup.parse(html2);
                //System.out.println(doc2);
                ICStateNum = "2";
            }
            Element IC = getTerm.select("input#ICSID").first();
            ICSID = IC.attr("value");


            formData.clear();
            //formData.put("ICAJAX", ICAJAX);
            //formData.put("ICNAVTYPEDROPDOWN", ICNAVTYPEDROPDOWN);
            formData.put("ICType", ICType);
            formData.put("ICElementNum", ICElementNum);
            formData.put("ICStateNum", ICStateNum);
            formData.put("ICAction", ICAction);
            formData.put("ICXPos", ICXPos);
            formData.put("ICYPos", ICYPos);
            formData.put("ResponsetoDiffFrame", ResponsetoDiffFrame);
            formData.put("TargetFrameName", TargetFrameName);
            formData.put("FacetPath", FacetPath);
            formData.put("ICFocus", ICFocus);
            formData.put("ICSaveWarningFilter", ICSaveWarningFilter);
            formData.put("ICChanged", ICChanged);
            formData.put("ICResubmit", ICResubmit);
            formData.put("ICSID", ICSID);
            //formData.put("ICActionPrompt", ICActionPrompt);
            //formData.put("ICTypeAheadID", ICTypeAheadID);
            //formData.put("ICFind", ICFind);
            //formData.put("ICAddCount", ICAddCount);
            formData.put("ICAPPCLSDATA", ICAPPCLSDATA);
            formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$);
            formData.put("SSR_DUMMY_RECV1$sels$0", SSR_DUMMY_RECV1$sels$0);
            formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$70$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$70$);

            Document getGrade = Jsoup.connect(viewgrade)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .get();

            System.out.println(getGrade+" View Grade ending");
            String html = getGrade.text()+"\n"+getGrade.html();
            Document doc = Jsoup.parse(html);

            List<String> cumlt;
            List<String> termgr;

            cumlt = doc.select("span[id*='STATS_CUMS$']").eachText();
            termgr = doc.select("span[id*='STATS_ENRL$']").eachText();

            /*if(SSR_DUMMY_RECV1$sels$0.equalsIgnoreCase("0")) {
                cuml = doc.select("span[id='STATS_CUMS$13']").text();
                termG = doc.select("span[id='STATS_ENRL$13']").text();
            }else {
                cuml = doc.select("span[id='STATS_CUMS$12']").text();
                termG = doc.select("span[id='STATS_ENRL$12']").text();
            }*/

            cuml = cumlt.get(cumlt.size()-1);
            termG = termgr.get(termgr.size()-1);

            enroll = doc.select("span#ACAD_STACTN_TBL_DESCRFORMAL").text();

            termC  = new ArrayList<>();
            termC = doc.select("a[name*='CLS_LINK$']").eachText();

            ects = new ArrayList<String>();
            ects = doc.select("span[id*='STDNT_ENRL_SSV1_UNT_TAKEN$']").eachText();

            grades = new ArrayList<String>();
            grades = doc.select("span[id*='STDNT_ENRL_SSV1_CRSE_GRADE_OFF$']").eachText();

            gradep = new ArrayList<String>();
            gradep = doc.select("span[id*='STDNT_ENRL_SSV1_GRADE_POINTS$']").eachText();


        }catch (Exception e){
            System.err.println(e+" Viewgrade3");
            exception3 = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (exception3 != null && !exception3.getMessage().isEmpty()){

            String Msg = exception3.toString();
            if (Msg.contains("ENETUNREACH")) {
                Msg = "Network is Unreachable";
            } else if (Msg.contains("EHOSTUNREACH")) {
                Msg = "No Route to Host, Connect to UoG Campus Network";
            }
            final String finalMsg = Msg;
            makeToast(context, finalMsg, Toast.LENGTH_SHORT);
        }
        if (pdialog.isShowing()) {
            pdialog.dismiss();
        }
    }

    private void makeToast(final Context ctx, final String msg, final int dur) {

        Toast toast = Toast.makeText(ctx, msg, dur);
        View view = toast.getView();
        view.setBackgroundColor(Color.TRANSPARENT);

        TextView text = view.findViewById(android.R.id.message);
        text.setShadowLayer((float) 0.75, 2, 4, Color.BLACK);
        text.setTextColor(Color.WHITE);
        toast.show();
    }
}