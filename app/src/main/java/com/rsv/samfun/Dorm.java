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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dorm extends Fragment {

    String myValue, uname, pword;
    TextView name , id , FromDate, Status, Campus, DormID, Room, Bed, AssD1, Quality, AssC1, Comment, AssD2, AssC2, ReturnDate;
    public HashMap<String, String> cookies = new HashMap<>();
    View rootView;

    public Dorm() {
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
        rootView = inflater.inflate(R.layout.content_dorm, container, false);
        name = rootView.findViewById(R.id.DORMsname);
        id = rootView.findViewById(R.id.DORMsid);
        FromDate = rootView.findViewById(R.id.DORMtextView8);
        Status = rootView.findViewById(R.id.DORMtextView9);
        Campus = rootView.findViewById(R.id.DORMtextView15);
        DormID = rootView.findViewById(R.id.DORMtextView16);
        Room = rootView.findViewById(R.id.DORMtext3);
        Bed = rootView.findViewById(R.id.DORMtext4);
        AssD1 = rootView.findViewById(R.id.DORMtext8);
        Quality = rootView.findViewById(R.id.DORMtext9);
        AssC1 = rootView.findViewById(R.id.DORMtext15);
        Comment = rootView.findViewById(R.id.DORMtext16);
        AssD2 = rootView.findViewById(R.id.DORMtxt4);
        AssC2 = rootView.findViewById(R.id.DORMtxt5);
        ReturnDate = rootView.findViewById(R.id.DORMtxt6);

        String array[] = myValue.split(" ");
        name.setText(array[0]+" "+array[1]);
        id.setText(array[3]);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dorm2 D2 = new Dorm2();
        D2.pdialog = new ProgressDialog(getActivity());
        D2.context = getActivity();
        D2.uname = uname;
        D2.pword = pword;
        try{
            D2.execute().get();
            updateTables(D2);
        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void updateTables(Dorm2 D2){
        try {

            FromDate.setText("");
            for (String row : D2.FromDate) {
                FromDate.append(row + "\n");
            }

            Status.setText("");
            for (String row : D2.Status) {
                Status.append(row + "\n");
            }

            Campus.setText("");
            for (String row : D2.Campus) {
                Campus.append(row + "\n");
            }

            DormID.setText("");
            for (String row : D2.DormID) {
                DormID.append(row + "\n");
            }

            Room.setText("");
            for (String row : D2.Room) {
                Room.append(row + "\n");
            }

            Bed.setText("");
            for (String row : D2.Bed) {
                Bed.append(row + "\n");
            }

            AssD1.setText("");
            for (String row : D2.AssD1) {
                AssD1.append(row + "\n");
            }

            Quality.setText("");
            for (String row : D2.Quality) {
                Quality.append(row + "\n");
            }

            AssC1.setText("");
            for (String row : D2.AssC1) {
                AssC1.append(row + "\n");
            }

            Comment.setText("");
            for (String row : D2.Comment) {
                Comment.append(row + "\n");
            }

            AssD2.setText("");
            for (String row : D2.AssD2) {
                AssD2.append(row + "\n");
            }

            //AssC2.setText("");
            for (String row : D2.AssC2) {
                AssC2.append(row + "\n");
            }

            ReturnDate.setText("");
            for (String row : D2.ReturnDate) {
                ReturnDate.append(row + "\n");
            }

        }catch (Exception e){
            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG);
            System.err.println(e);
        }

    }


}

class Dorm2 extends AsyncTask<Void, Void, Void>{

    final String tzone = "-180";
    final String loginFormUrl = "http://10.139.8.225/psp/SIS/?cmd=login";
    final String loginActionUrl = "http://10.139.8.225/psp/SIS/?cmd=login&amp;languageCd=ENG";
    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36";
    final static String viewDorm= "http://10.139.8.225/psc/SIS/EMPLOYEE/HRMS/c/U_DORM_MN.UG_DRM_SRVC_STDNT.GBL";
    public HashMap<String, String> cookies = new HashMap<>();
    public HashMap<String, String> formData = new HashMap<>();
    public String uname, pword;
    List<String> FromDate = new ArrayList<>();
    List<String> Status = new ArrayList<>();
    List<String> Campus = new ArrayList<>();
    List<String> DormID = new ArrayList<>();
    List<String> Room = new ArrayList<>();
    List<String> Bed = new ArrayList<>();
    List<String> AssD1 = new ArrayList<>();
    List<String> Quality = new ArrayList<>();
    List<String> AssC1 = new ArrayList<>();
    List<String> Comment = new ArrayList<>();
    List<String> AssD2 = new ArrayList<>();
    List<String> AssC2 = new ArrayList<>();
    List<String> ReturnDate = new ArrayList<>();
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

            Document getDorm = Jsoup.connect(viewDorm)
                    .userAgent(USER_AGENT)
                    .cookies(cookies)
                    .followRedirects(true)
                    .get();
            //System.out.println(getDorm);
            String html = getDorm.text()+"\n"+getDorm.html();
            Document doc = Jsoup.parse(html);

            FromDate = doc.select("span[id*='U_HSTL_RM_AL_DL_EFFDT$']").eachText();
            /*for (String row : FromDate) {
                System.out.println(row);
            }*/

            Status = doc.select("span[id*='U_HSTL_RM_AL_DL_EFF_STATUS$']").eachText();

            Campus = doc.select("span[id*='U_CAMPUS_VW_DESCR$']").eachText();

            DormID = doc.select("span[id*='U_HOSTEL_ID_VW_DESCR$']").eachText();

            Room = doc.select("span[id*='U_HSTL_RM_AL_DL_U_ROOM_NBR$']").eachText();

            Bed = doc.select("span[id*='U_HSTL_RM_AL_DL_U_RM_BEDNO$']").eachText();



            AssD1 = doc.select("span[id*='U_ASSET_SHRD_VW_DESCR$']").eachText();

            Quality = doc.select("span[id*='U_HSTL_RM_ASSET_QUANTITY$']").eachText();

            AssC1 = doc.select("span[id*='U_HSTL_RM_ASSET_U_ASSET_COND$']").eachText();

            Comment = doc.select("span[id*='U_HSTL_RM_ASSET_COMMENTS$']").eachText();





            AssD2 = doc.select("span[id*='U_ASSET_INDV_VW_DESCR$']").eachText();

            AssC2 = doc.select("span[id*='U_STDNT_ASST_DL_U_ASSET_COND$']").eachText();

            ReturnDate = doc.select("span[id*='U_STDNT_ASST_DL_RETURN_DT$']").eachText();


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
