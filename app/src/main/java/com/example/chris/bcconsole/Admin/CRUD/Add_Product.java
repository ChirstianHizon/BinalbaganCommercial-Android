package com.example.chris.bcconsole.Admin.CRUD;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chris.bcconsole.AdminMainActivity;
import com.example.chris.bcconsole.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.chris.bcconsole.AdminMainActivity.url;

public class Add_Product extends AppCompatActivity {

    private Spinner spcategory;
    private Activity context = this;
    private List<String> spinnerArrayName =  new ArrayList<String>();
    private List<String> spinnerArrayId =  new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private Button btnselect;
    private Button btnsubmit;
    public static final int PICK_IMAGE = 1;
    private ImageView ivselect;


    private Calendar c = Calendar.getInstance();
    private int mYear = c.get(Calendar.YEAR);
    private int mMonth = c.get(Calendar.MONTH);
    private int mDay = c.get(Calendar.DAY_OF_MONTH);

    private Uri imageUri;
    private StorageReference storageRef;
    private EditText edtname;
    private EditText edtdesc;
    private EditText edtprice;
    private EditText edtoptimal;
    private EditText edtwarning;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

//       Firebase Initializations

        storageRef = FirebaseStorage.getInstance().getReference();

        setTitle("Add Product");

        spcategory = (Spinner)findViewById(R.id.sp_category);
        btnselect = (Button)findViewById(R.id.btn_select);
        btnsubmit = (Button)findViewById(R.id.btn_submit);
        ivselect = (ImageView)findViewById(R.id.iv_select);

        edtname = (EditText)findViewById(R.id.edt_name);
        edtdesc = (EditText)findViewById(R.id.edt_desc);
        edtprice = (EditText)findViewById(R.id.edt_price);
        edtoptimal = (EditText)findViewById(R.id.edt_optimal);
        edtwarning = (EditText)findViewById(R.id.edt_warning);

        getCategories();

        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArrayName);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcategory.setAdapter(adapter);

//        ------------------------------- SELECT IMAGE -------------------------------------------------//

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);

            }
        });


//        --------------------------------  SUBMIT DATA ---------------------------------------------//

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String  name = edtname.getText().toString(),
                        desc = edtdesc.getText().toString(),
                        price = edtprice.getText().toString(),
                        optimal = edtoptimal.getText().toString(),
                        warning = edtwarning.getText().toString(),
                        category = spinnerArrayId.get(spcategory.getSelectedItemPosition());
                if(Double.valueOf(edtprice.getText().toString()) < 0.01 ||
                        Double.valueOf(edtoptimal.getText().toString()) < 0.01 ||
                        Double.valueOf(edtwarning.getText().toString()) < 0.01){

                    Toast.makeText(context, "Level input Error", Toast.LENGTH_SHORT).show();

                }else{
                    if(Integer.valueOf(optimal) < Integer.valueOf(warning)){

                        Toast.makeText(context, "Optimal is Greater than Warning", Toast.LENGTH_SHORT).show();

                    }else{

                        btnselect.setEnabled(false);
                        btnsubmit.setEnabled(false);

                        progress = new ProgressDialog(context);
                        progress.setMessage("Uploading Data to Servers");
                        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progress.setIndeterminate(true);
                        progress.setProgress(0);
                        progress.setCancelable(false);
                        progress.show();

                        if(imageUri == null){
                            uploadtoServer(name,desc,price,category,optimal,warning,"");
                        }else{

                            StorageReference uploadTask = storageRef.child("products/"+imageUri.getLastPathSegment());
                            uploadTask.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get a URL to the uploaded content
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                                    Toast.makeText(context, "UPLOAD COMPLETE", Toast.LENGTH_SHORT).show();

                                            uploadtoServer(name,desc,price,category,optimal,warning, String.valueOf(downloadUrl));

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(context, "UPLOAD Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    int uploadProgress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                                    progress.setProgress(uploadProgress);
                                    Log.d("PROGRESS", String.valueOf(uploadProgress));
                                }
                            });
                        }

                    }
                }


                Log.d("PRODUCT_ADD",name +", "+desc+", "+price+", "+optimal+", "+warning+", "+category+", "+imageUri);

            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            if (data != null){
                try {
                    imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ivselect.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private void getCategories() {
        Toast.makeText(context, AdminMainActivity.url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CRUD ",response);
                        try {
                            JSONObject reader = new JSONObject(response);
                            int counter = Integer.valueOf(reader.getString("COUNTER"));

                            for (int x = 1; x <= counter;x++){
                                JSONObject category = new JSONObject(reader.getString(String.valueOf(x)));

                                spinnerArrayName.add(category.getString("NAME"));
                                spinnerArrayId.add(category.getString("ID"));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "17");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//  ------------------------------------- UPLOAD TO SERVER ----------------------------------------- //

    private void uploadtoServer(final String name, final String desc, final String price, final String category, final String optimal, final String warning, final String image) {
        Toast.makeText(context, AdminMainActivity.url, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CRUD_RESULT",response);

                        try {
                            JSONObject reader = new JSONObject(response);
                            if(reader.getBoolean("main")){
                                Toast.makeText(context, "Product Added", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Product Name Already Used", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        progress.cancel();
                        btnselect.setEnabled(true);
                        btnsubmit.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Unable to Connect to Server", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("access", "Binalbagan_Commercial_MOBILE_Access");
                params.put("type", "18");
                params.put("name", name);
                params.put("desc", desc);
                params.put("price", price);
                params.put("category", category);
                params.put("optimal", optimal);
                params.put("warning", warning);
                params.put("image", image);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

//                DatePickerDialog mDatePicker=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
//                        // TODO Auto-generated method stub
//
//                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//                    }
//                },mYear, mMonth, mDay);
//                mDatePicker.show();
