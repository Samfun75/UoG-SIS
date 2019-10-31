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
import android.widget.Button;
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

public class View_weekly extends Fragment {

    TextView Ctitle, Sdate, Edate, headtxt;
    String ICSID, uname, pword, new_strdt;
    List<String> listCr = new ArrayList<>();
    List<String> listSd = new ArrayList<>();
    List<String> listEd = new ArrayList<>();
    View rootView;
    public HashMap<String, String> cookies = new HashMap<>();
    Button prv_btn, nxt_btn;


    public View_weekly() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            Bundle bundle = this.getArguments();
            uname = bundle.getString("uname");
            pword = bundle.getString("pword");
            if(bundle.getSerializable("hashMap") != null)
                cookies = (HashMap<String,String>) bundle.getSerializable("hashMap");
            rootView = inflater.inflate(R.layout.content_viewweekly, container, false);
            Ctitle = rootView.findViewById(R.id.WEEKtextView8);
            Sdate = rootView.findViewById(R.id.WEEKtextView15);
            Edate = rootView.findViewById(R.id.WEEKtextView16);
            headtxt = rootView.findViewById(R.id.WEEKtextView30);
            prv_btn = rootView.findViewById(R.id.WEEKbutton2);
            nxt_btn = rootView.findViewById(R.id.WEEKbutton3);
        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View_weekly2 vw2 = new View_weekly2();
        try{
            vw2.uname = uname;
            vw2.pword = pword;
            vw2.pdialog = new ProgressDialog(getContext());
            vw2.context = getContext();
            vw2.execute().get();

            cookies.clear();
            cookies.putAll(vw2.cookies);
            headtxt.setText("");
            headtxt.setText(vw2.headtxt);
            listCr = vw2.listCr;
            listSd = vw2.listSd;
            listEd = vw2.listEd;


            Ctitle.setText("");
            if (listCr != null){
                for (String row : listCr) {
                    Ctitle.append(row+"\n");
                }
            }
            Sdate.setText("");
            if (listSd != null){
                for (String row : listSd) {
                    Sdate.append(row+"\n");
                }
            }
            Edate.setText("");
            if (listEd != null){
                for (String row : listEd) {
                    Edate.append(row+"\n");
                }
            }
            new_strdt = vw2.new_strdt;
            prv_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateWeek(new_strdt,0);
                }
            });

            nxt_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateWeek(new_strdt,1);
                }
            });
        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void updateWeek(String newstrdt, int action){
        View_weekly3 vw3 = new View_weekly3();
        try{
            vw3.action = action;
            vw3.new_strdt = newstrdt;
            vw3.cookies = cookies;
            vw3.execute().get();
            System.out.println("Chekinggggggggggggggggg");
            headtxt.setText(vw3.headtxt);

            Ctitle.setText("");
            if (!vw3.listCr.isEmpty()){
                for (String row : vw3.listCr) {
                    Ctitle.append(row+"\n");
                }
            }

            Sdate.setText("");
            if (!vw3.listSd.isEmpty()){
                for (String row : vw3.listSd) {
                    Sdate.append(row+"\n");
                }
            }

            Edate.setText("");
            if (!vw3.listEd.isEmpty()){
                for (String row : vw3.listEd) {
                    Edate.append(row+"\n");
                }
            }
            new_strdt = vw3.new_strdt;


        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage()+" inside update error",Toast.LENGTH_LONG).show();
        }
    }
}

class View_weekly2 extends AsyncTask<Void, Void, Void>{

    final String tzone = "-180";
    final String loginFormUrl = "http://10.139.8.225/psp/SIS/?cmd=login";
    final String loginActionUrl = "http://10.139.8.225/psp/SIS/?cmd=login&amp;languageCd=ENG";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    final String viewWeek = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_SCHD_W.GBL";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    public String uname, pword, ICSID;
    String headtxt, new_strdt;
    List<String> listCr = new ArrayList<>();
    List<String> listSd = new ArrayList<>();
    List<String> listEd = new ArrayList<>();
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

            Document getWeek = Jsoup.connect(viewWeek)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();

            Lang = getWeek.select("select[id='#ICDataLang']");
            Element IC = getWeek.selectFirst("input#ICSID");
            ICSID = IC.attr("value");

                //System.out.println(getWeek);
                String html = getWeek.text() + "\n" + getWeek.html();
                Document doc2 = Jsoup.parse(html);

                headtxt = doc2.select("td.SSSGROUPBOXGREEN").first().text();
                System.out.println(headtxt);

