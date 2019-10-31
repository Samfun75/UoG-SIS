package com.rsv.samfun;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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

public class Spelling extends Fragment {

    String myValue, uname, pword, ICSID;
    TextView CID;
    EditText Ename , Aname;
    public HashMap<String, String> cookies = new HashMap<>();
    Switch mSwitch;
    Button spl_btn;
    WebView Img;
    View rootView;
    Spelling2 S2 = new Spelling2();

    public Spelling() {
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
        rootView = inflater.inflate(R.layout.content_spelling, container, false);
        Ename = rootView.findViewById(R.id.SPELLEng);
        Aname = rootView.findViewById(R.id.SPELLAmh);
        CID = rootView.findViewById(R.id.SPELLsid);
        mSwitch = rootView.findViewById(R.id.switch1);
        Img = rootView.findViewById(R.id.webView);
        spl_btn = rootView.findViewById(R.id.button5);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        S2.pdialog = new ProgressDialog(getActivity());
        S2.context = getActivity();
        S2.uname = uname;
        S2.pword = pword;
        try{
            S2.execute().get();

            Ename.setText(S2.Ename);
            Ename.setEnabled(false);
            Aname.setText(S2.Aname);
            Aname.setEnabled(false);
            CID.setText(S2.CID);
            ICSID = S2.ICSID;
            if(S2.UG_SPL_CRCTN_FR_CHECKED == 0){
                mSwitch.setEnabled(false);
                spl_btn.setEnabled(false);
                spl_btn.setBackgroundResource(R.color.darkgray);
            }

            cookies.clear();
            cookies.putAll(S2.cookies);

            Img.setInitialScale(1);
            Img.getSettings().setLoadsImagesAutomatically(true);
            Img.getSettings().setUseWideViewPort(true);
            Img.getSettings().setLoadWithOverviewMode(true);
            Img.setWebViewClient(new WebViewClient(){
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Img.loadUrl("file:///android_asset/myerrorpage.html");
                }
            });
            Img.loadUrl("http://10.139.8.225"+S2.ImgUrl, S2.cookies);

        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        spl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitch.getText().equals("Yes")){
                    String SubNamesE[] = Ename.getText().toString().split(" ");
                    String SubNmaesA[] = Aname.getText().toString().split(" ");
                    if (SubNamesE.length == 3 && SubNmaesA.length == 3){
                        SubmitNames(SubNamesE, SubNmaesA, 1);
                    }else {
                                AlertDialog failed = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
                                failed.setCanceledOnTouchOutside(false);
                                failed.setTitle("Please Correct the Format");
                                failed.setMessage("Correct usage: FirstName MiddleName LastName\nDon't Add extra Spaces");
                                failed.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                failed.show();

                    }
                    Toast.makeText(getActivity(),"We are working on it...",Toast.LENGTH_LONG).show();
                }else if (mSwitch.getText().equals("No")){
                    String SubNamesE[] = Ename.getText().toString().split(" ");
                    String SubNmaesA[] = Aname.getText().toString().split(" ");
                    if (SubNamesE.length == 3 && SubNmaesA.length == 3){
                        SubmitNames(SubNamesE, SubNmaesA,0);
                    }else {
                        AlertDialog failed = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
                        failed.setCanceledOnTouchOutside(false);
                        failed.setTitle("Please Correct the Format");
                        failed.setMessage("Correct usage: FirstName MiddleName LastName\nDon't Add extra Spaces");
                        failed.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        failed.show();

                    }
                }
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Ename.setEnabled(true);
                    Ename.setBackgroundResource(R.drawable.general_boarder);
                    Aname.setEnabled(true);
                    Aname.setBackgroundResource(R.drawable.general_boarder);
                }else {
                    Ename.setEnabled(false);
                    Ename.setText(S2.Ename);
                    Ename.setBackgroundColor(Color.TRANSPARENT);
                    Aname.setEnabled(false);
                    Aname.setText(S2.Aname);
                    Aname.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

    }

    private void SubmitNames(String[] SubNamesE, String[] SubNamesA, int i) {
        Spelling3 S3 = new Spelling3();
        if(i == 1){
            S3.ICSID = ICSID;
            S3.pdialog = new ProgressDialog(getActivity());
            S3.context = getActivity();
            S3.cookies = cookies;
            S3.UG_SPL_CRCTN_FR_CHECKED = "Y";
            S3.UG_SPL_CRCTN_FR_FIRST_NAME = SubNamesE[0];
            S3.UG_SPL_CRCTN_FR_MIDDLE_NAME = SubNamesE[1];
            S3.UG_SPL_CRCTN_FR_LAST_NAME = SubNamesE[2];
            S3.UG_SPL_CRCTN_FR_FIRST_NAME_CD = SubNamesA[0];
            S3.UG_SPL_CRCTN_FR_MIDDLE_NAME_TOEFL = SubNamesA[1];
            S3.UG_SPL_CRCTN_FR_LAST_NAME_CD = SubNamesA[2];
           try {
               S3.execute().get();
               Ename.setText(S3.Ename);
               Aname.setText(S3.Aname);
               CID.setText(S3.CID);

               mSwitch.setEnabled(false);
               spl_btn.setEnabled(false);
               spl_btn.setBackgroundResource(R.color.darkgray);

           }catch (Exception e){
               System.out.println(e);
           }

        }else{

        }

    }


}

