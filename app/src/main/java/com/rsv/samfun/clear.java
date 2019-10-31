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


public class clear extends Fragment {

    Button rqt_btn;
    String myValue, uname, pword, ICSID;
    TextView name , id, Acy, Term, Acp, Acg, efDate, Ap1,Ap2,Ap3,Ap4,Ap5,Ap6,Apr1,Apr2,Apr3,Apr4,Apr5,Apr6;
    public HashMap<String, String> cookies = new HashMap<>();
    View rootView;

    public clear() {
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
        rootView = inflater.inflate(R.layout.content_clear, container, false);
        rqt_btn = rootView.findViewById(R.id.CLEARbutton6);
        name = rootView.findViewById(R.id.CLEARsname);
        id = rootView.findViewById(R.id.CLEARsid);
        Acy = rootView.findViewById(R.id.CLEARtextView34);
        Term = rootView.findViewById(R.id.CLEARtextView41);
        Acg = rootView.findViewById(R.id.CLEARtextView6);
        Acp = rootView.findViewById(R.id.CLEARtextView10);
        efDate = rootView.findViewById(R.id.CLEARtextView43);
        Apr1 = rootView.findViewById(R.id.CLEARtextView48);
        Apr2 = rootView.findViewById(R.id.CLEARtextView50);
        Apr3 = rootView.findViewById(R.id.CLEARtextView52);
        Apr4 = rootView.findViewById(R.id.CLEARtextView54);
        Apr5 = rootView.findViewById(R.id.CLEARtextView56);
        Apr6 = rootView.findViewById(R.id.CLEARtextView58);
        Ap1 = rootView.findViewById(R.id.CLEARtextView49);
        Ap2 = rootView.findViewById(R.id.CLEARtextView51);
        Ap3 = rootView.findViewById(R.id.CLEARtextView53);
        Ap4 = rootView.findViewById(R.id.CLEARtextView55);
        Ap5 = rootView.findViewById(R.id.CLEARtextView57);
        Ap6 = rootView.findViewById(R.id.CLEARtextView59);

        String array[] = myValue.split(" ");
        name.setText(array[0]+" "+array[1]);
        id.setText(array[3]);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final clear2 c2 = new clear2();
        c2.pdialog = new ProgressDialog(getActivity());
        c2.context = getActivity();
        c2.uname = uname;
        c2.pword = pword;
        try{
            c2.execute().get();
            updateTables(c2);

            cookies.clear();
            cookies.putAll(c2.cookies);
            ICSID = c2.ICSID;
        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        rqt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear3 c3 = new clear3();
                try {
                    c3.ICSID = ICSID;
                    c3.cookies = cookies;
                    c3.pdialog = new ProgressDialog(getActivity());
                    c3.context = getActivity();
                    c3.execute().get();
                    System.out.println("after exceute");

                    updateTables2(c3);

                }catch(Exception e){
                    System.out.println(e);
                    Toast.makeText(getActivity(),e.getMessage()+" onclick listener",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void updateTables(clear2 c2){
        try {
            if (c2.rqt_btn == 1){
                rqt_btn.setEnabled(false);
                rqt_btn.setBackgroundResource(R.color.darkgray);
            }else if (c2.rqt_btn == 0){
                rqt_btn.setEnabled(true);
                rqt_btn.setBackgroundResource(R.drawable.btn_bg);
            }
            Acy.setText(c2.Acy);
            Term.setText(c2.Term);
            Acg.setText(c2.Acg);
            Acp.setText(c2.Acp);
            efDate.setText(c2.efDate);

            Apr1.setText(c2.Approver.get(0));
            Apr2.setText(c2.Approver.get(1));
            Apr3.setText(c2.Approver.get(2));
            Apr4.setText(c2.Approver.get(3));
            Apr5.setText(c2.Approver.get(4));
            Apr6.setText(c2.Approver.get(5));

            Ap1.setText(c2.Approve.get(0));
            Ap2.setText(c2.Approve.get(1));
            Ap3.setText(c2.Approve.get(2));
            Ap4.setText(c2.Approve.get(3));
            Ap5.setText(c2.Approve.get(4));
            Ap6.setText(c2.Approve.get(5));

        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void updateTables2(clear3 c3){
        try {

            if (c3.rqt_btn == 1){
                rqt_btn.setEnabled(false);
                rqt_btn.setBackgroundResource(R.color.darkgray);
            }else if (c3.rqt_btn == 0){
                rqt_btn.setEnabled(true);
                rqt_btn.setBackgroundResource(R.drawable.btn_bg);
            }
            Acy.setText(c3.Acy);
            Term.setText(c3.Term);
            Acg.setText(c3.Acg);
            Acp.setText(c3.Acp);
            efDate.setText(c3.efDate);

            Apr1.setText(c3.Approver.get(0));
            Apr2.setText(c3.Approver.get(1));
            Apr3.setText(c3.Approver.get(2));
            Apr4.setText(c3.Approver.get(3));
            Apr5.setText(c3.Approver.get(4));
            Apr6.setText(c3.Approver.get(5));

            Ap1.setText(c3.Approve.get(0));
            Ap2.setText(c3.Approve.get(1));
            Ap3.setText(c3.Approve.get(2));
            Ap4.setText(c3.Approve.get(3));
            Ap5.setText(c3.Approve.get(4));
            Ap6.setText(c3.Approve.get(5));

        }catch (Exception e){
            System.err.println(e);
            Toast.makeText(getActivity(),e.getMessage()+" updatetable2",Toast.LENGTH_LONG).show();
        }
    }

}

class clear2 extends AsyncTask<Void, Void, Void>{

    final static String viewclear = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/U_SR_MENU.U_CLEARANCE_SS.GBL";
    final String tzone = "-180";
    final String loginFormUrl = "http://10.139.8.225/psp/SIS/?cmd=login";
    final String loginActionUrl = "http://10.139.8.225/psp/SIS/?cmd=login&amp;languageCd=ENG";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    public String uname, pword, Acy, Term, Acp, Acg, efDate;
    List<String> Approver  = new ArrayList<>();
    List<String> Approve  = new ArrayList<>();
    String ICSID;
    int rqt_btn;
    Exception exception;
    ProgressDialog pdialog;
    Context context;

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


            Document getClear = Jsoup.connect(viewclear)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();
            System.out.println(getClear);
            Element IC = getClear.selectFirst("input#ICSID");
            ICSID = IC.attr("value");

            String html = getClear.text()+"\n"+getClear.html();
            Document doc = Jsoup.parse(html);

            Approver = doc.select("span[id*='PSROLEDEFN_DESCRLONG$']").eachText();
            /*for (String row : Approver) {
                System.out.println(row);
            }*/

            Approve = doc.select("span[id*='U_CLEAR_SS_TBL_U_APPROVED_2$']").eachText();
            /*for (String row : Approve) {
                System.out.println(row);
            }*/

            Acy = doc.select("span[id='U_ID_CLER_WRK_ACAD_YEAR']").first().text();
            //System.out.println(Acy);
            Term = doc.select("span[id='U_ID_CLER_WRK_DESCR1_GER']").first().text();
            //System.out.println(Term);
            Acp = doc.select("span[id='U_ID_CLER_WRK_DESCR1']").first().text();
            //System.out.println(Acp);
            Acg = doc.select("span[id='U_ID_CLER_WRK_DESCR']").first().text();
            //System.out.println(Acg);
            efDate = doc.select("span[id*='U_CLEARANCE_TBL_EFFDT$']").last().text();
            //System.out.println(efDate);

            Element rqtbtn = doc.selectFirst("input[id='U_ID_CLER_WRK_BUTTON$0']");
            Element addrow = doc.selectFirst("img[name='U_ID_REP_SS_TBL$new$0$$img$0']");
            if (rqtbtn.hasAttr("disabled") && addrow == null) {
                rqt_btn = 1;
            }else {
                rqt_btn = 0;
            }

        }catch (Exception e) {
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

class clear3 extends AsyncTask<Void, Void, Void>{
    final static String viewclear = "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/U_SR_MENU.U_CLEARANCE_SS.GBL";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    public String Acy, Term, Acp, Acg, efDate;
    List<String> Approver  = new ArrayList<>();
    List<String> Approve  = new ArrayList<>();
    String ICSID;
    int rqt_btn;
    Exception exception;
    ProgressDialog pdialog;
    Context context;

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
            formData.put("ICAJAX","1");
            formData.put("ICNAVTYPEDROPDOWN","0");
            formData.put("ICType","Panel");
            formData.put("ICElementNum", "0");
            formData.put("ICStateNum","1");
            formData.put("ICAction", "U_ID_REP_SS_TBL$new$0$$0");
            formData.put("ICXPos", "0");
            formData.put("ICYPos","0");
            formData.put("ResponsetoDiffFrame","-1");
            formData.put("TargetFrameName","None");
            formData.put("FacetPath","None");
            formData.put("ICFocus","");
            formData.put("ICSaveWarningFilter","0");
            formData.put("ICChanged", "1");
            formData.put("ICResubmit","0");
            formData.put("ICSID", ICSID);
            formData.put("ICActionPrompt", "false");
            formData.put("ICTypeAheadID","");
            formData.put("ICFind","");
            formData.put("ICAddCount","");
            formData.put("ICAPPCLSDATA","");



            Document getClea = Jsoup.connect(viewclear)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();
            System.out.println(getClea);

            Document getClear = Jsoup.connect(viewclear)
                    .userAgent(USER_AGENT)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();

            formData.put("ICStateNum","2");
            formData.put("ICAction", "U_ID_CLER_WRK_BUTTON$0");

            Document getClear2 = Jsoup.connect(viewclear)
                    .userAgent(USER_AGENT)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();

            formData.put("ICStateNum","3");
            formData.put("ICAction", "#ICYes");

            Document getClear3 = Jsoup.connect(viewclear)
                    .userAgent(USER_AGENT)
                    .data(formData)
                    .method(Connection.Method.POST)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();

            //System.out.println(getClear3);
            String html = getClear3.text()+"\n"+getClear3.html();
            Document doc = Jsoup.parse(html);

            Approver = doc.select("span[id*='PSROLEDEFN_DESCRLONG$']").eachText();
            /*for (String row : Approver) {
                System.out.println(row);
            }*/

            Approve = doc.select("span[id*='U_CLEAR_SS_TBL_U_APPROVED_2$']").eachText();
            /*for (String row : Approve) {
                System.out.println(row);
            }*/

            Acy = doc.select("span[id='U_ID_CLER_WRK_ACAD_YEAR']").first().text();
            //System.out.println(Acy);
            Term = doc.select("span[id='U_ID_CLER_WRK_DESCR1_GER']").first().text();
            //System.out.println(Term);
            Acp = doc.select("span[id='U_ID_CLER_WRK_DESCR1']").first().text();
            //System.out.println(Acp);
            Acg = doc.select("span[id='U_ID_CLER_WRK_DESCR']").first().text();
            //System.out.println(Acg);
            efDate = doc.select("span[id*='U_CLEARANCE_TBL_EFFDT$']").last().text();
            //System.out.println(efDate);

            Element rqtbtn = doc.selectFirst("input[id='U_ID_CLER_WRK_BUTTON$0']");
            if (rqtbtn.hasAttr("disabled")) {
                rqt_btn = 1;
            }else {
                rqt_btn = 0;
            }

        }catch (Exception e) {
            System.err.println(e+" In clear3 background");
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
            makeToast(context, finalMsg+" clear3 onpost", Toast.LENGTH_SHORT);
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
