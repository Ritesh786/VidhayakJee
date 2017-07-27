package fujitsu.vidhayak.vidhayakjee.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fujitsu.vidhayak.vidhayakjee.FetchingPackage.AppController;
import fujitsu.vidhayak.vidhayakjee.FetchingPackage.Movie;
import fujitsu.vidhayak.vidhayakjee.R;
import fujitsu.vidhayak.vidhayakjee.SaveUserId;

/**
 * A simple {@link Fragment} subclass.
 */
public class VidhayakLocation extends Fragment implements View.OnClickListener {


    String lat,lang,place,time;
    Button mopenmapbtn;
    private ProgressDialog pDialog;
    TextView maddresstxt,mtimetxt;

    public VidhayakLocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_vidhayak_location, container, false);
        pDialog = new ProgressDialog(getContext());
        mopenmapbtn = (Button) view.findViewById(R.id.openmap_btn);
        maddresstxt = (TextView) view.findViewById(R.id.addresstxt);
        mtimetxt =  (TextView) view.findViewById(R.id.timetxt);
        mopenmapbtn.setOnClickListener(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        Getaddress();



      //  GoogleMap googleMap = ((MapView) view.findViewById(R.id.YOURMAPID)).getm

        return  view;
    }

    public void Getaddress(){

        final String url = "http://minews.in/lumen/public/location/fetch";

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hidePDialog();
                        JSONObject jobject = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            jobject = new JSONObject(response);

                            lat =  jobject.getString("lats");
                            lang = jobject.getString("longs");
                            place =  jobject.getString("place");
                            time =  jobject.getString("updated_at");

                            Log.d("nnnono","omonon"+lat+place+time);

                            maddresstxt.setText(place);
                            mtimetxt.setText(time);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onClick(View v) {

        Intent mapintent = new Intent(getContext(),MapsActivity.class);
        mapintent.putExtra("lat",lat);
        mapintent.putExtra("long",lang);
        startActivity(mapintent);

    }
}