class Spelling2 extends AsyncTask<Void, Void, Void>{
    final String tzone = "-180";
    final String loginFormUrl = "http://10.139.8.225/psp/SIS/?cmd=login";
    final String loginActionUrl = "http://10.139.8.225/psp/SIS/?cmd=login&amp;languageCd=ENG";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    final static String spelling= "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/UG_GRAD_RULES.UG_SPLN_CRNT_CMP.GBL";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    public String uname, pword, Ename, Aname, CID, ImgUrl;
    int UG_SPL_CRCTN_FR_CHECKED;
    Exception exception;
    ProgressDialog pdialog;
    Context context;

    static String ICAJAX = "1";
    static String ICNAVTYPEDROPDOWN = "0";
    static String ICType = "Panel";
    static String ICElementNum = "0";
    static String ICStateNum = "1";
    static String ICAction = "#ICRow0";
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
    static String UG_SPL_CRCTN_FR_EMPLID = "";

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
                    .userAgent(USER_AGENT)
                    .execute();
            cookies = (HashMap<String, String>) loginForm.cookies();

            formData.put("userid", uname);
            formData.put("pwd", pword);
            formData.put("timezoneOffset", tzone);
            Connection.Response homePage = Jsoup.connect(loginActionUrl)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .followRedirects(true)
                    .execute();
            cookies.clear();
            cookies.putAll(homePage.cookies());

            Document getspelling = Jsoup.connect(spelling)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();

            String html = getspelling.text()+"\n"+getspelling.html();
            Document doc = Jsoup.parse(html);
            //System.out.println(doc);

            Element IC = doc.selectFirst("input#ICSID");
            ICSID = IC.attr("value");
            System.out.println(ICSID);
            UG_SPL_CRCTN_FR_EMPLID = doc.select("input#UG_SPL_CRCTN_FR_EMPLID").attr("value");
            //Elements Lang = doc.select("select[id='#ICDataLang']");
            System.out.println(UG_SPL_CRCTN_FR_EMPLID);

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
            formData.put("UG_SPL_CRCTN_FR_EMPLID", UG_SPL_CRCTN_FR_EMPLID);
            //if(Lang != null) {
            //    formData.put("#ICDataLang", "ENG");
            //}

            Document getimg = Jsoup.connect(spelling)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .get();

            String html2 = getimg.text()+"\n"+getimg.html();
            Document doc2 = Jsoup.parse(html2);
            //System.out.println(doc2);


