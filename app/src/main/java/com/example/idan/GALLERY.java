package com.example.idan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GALLERY extends AppCompatActivity {
    ImageView imageUpload;
    Button btnup, btndl;
    private static final int RESOLT_LOAD_IMAGE =1;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery2);

        setContentView(R.layout.activity_gallery);

        imageUpload=(ImageView) findViewById(R.id.imvigallery);

        btnup =(Button) findViewById(R.id.btngalleryupload);
        btndl = (Button) findViewById(R.id.btngallerydownload);


        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        btndl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadimage();
            }
        });
    }

    private void downloadimage() {

        // final File localFile = File.createTempFile("images", "jpg");

        islandRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        try {
                            Bitmap  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageUpload.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(Gallery.this, "got image", Toast.LENGTH_SHORT).show();
                        }}})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(Gallery.this, "failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void uploadImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"Select Picture"), RESOLT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESOLT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            fileUri = data.getData();
            if (fileUri != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                islandRef.putFile(fileUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(Gallery.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Gallery.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }
            else
                Toast.makeText(Gallery.this, "choose an image", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st=item.getTitle().toString();
        if(st.equals("update")){
            Intent a=new Intent(this, Update.class);
            startActivity(a);
        }
        if(st.equals("register")){
            Intent a=new Intent(this, MainActivity.class);
            startActivity(a);
        }
        return super.onOptionsItemSelected(item);
    }
    }
}