                Elements lstCr = doc2.select("span[id*='CLASS_TITLE$']");
                if (lstCr != null){
                    listCr = lstCr.eachText();
                    for (String row : listCr) {
                        System.out.println(row);
                    }
                }

                Elements lstSd = doc2.select("span[id*='STDNT_WK_NO_MTG_START_DT$']");
                if (lstSd != null){
                    listSd = lstSd.eachText();
                    for (String row : listSd) {
                        System.out.println(row);
                    }
                }

                Elements lstEd = doc2.select("span[id*='STDNT_WK_NO_MTG_END_DT$']");
                if (lstEd != null){
                    listEd = lstEd.eachText();
                    for (String row : listEd) {
                        System.out.println(row);
                    }
                }

                Element strdt_new = doc2.selectFirst("input#DERIVED_CLASS_S_START_DT");
                new_strdt = strdt_new.attr("value");
                System.out.println(new_strdt);



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

class View_weekly3 extends AsyncTask<Void, Void, Void>{

    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    final String viewWeek = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_SCHD_W.GBL";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    String headtxt, new_strdt;
    List<String> listCr = new ArrayList<>();
    List<String> listSd = new ArrayList<>();
    List<String> listEd = new ArrayList<>();
    public int action;
    Exception exception;
    ProgressDialog pdialog;
    Context context;
    Elements Lang;

    static String ICType = "Panel";
    static String ICElementNum = "0";
    static String ICStateNum = "1";
    static String ICActionN = "DERIVED_CLASS_S_SSR_NEXT_WEEK";
    static String ICActionP = "DERIVED_CLASS_S_SSR_PREV_WEEK";
    static String ICXPos = "0";
    static String ICYPos = "73";
    static String ResponsetoDiffFrame = "-1";
    static String TargetFrameName = "None";
    static String FacetPath = "None";
    static String ICFocus = "";
    static String ICSaveWarningFilter = "0";
    static String ICChanged = "-1";
    static String ICResubmit = "0";
    static String ICSID = "";
    static String ICActionPrompt = "false";
    static String ICAPPCLSDATA = "";
    static String ICDataLang = "ENG";
    static String DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$ = "9999";
    static String DERIVED_REGFRM1_SSR_SCHED_FORMAT$38$ = "W";
    static String DERIVED_CLASS_S_START_DT = "06/03/2019";
    static String DERIVED_CLASS_S_MEETING_TIME_START = "8:00AM";
    static String DERIVED_CLASS_S_MEETING_TIME_END = "6:00PM";
    static String DERIVED_CLASS_S_SHOW_AM_PM$chk = "Y";
    static String DERIVED_CLASS_S_SHOW_AM_PM = "Y";
    static String DERIVED_CLASS_S_MONDAY_LBL$81$$chk = "Y";
    static String DERIVED_CLASS_S_MONDAY_LBL$81$ = "Y";
    static String DERIVED_CLASS_S_THURSDAY_LBL$chk = "Y";
    static String DERIVED_CLASS_S_THURSDAY_LBL = "Y";
    static String DERIVED_CLASS_S_SUNDAY_LBL$chk = "Y";
    static String DERIVED_CLASS_S_SUNDAY_LBL = "Y";
    static String DERIVED_CLASS_S_SSR_DISP_TITLE$chk = "N";
    static String DERIVED_CLASS_S_TUESDAY_LBL$chk = "Y";
    static String DERIVED_CLASS_S_TUESDAY_LBL = "Y";
    static String DERIVED_CLASS_S_FRIDAY_LBL$chk = "Y";
    static String DERIVED_CLASS_S_FRIDAY_LBL = "Y";
    static String DERIVED_CLASS_S_SHOW_INSTR$chk = "N";
    static String DERIVED_CLASS_S_WEDNESDAY_LBL$chk = "Y";
    static String DERIVED_CLASS_S_WEDNESDAY_LBL = "Y";
    static String DERIVED_CLASS_S_SATURDAY_LBL$chk = "Y";
    static String DERIVED_CLASS_S_SATURDAY_LBL = "Y";
    static String DERIVED_SSTSNAV_SSTS_MAIN_GOTO$102$ = "9999";