            String chk = doc.select("input[id='UG_SPL_CRCTN_FR_EMPLID']").first().attr("class");
            System.out.println(chk);
            if(chk.equalsIgnoreCase("PSEDITBOX"))
            {
                Ename = doc.select("a[id='RESULT1$0']").first().text();
                Aname = doc.select("a[id='RESULT2$0']").first().text();
                CID = doc.select("a[id='RESULT3$0']").first().text();
                UG_SPL_CRCTN_FR_CHECKED = 0;
            }else {

                Ename = doc2.select("span[id='UG_SPL_CRCTN_FR_NAME_DISPLAY_TXT']").first().text();
                Aname = doc2.select("span[id='UG_SPL_CRCTN_FR_NAME_FORMAL_TXT']").first().text();
                CID = doc2.select("span[id='UG_SPL_CRCTN_FR_CAMPUS_ID']").first().text();
                UG_SPL_CRCTN_FR_CHECKED = 1;
            }
            String img = doc.toString() +"\n"+ doc2.toString();
            Document imgDoc = Jsoup.parse(img);
            ImgUrl = imgDoc.select("img.PSIMAGE").attr("src");
            System.out.println(ImgUrl+" image urllllll");


        }catch (Exception e) {
            System.err.println(e+" DoinBackground");
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

class Spelling3 extends AsyncTask<Void, Void, Void>{
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    final static String spelling= "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/UG_GRAD_RULES.UG_SPLN_CRNT_CMP.GBL";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    public String Ename, Aname, CID;
    Exception exception;
    ProgressDialog pdialog;
    Context context;

    static String ICAJAX = "1";
    static String ICNAVTYPEDROPDOWN = "0";
    static String ICType = "Panel";
    static String ICElementNum = "0";
    static String ICStateNum = "1";
    static String ICActionBc = "UG_SPL_CRCTN_FR_CHECKED";
    static String ICActionAc = "UG_GRAD_WRK_SUBMIT_BTN";
    static String ICXPos = "0";
    static String ICYPos = "210";
    static String ResponsetoDiffFrame = "-1";
    static String TargetFrameName = "None";
    static String FacetPath = "None";
    static String ICFocus = "";
    static String ICSaveWarningFilter = "0";
    static String ICChanged = "0";
    static String ICResubmit = "0";
    static String ICSID = "";
    static String ICActionPrompt = "false";
    static String ICTypeAheadID = "";
    static String ICFind = "";
    static String ICAddCount = "";
    static String ICAPPCLSDATA = "";
    static String UG_SPL_CRCTN_FR_CHECKED = "";

    static String UG_SPL_CRCTN_FR_FIRST_NAME = "";
    static String UG_SPL_CRCTN_FR_MIDDLE_NAME = "";
    static String UG_SPL_CRCTN_FR_LAST_NAME = "";
    static String UG_SPL_CRCTN_FR_FIRST_NAME_CD = "";
    static String UG_SPL_CRCTN_FR_MIDDLE_NAME_TOEFL = "";
    static String UG_SPL_CRCTN_FR_LAST_NAME_CD = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdialog.setMessage("Fetching Terms...");
        pdialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            formData.clear();
            formData.put("ICAJAX", ICAJAX);
            formData.put("ICNAVTYPEDROPDOWN", ICNAVTYPEDROPDOWN);
            formData.put("ICType", ICType);
            formData.put("ICElementNum", ICElementNum);
            formData.put("ICStateNum", ICStateNum);
            formData.put("ICAction", ICActionBc);
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
            formData.put("UG_SPL_CRCTN_FR_CHECKED", UG_SPL_CRCTN_FR_CHECKED);

            Document sendSpelling = Jsoup.connect(spelling)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .get();
            if (UG_SPL_CRCTN_FR_CHECKED.equalsIgnoreCase("Y")){
                formData.put("UG_SPL_CRCTN_FR_FIRST_NAME", UG_SPL_CRCTN_FR_FIRST_NAME);
                formData.put("UG_SPL_CRCTN_FR_MIDDLE_NAME", UG_SPL_CRCTN_FR_MIDDLE_NAME);
                formData.put("UG_SPL_CRCTN_FR_LAST_NAME", UG_SPL_CRCTN_FR_LAST_NAME);
                formData.put("UG_SPL_CRCTN_FR_FIRST_NAME_CD", UG_SPL_CRCTN_FR_FIRST_NAME_CD);
                formData.put("UG_SPL_CRCTN_FR_MIDDLE_NAME_TOEFL", UG_SPL_CRCTN_FR_MIDDLE_NAME_TOEFL);
                formData.put("UG_SPL_CRCTN_FR_LAST_NAME_CD", UG_SPL_CRCTN_FR_LAST_NAME_CD);
            }
            formData.put("ICStateNum", "2");
            formData.put("ICAction", ICActionAc);

            Document sendSpelling2 = Jsoup.connect(spelling)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .get();

            String html2 = sendSpelling2.text()+"\n"+sendSpelling2.html();
            Document doc2 = Jsoup.parse(html2);

            Ename = doc2.select("span[id='UG_SPL_CRCTN_FR_NAME_DISPLAY_TXT']").first().text();
            Aname = doc2.select("span[id='UG_SPL_CRCTN_FR_NAME_FORMAL_TXT']").first().text();
            CID = doc2.select("span[id='UG_SPL_CRCTN_FR_CAMPUS_ID']").first().text();

        }catch (Exception e){
            System.out.println(e);
            exception = e;
        }

        return null;
    }
}