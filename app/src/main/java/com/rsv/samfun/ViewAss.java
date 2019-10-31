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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAss extends Fragment {

    String myValue, ICSID, uname, pword;
    TextView name , id , assm, termC, total, grade, CurrentG;
    Spinner spinner2, spinner3;
    Button va_btn;
    List<String> list2 = new ArrayList<>();
    List<String> list3 = new ArrayList<>();
    View rootView;
    public HashMap<String, String> cookies = new HashMap<>();
    Elements Lang;

    public ViewAss() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        myValue = bundle.getString("name");
        uname = bundle.getString("uname");
        pword = bundle.getString("pword");
        if(bundle.getSerializable("hashMap") != null)
            cookies = (HashMap<String,String>) bundle.getSerializable("hashMap");
        rootView = inflater.inflate(R.layout.content_view_ass, container, false);
        name = rootView.findViewById(R.id.ASSsname);
        id = rootView.findViewById(R.id.ASSsid);
        va_btn = rootView.findViewById(R.id.vg_btn);
        spinner2 = rootView.findViewById(R.id.ASSspin2);
        spinner3 = rootView.findViewById(R.id.ASSspin3);
        assm = rootView.findViewById(R.id.ASStextView9);
        termC = rootView.findViewById(R.id.ASStextView8);
        total = rootView.findViewById(R.id.ASStextView16);
        grade = rootView.findViewById(R.id.ASStextView15);
        CurrentG = rootView.findViewById(R.id.ASStextView11);


        String array[] = myValue.split(" ");
        name.setText(array[0]+" "+array[1]);
        id.setText(array[3]);
        spinner3.setEnabled(false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ViewAss2 va2 = new ViewAss2();
        va2.pdialog = new ProgressDialog(getActivity());
        va2.context = getActivity();
        va2.uname = uname;
        va2.pword = pword;
        //va2.cookies = cookies;
        try{
            va2.execute().get();
        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        cookies.clear();
        cookies.putAll(va2.cookies);
        ICSID = va2.ICSID;
        list2.add("Choose Term:");
        if(va2.Lang != null){
            Lang = va2.Lang;
        }
        for (String row :  va2.list2) {
            list2.add(row);
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, list2);
        dataAdapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter2);
        if (list2.isEmpty()){
            Toast.makeText(getActivity(),"Terms not Found",Toast.LENGTH_LONG).show();
        }else{
            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (position == 0){
                        spinner3.setEnabled(false);
                        spinner3.setBackgroundResource(R.drawable.spinner_border_dark);
                    }else {
                        getCourse(String.valueOf(spinner2.getSelectedItemPosition()-1));
                        Toast.makeText(getActivity(), spinner2.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, list3);
                        dataAdapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spinner3.setAdapter(dataAdapter3);
                        spinner3.setEnabled(true);
                        spinner3.setBackgroundResource(R.drawable.spinner_border);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    spinner3.setEnabled(false);
                }

            });

            spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });
        }

        va_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (spinner2.getSelectedItemPosition() > 0){
                        getAsses(String.valueOf(spinner3.getSelectedItemPosition()));

                    }else {
                        Toast.makeText(getActivity(),"Please select Term and Course",Toast.LENGTH_LONG);
                    }

                }catch (Exception e){
                    System.err.println(e+" On button click");
                }
            }
        });

    }

    private void getCourse(String pos){
        try{
            ViewAss3 va3 = new ViewAss3();
            va3.SSR_DUMMY_RECV1$sels$0 = pos;
            va3.pdialog = new ProgressDialog(getContext());
            va3.context = getContext();
            va3.cookies = cookies;
            va3.ICSID = ICSID;
            if(Lang != null){
                va3.Lang = Lang;
            }
            va3.execute().get();
            list3.clear();
            for (String row :  va3.list3) {
                list3.add(row);
            }

        }catch (Exception e){
            System.err.println(e+" getCourse");
        }
    }

    private void  getAsses(String pos){
        try{
            ViewAss4 va4 = new ViewAss4();
            va4.ICAction = "CLASSTITLE$"+pos;
            va4.pdialog = new ProgressDialog(getContext());
            va4.context = getContext();
            va4.cookies = cookies;
            va4.ICSID = ICSID;
            if(Lang != null){
                va4.Lang = Lang;
            }
            va4.execute().get();

            termC.setText(va4.termC);
            CurrentG.setText("Current Course Grade:     "+va4.CurrentS+"%    "+va4.CurrentG);

            assm.setText("");
            for (String row :  va4.assm) {
                assm.append(row+"\n");
            }
            total.setText("");
            for (String row :  va4.total) {
                total.append(row+"\n");
            }
            grade.setText("");
            for (String row :  va4.grade) {
                grade.append(row+"\n");
            }



        }catch(Exception e){
            System.err.println(e+" getAsses");
        }
    }

}

class ViewAss2 extends AsyncTask<Void, Void, Void> {

    final String tzone = "-180";
    final String loginFormUrl = "http://10.139.8.225/psp/SIS/?cmd=login";
    final String loginActionUrl = "http://10.139.8.225/psp/SIS/?cmd=login&amp;languageCd=ENG";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    final String viewAss = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SS_LAM_STD_GR_LST.GBL";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    public String uname, pword;
    List<String> list2 = new ArrayList<>();
    String ICSID;
    Exception exception;
    ProgressDialog pdialog;
    Context context;
    Elements Lang;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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


            Document getTerm = Jsoup.connect(viewAss)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();

