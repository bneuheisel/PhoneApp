package com.example.project4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.net.Proxy.Type.HTTP;

public class NewPersonForm extends AppCompatActivity {

    Button btn_saveContact, btn_returnToList, btn_takePhoto, btn_choosePhoto;
    Button btn_web, btn_email, btn_call, btn_text, btn_map;
    EditText et_name, et_address, et_phoneNumber, et_email, et_url;
    ImageView iv_personIcon;

    int positionToEdit = -1;
    //Photo stuff:
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int SELECT_A_PHOTO = 2;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_new_person_form );
        //Contact info:
        btn_saveContact = findViewById(R.id.btn_saveContact);
        btn_returnToList = findViewById(R.id.btn_returnToList);
        et_name = findViewById(R.id.et_name);
        et_address = findViewById(R.id.et_address);
        et_phoneNumber = findViewById(R.id.et_phoneNumber);
        et_email = findViewById(R.id.et_email);
        et_url = findViewById(R.id.et_url);
        //Photo stuff:
        btn_takePhoto = findViewById(R.id.btn_takePhoto);
        btn_choosePhoto = findViewById(R.id.btn_choosePhoto);
        iv_personIcon = findViewById(R.id.iv_personIcon);

        //Common intents
        btn_web = findViewById(R.id.btn_web);
        btn_email = findViewById(R.id.btn_email);
        btn_call = findViewById(R.id.btn_call);
        btn_text = findViewById(R.id.btn_text);
        btn_map = findViewById(R.id.btn_map);


        //listen for incoming data
        Bundle incomingIntent = getIntent().getExtras();

        if(incomingIntent != null){
            String name = incomingIntent.getString("name");
            String address = incomingIntent.getString("address");
            String phone = incomingIntent.getString("phone");
            String email = incomingIntent.getString("email");
            String url = incomingIntent.getString("url");
            positionToEdit = incomingIntent.getInt("edit");

            //Fill in the form
            et_name.setText(name);
            et_address.setText(address);
            et_phoneNumber.setText(phone);
            et_email.setText(email);
            et_url.setText(url);
        }

        btn_saveContact.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get strings from et_
                String newName = et_name.getText().toString();
                String newAddress = et_address.getText().toString();
                String newPhoneNumber = et_phoneNumber.getText().toString();
                String newEmail = et_email.getText().toString();
                String newUrl = et_url.getText().toString();

                //put strings into a message for Main Activity

                //start main again

                Intent i = new Intent(v.getContext(), MainActivity.class);

                i.putExtra("edit", positionToEdit);
                i.putExtra("name", newName);
                i.putExtra("address", newAddress);
                i.putExtra("phone", newPhoneNumber);
                i.putExtra("email", newEmail);
                i.putExtra("url", newUrl);

                startActivity(i);
            }
        } );

        btn_returnToList.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        } );
        btn_takePhoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        } );
        btn_choosePhoto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an intent to select a photo from gallery
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //start the intent with a request code
                startActivityForResult( i, SELECT_A_PHOTO );
            }
        } );
        btn_web.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebPage( et_url.getText().toString() );
            }
        } );
        btn_email.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String [] addresses = new String [1];
                addresses[0] = et_email.getText().toString();
                composeEmail(addresses, "Hello");
            }
        } );
        btn_call.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhoneNumber(et_phoneNumber.getText().toString());
            }
        } );
        btn_text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeMmsMessage(et_phoneNumber.getText().toString(), "Hello");
            }
        } );
        btn_map.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mapQuery = "geo:0,0?q=" + et_address.getText().toString();
                Uri mapuri = Uri.parse( mapQuery );
                showMap( mapuri );
            }
        } );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView iv_personIcon;
        iv_personIcon = findViewById(R.id.iv_personIcon);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //set the image in iv_personIcon
            Glide.with(this).load( currentPhotoPath ).into(iv_personIcon);
        }
        if (requestCode == SELECT_A_PHOTO && resultCode == RESULT_OK){
            Uri selectedPhoto = data.getData();

            Glide.with(this).load( selectedPhoto ).into(iv_personIcon);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText( this, "Could not save file.", Toast.LENGTH_SHORT ).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.project4.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void openWebPage(String url){
        if(!url.startsWith("http://") || !url.startsWith("https://")){
            url = "http://" + url;
        }
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeMmsMessage(String phoneNumber, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phoneNumber));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