    @Override
    protected Void doInBackground(Void... voids) {

            DERIVED_CLASS_S_START_DT = new_strdt;
        formData.clear();
        formData.put("ICType", ICType);
        formData.put("ICElementNum", ICElementNum);
        formData.put("ICStateNum", ICStateNum);
        if (action == 0) {
            formData.put("ICAction", ICActionP);
        }else if (action == 1){
            formData.put("ICAction", ICActionN);
        }
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
        formData.put("ICAPPCLSDATA", ICAPPCLSDATA);
        if(Lang != null) {
            formData.put("#ICDataLang", ICDataLang);
        }
        formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$22$);
        formData.put("DERIVED_REGFRM1_SSR_SCHED_FORMAT$38$", DERIVED_REGFRM1_SSR_SCHED_FORMAT$38$);
        formData.put("DERIVED_CLASS_S_START_DT", DERIVED_CLASS_S_START_DT);
        formData.put("DERIVED_CLASS_S_MEETING_TIME_START", DERIVED_CLASS_S_MEETING_TIME_START);
        formData.put("DERIVED_CLASS_S_MEETING_TIME_END", DERIVED_CLASS_S_MEETING_TIME_END);
        formData.put("DERIVED_CLASS_S_SHOW_AM_PM$chk", DERIVED_CLASS_S_SHOW_AM_PM$chk);
        formData.put("DERIVED_CLASS_S_SHOW_AM_PM",DERIVED_CLASS_S_SHOW_AM_PM );
        formData.put("DERIVED_CLASS_S_MONDAY_LBL$81$$chk", DERIVED_CLASS_S_MONDAY_LBL$81$$chk);
        formData.put("DERIVED_CLASS_S_MONDAY_LBL$81$", DERIVED_CLASS_S_MONDAY_LBL$81$);
        formData.put("DERIVED_CLASS_S_THURSDAY_LBL$chk", DERIVED_CLASS_S_THURSDAY_LBL$chk);
        formData.put("DERIVED_CLASS_S_THURSDAY_LBL", DERIVED_CLASS_S_THURSDAY_LBL);
        formData.put("DERIVED_CLASS_S_SUNDAY_LBL$chk", DERIVED_CLASS_S_SUNDAY_LBL$chk);
        formData.put("DERIVED_CLASS_S_SUNDAY_LBL", DERIVED_CLASS_S_SUNDAY_LBL);
        formData.put("DERIVED_CLASS_S_SSR_DISP_TITLE$chk", DERIVED_CLASS_S_SSR_DISP_TITLE$chk);
        formData.put("DERIVED_CLASS_S_TUESDAY_LBL$chk", DERIVED_CLASS_S_TUESDAY_LBL$chk);
        formData.put("DERIVED_CLASS_S_TUESDAY_LBL", DERIVED_CLASS_S_TUESDAY_LBL);
        formData.put("DERIVED_CLASS_S_FRIDAY_LBL$chk", DERIVED_CLASS_S_FRIDAY_LBL$chk);
        formData.put("DERIVED_CLASS_S_FRIDAY_LBL", DERIVED_CLASS_S_FRIDAY_LBL);
        formData.put("DERIVED_CLASS_S_SHOW_INSTR$chk", DERIVED_CLASS_S_SHOW_INSTR$chk);
        formData.put("DERIVED_CLASS_S_WEDNESDAY_LBL$chk", DERIVED_CLASS_S_WEDNESDAY_LBL$chk);
        formData.put("DERIVED_CLASS_S_WEDNESDAY_LBL", DERIVED_CLASS_S_WEDNESDAY_LBL);
        formData.put("DERIVED_CLASS_S_SATURDAY_LBL$chk", DERIVED_CLASS_S_SATURDAY_LBL$chk);
        formData.put("DERIVED_CLASS_S_SATURDAY_LBL", DERIVED_CLASS_S_SATURDAY_LBL);
        formData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$102$", DERIVED_SSTSNAV_SSTS_MAIN_GOTO$102$);

        try {
            Document getweek = Jsoup.connect(viewWeek)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .get();

            System.out.println(getweek);
            String html = getweek.text() + "\n" + getweek.html();
            Document doc = Jsoup.parse(html);

            headtxt = doc.select("td.SSSGROUPBOXGREEN").first().text();
            System.out.println(headtxt);

            listCr = doc.select("span[id*='CLASS_TITLE$']").eachText();
            if (listCr != null) {
                for (String row : listCr) {
                    System.out.println(row);
                }
            }

            listSd = doc.select("span[id*='STDNT_WK_NO_MTG_START_DT$']").eachText();
            if (listSd != null) {
                for (String row : listSd) {
                    System.out.println(row);
                }
            }

            listEd = doc.select("span[id*='STDNT_WK_NO_MTG_END_DT$']").eachText();
            if (listEd != null) {
                for (String row : listEd) {
                    System.out.println(row);
                }
            }

            Element strdt_new = doc.selectFirst("input#DERIVED_CLASS_S_START_DT");
            new_strdt = strdt_new.attr("value");
            System.out.println(new_strdt + "new strdt check pointttt");


        }catch (Exception e){
            System.err.println(e);
        }

        return null;
    }
}