            list2 = getTerm.select("span[id*='TERM_CAR$']").eachText();
            Element IC = getTerm.selectFirst("input#ICSID");
            ICSID = IC.attr("value");
            Lang = getTerm.select("select[id='#ICDataLang']");

        } catch (Exception e) {
            System.err.println(e);
            exception = e;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
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

class ViewAss3 extends AsyncTask<Void, Void, Void>{

    final String viewAss = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SS_LAM_STD_GR_LST.GBL";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    List<String> list3;
    Exception exception;
    ProgressDialog pdialog;
    Context context;
    Elements Lang;

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
    static String SSR_DUMMY_RECV1$sels$0 = "0";
    static String DERIVED_SSTSNAV_SSTS_MAIN_GOTO$70$ = "9999";

    @Override
    protected void onPreExecute() {
        pdialog.setMessage("Fetching Grades...");
        pdialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {

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
        if(Lang != null) {
            formData.put("#ICDataLang", "ENG");
        }
        formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$);
        formData.put("SSR_DUMMY_RECV1$sels$0", SSR_DUMMY_RECV1$sels$0);
        formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$70$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$70$);

        try {

            Document getcourses = Jsoup.connect(viewAss)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .get();

            list3 = getcourses.select("a[id*='CLASSTITLE$']").eachText();

            //System.out.println(getcourses+"\n ViewAss ending");

        } catch (Exception e) {
            System.err.println(e);
            exception = e;

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
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

class ViewAss4 extends AsyncTask<Void, Void, Void>{

    final String viewAss = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SS_LAM_STD_GR_LST.GBL";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    String termC, CurrentG, CurrentS;
    List<String> assm, total, grade = new ArrayList<>();
    Exception exception;
    ProgressDialog pdialog;
    Context context;
    Elements Lang;

    static String ICAJAX = "1";
    static String ICNAVTYPEDROPDOWN = "0";
    static String ICType = "Panel";
    static String ICElementNum = "0";
    static String ICStateNum = "2";
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
    static String ICActionPrompt = "false";
    static String ICTypeAheadID = "";
    static String ICFind = "";
    static String ICAddCount = "";
    static String ICAPPCLSDATA = "";
    static String DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$ = "9999";
    //static String SSR_DUMMY_RECV1$sels$0 = "0";
    static String DERIVED_SSTSNAV_SSTS_MAIN_GOTO$69$ = "9999";

    @Override
    protected void onPreExecute() {
        pdialog.setMessage("Fetching Grades...");
        pdialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        formData.clear();
        formData.put("ICAJAX", ICAJAX);
        formData.put("ICNAVTYPEDROPDOWN", ICNAVTYPEDROPDOWN);
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
        formData.put("ICActionPrompt", ICActionPrompt);
        formData.put("ICTypeAheadID", ICTypeAheadID);
        formData.put("ICFind", ICFind);
        formData.put("ICAddCount", ICAddCount);
        formData.put("ICAPPCLSDATA", ICAPPCLSDATA);
        if(Lang != null) {
            formData.put("#ICDataLang", "ENG");
        }
        formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$);
        //formData.put("SSR_DUMMY_RECV1$sels$0", SSR_DUMMY_RECV1$sels$0);
        formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$69$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$69$);

        try {

            Document getLink = Jsoup.connect(viewAss)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .get();

            System.out.println(getLink);
            String link = null;
            Element text = getLink.select("GENSCRIPT[id='onloadScript']").last();
            if (text != null){
                String txt = getLink.select("GENSCRIPT[id='onloadScript']").last().text();
                if (txt.contains("document.location")){
                    link = getLink.select("GENSCRIPT[id='onloadScript']").last().text();
                    link = link.replace("document.location='", "");
                    link = link.substring(0, link.indexOf("';"));
                }
            }
            System.out.println(link+" linkkkkkkkkkkkk");

            String html2 = null;
            if (link != null){
                Document getAss = Jsoup.connect(link)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .method(Connection.Method.GET)
                    .followRedirects(true)
                    .get();
                html2 = getAss.text()+"\n"+getAss.html();
            }

            if (html2 == null){ html2 ="";}
            html2 = html2+"\n"+getLink.text()+"\n"+getLink.html();
            Document doc2 = Jsoup.parse(html2);
            System.out.println(doc2);

            termC = doc2.select("span[title='View Details']").first().text();
            System.out.println(termC);
            CurrentG = doc2.select("span[id='DERIVED_LAM_CRSE_GRADE_INPUT']").first().text();
            System.out.println(CurrentG);
            CurrentS = doc2.select("span[id='STDNT_GRADE_HDR_GRADE_AVG_CURRENT']").first().text();
            System.out.println(CurrentS);
            assm = doc2.select("a[id*='ACTIVITY$']").eachText();
            System.out.println(assm);
            total = doc2.select("span[id*='LAM_CLASS_ACTV_MARK_OUT_OF$']").eachText();
            System.out.println(total);
            grade = doc2.select("span[id*='STDNT_GRADE_DTL_STUDENT_GRADE$']").eachText();
            System.out.println(grade);


        }catch(Exception e){
            System.err.println(e+" ViewAss4");
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
       /* if (exception != null && !exception.getMessage().isEmpty()){

            String Msg = exception.toString();
            if (Msg.contains("ENETUNREACH")) {
                Msg = "Network is Unreachable";
            } else if (Msg.contains("EHOSTUNREACH")) {
                Msg = "No Route to Host, Connect to UoG Campus Network";
            }
            final String finalMsg = Msg;
            makeToast(context, finalMsg, Toast.LENGTH_SHORT);
        }*/

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