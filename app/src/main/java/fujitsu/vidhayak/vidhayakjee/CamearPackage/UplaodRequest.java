package fujitsu.vidhayak.vidhayakjee.CamearPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fujitsu.vidhayak.vidhayakjee.R;
import fujitsu.vidhayak.vidhayakjee.SaveUserId;
import fujitsu.vidhayak.vidhayakjee.Urls;

public class UplaodRequest extends AppCompatActivity implements View.OnClickListener {

    EditText mrequesttitle,mrequestdewscrition;
    Button mchooseimagebtn,muploadrequstbtn;

    ImageView mrequstimage;
    private Bitmap bitmap;
    AlertDialog dialog;
    String strtim;

    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_REQUEST = 2;

    public static final String KEY_ID= "user_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "content";
    public static final String KEY_IMAGE = "image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uplaod_request);

        mrequesttitle = (EditText) findViewById(R.id.request_title);
        mrequestdewscrition = (EditText) findViewById(R.id.request_description);

        mchooseimagebtn = (Button) findViewById(R.id.chooseimage_btn);
        muploadrequstbtn = (Button) findViewById(R.id.uploadrequst_btn);

        mrequstimage = (ImageView) findViewById(R.id.requst_Image);

        mchooseimagebtn.setOnClickListener(this);
        muploadrequstbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        if(v == mchooseimagebtn){

            AlertDialog.Builder mbuilder = new AlertDialog.Builder(UplaodRequest.this);
            View mview = getLayoutInflater().inflate(R.layout.chooseimage, null);
            Button mtakephoto = (Button) mview.findViewById(R.id.imagebycamera);
            mtakephoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent cameraIntent = new Intent(UplaodRequest.this,CameraActivity.class);
                    cameraIntent.putExtra(GlobalVariables.FILENAME,GlobalVariables.profilepic_name);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);


                }
            });

            Button mtakegallery = (Button) mview.findViewById(R.id.imagebygallery);
            mtakegallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showFileChooser();

                }
            });
            mbuilder.setView(mview);
            dialog = mbuilder.create();
            dialog.show();
        }

        if(v == muploadrequstbtn){
                uploadImage();
        }

    }

    private void showFileChooser() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= 23) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);


            } else {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, ""), PICK_IMAGE_REQUEST);

            }
        }catch (Exception e){

            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {


            final String userid = String.valueOf(SaveUserId.getInstance(UplaodRequest.this).getUserId());
            final String title = mrequesttitle.getText().toString().trim();
            final String description = mrequestdewscrition.getText().toString().trim();
            final String image = getStringImage(bitmap);


            String url = null;
            String REGISTER_URL = Urls.uploadrequest;

            REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
            try {
                URL sourceUrl = new URL(REGISTER_URL);
                url = sourceUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            final ProgressDialog loading = ProgressDialog.show(UplaodRequest.this, "Uploading...", "Please wait...", false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("jaba", userid);
                            try {
                                JSONObject jsonresponse = new JSONObject(response);
                                boolean success = jsonresponse.getBoolean("success");

                                if (success) {

                                    UplaodRequest.this.finish();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UplaodRequest.this);
                                    builder.setMessage("Upoading Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                          //  Log.d("jabadi", headline);
                            loading.dismiss();
                            Toast.makeText(UplaodRequest.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("bada123", userid);

                            loading.dismiss();
                            Toast.makeText(UplaodRequest.this, error.toString(), Toast.LENGTH_LONG).show();
                            Log.d("error1234", error.toString());

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(KEY_ID, userid);
                    params.put(KEY_TITLE, title);
                    params.put(KEY_DESCRIPTION, description);
                    params.put(KEY_IMAGE, image);
                    return params;

                }

            };
            // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            stringRequest.setRetryPolicy(
                    new DefaultRetryPolicy(
                            500000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    )
            );


            RequestQueue requestQueue = Volley.newRequestQueue(UplaodRequest.this);
            requestQueue.add(stringRequest);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
           // Log.d("try4",str);
            super.onActivityResult(requestCode, resultCode, data);}catch (Exception e) {
            Log.d("try8", e.toString());
         //   Toast.makeText(getContext(), "On super " + e.toString(), Toast.LENGTH_LONG).show();

        }


//
//        if (requestCode >= PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//
//            Uri filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
//                mnewsimage.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//        }

        if(requestCode==PICK_IMAGE_REQUEST) {

            if(data==null){

                Toast.makeText(UplaodRequest.this," Please Select Image For Uploading.... ",Toast.LENGTH_LONG).show();

            }else {
                Uri filePath = data.getData();
                Intent intentcrop = new Intent(UplaodRequest.this, CropImage.class);
                intentcrop.putExtra("ramji", filePath.toString());
                startActivityForResult(intentcrop, 6);
            }
        }


//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            bitmap = (Bitmap) data.getExtras().get("data");
//            mnewsimage.setImageBitmap(bitmap);
//        }

        if(requestCode ==CAMERA_REQUEST ){
//            if(data.getExtras()==null){
//
//                Toast.makeText(getContext()," Please Take Image For Uploading.... ",Toast.LENGTH_LONG).show();
//
//            }else {
//                Bitmap bitmapcamear = (Bitmap) data.getExtras().get("data");
//                String bitstring = getStringImage(bitmapcamear);
//                boolean checktr = true;
//                Intent intentcrop = new Intent(getContext(), CropImage.class);
//                intentcrop.putExtra("cameraji", bitstring);
//                intentcrop.putExtra("camerajiboolean", checktr);
//                startActivityForResult(intentcrop, PICK_CROPIMAGE);
//            }
            bitmap =  UtilityClass.getImage(GlobalVariables.profilepic_name);
            mrequstimage.setImageBitmap(bitmap);
            dialog.dismiss();


        }

        if(requestCode==6)
        {
            strtim = data.getStringExtra("cropimage");
            Log.d("imageindash","imageindd "+strtim);
            bitmap = StringToBitMap(strtim);
            Log.d("imageinbitmap","imageinbit "+bitmap);
            mrequstimage.setImageBitmap(bitmap);
            dialog.dismiss();

        }

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
