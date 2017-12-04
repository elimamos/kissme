package com.example.elisa.kissmekate;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.IOException;



public class GetUserLips extends AppCompatActivity {
    static final int CAM_REQUEST=101;

    int MY_REQUEST_CODE = 1;
    int MY_REQUEST_CODE2 = 2;
    int MY_REQUEST_CODE3 = 3;
    String fileName;
    File image_file;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_lips);
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE2);
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE3);
        }
      //  capture();
        dispatchTakePictureIntent();

      //a  facedetection();
    }

    private File getFile(){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        final String TAG = "MainActivity";

        File folder =  new File (path, getResources().getString( R.string.app_name));
        boolean success = false;
        if(!folder.exists()){
            success=folder.mkdirs();
            success=true;
        }
        if (!success){
            Toast.makeText(this, "Nie udalo sie stworzyc folderu!",Toast.LENGTH_SHORT);
            Log.d(TAG,"Folder not created.");
        }
        else{
            Log.d(TAG,"Folder created!");
        }
  //      Intent intent = getIntent();
        fileName ="testowe";//intent.getExtras().getString("przedmiotKod");

        image_file = new File(folder, fileName+".jpg");

        return image_file;
    }

    public void capture() {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getFile();
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(camera_intent,CAM_REQUEST);


    }
    private static final String TAG = "PhotoViewerActivity";
    Bitmap bitmap;
    FaceView overlay;
    public void facedetection(){
        /* bitmap = null;
       // ImageView view = (ImageView) findViewById(R.id.view);
      //  File f = new File(_path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(image_file), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
      //  view.setImageBitmap(bitmap);

        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector. ACCURATE_MODE)
                .build();

        // This is a temporary workaround for a bug in the face detector with respect to operating
        // on very small images.  This will be fixed in a future release.  But in the near term, use
        // of the SafeFaceDetector class will patch the issue.
        Detector<Face> safeDetector = new SafeFaceDetector(detector);

        // Create a frame from the bitmap and run face detection on the frame.
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = safeDetector.detect(frame);

        if (!safeDetector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }
        ExifInterface ei = null;
        try {
            ei = new ExifInterface( Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString()+"/KissMeKate/"+fileName+".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;

        }
        overlay = (FaceView) findViewById(R.id.faceView);
        overlay.setContent(rotatedBitmap, faces);

        // Although detector may be used multiple times for different images, it should be released
        // when it is no longer needed in order to free native resources.
        safeDetector.release();
        TextView tv = (TextView) findViewById(R.id.bottom);

    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAM_REQUEST);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAM_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
          bitmap = (Bitmap) extras.get("data");
            facedetection();

        }
    }
    public static Bitmap createContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }



    public void showme(View view) {
       // FaceView fv= new FaceView(this ,null);
        /*  TextView tv = (TextView) findViewById(R.id.bottom);

        tv.setText("długość:"+Double.toString(overlay.getMyLenths()[0])+" lewy do środka:"+Double.toString(overlay.getMyLenths()[1])+" prawy do środka:"+Double.toString(overlay.getMyLenths()[2])+" środek nos:"+Double.toString(overlay.getMyLenths()[3])+" lewo nosL"+Double.toString(overlay.getMyLenths()[4])+" prawo nos:"+Double.toString(overlay.getMyLenths()[5]));
*/
       // TextView  ratio    = (TextView) findViewById(R.id.ratio);
        double[]numbers=overlay.getMyRelations();
        String message = overlay.getMyMessage();
        /*String text="";
        for(double i:numbers){
            text+=Double.toString(i)+"   ";
        }*/
        if(message.equals("")){
            Intent data = this.getIntent();

            data.putExtra("numbers",numbers);
            setResult(RESULT_OK, data);
            finish();
        }else {
            TextView tv = (TextView) findViewById(R.id.messageBox);
            tv.setText(message);
        }

      //  ratio.setText(text);

    }

    public void retry(View view) {
        recreate();
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(image_file.exists()){
            setResult(Activity.RESULT_OK,
                    new Intent().putExtra("sciezkaObraz", image_file.getAbsolutePath()).putExtra("sciezkaObraz", image_file.getAbsolutePath()));
            Log.d("MainActivity",image_file.getAbsolutePath().toString());

            finish();
        }
        else {

            setResult(Activity.RESULT_OK,
                    new Intent().putExtra("sciezkaObraz",image_file.getAbsolutePath()).putExtra("sciezkaObraz",""));
            Log.d("MainActivity","No image created");

            finish();

        }
    }*/

}