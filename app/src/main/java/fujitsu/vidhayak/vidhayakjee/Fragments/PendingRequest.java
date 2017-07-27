package fujitsu.vidhayak.vidhayakjee.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fujitsu.vidhayak.vidhayakjee.FetchingPackage.AppController;
import fujitsu.vidhayak.vidhayakjee.FetchingPackage.Movie;
import fujitsu.vidhayak.vidhayakjee.FetchingPackage.RecycleAdapter;
import fujitsu.vidhayak.vidhayakjee.R;
import fujitsu.vidhayak.vidhayakjee.SaveUserId;
import fujitsu.vidhayak.vidhayakjee.Urls;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingRequest extends Fragment {


    private static final String TAG = PendingRequest.class.getSimpleName();

    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();

    private RecyclerView recyclerView;

    private RecycleAdapter adapter;
    String strtext;
    Movie movie;

    public PendingRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_request, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        adapter = new RecycleAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        recyclerView.addOnItemTouchListener(
//                new RecyclerTouchListener(getContext(), new RecyclerTouchListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//
//                        Movie mo123 = movieList.get(position);
//
//                        Intent newsdetailintnt = new Intent(getContext(),NewsDetailShow.class);
//                        newsdetailintnt.putExtra("type",mo123.getYear());
//                        newsdetailintnt.putExtra("headline",mo123.getTitle());
//                        newsdetailintnt.putExtra("content",mo123.getRating());
//                        newsdetailintnt.putExtra("image",mo123.getThumbnailUrl());
//                        startActivity(newsdetailintnt);
//
//
//                        // TODO Handle item click
//                    }
//                })
//        );


        pDialog = new ProgressDialog(getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
//        getActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj

        populatedata();

        return view;

    }

    public void populatedata(){

        final String url = Urls.pendingrequest;
          strtext =  SaveUserId.getInstance(getContext()).getUserId();
        String newurl = url+strtext;


        JsonArrayRequest movieReq = new JsonArrayRequest(newurl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);

                                Movie movie = new Movie();

                                String imagestr = obj.getString("image");
                                String imagrurl = "https://s3.ap-south-1.amazonaws.com/vidhayak-storage/images/posts/";
                                String imageurlfull = imagrurl+imagestr;

                                movie.setTitle(obj.getString("title"));
                                movie.setThumbnailUrl(imageurlfull);
                                movie.setRating(obj.getString("content"));

                                movie.setYear("Request");
                                movie.setGenre(obj.getString("updated_at"));
                                movie.setId(obj.getString("id"));

                                movieList.add(movie);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

        if(movieList!=null) movieList.clear();

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


}
