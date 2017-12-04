/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.elisa.kissmekate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

/**
 * View which displays a bitmap containing a face along with overlay graphics that identify the
 * locations of detected facial landmarks.
 */
public class FaceView extends View {
    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;
    public int[]lengths;
    String message;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        message="";
    }

    /**
     * Sets the bitmap background and the associated face detections.
     */
    void setContent(Bitmap bitmap, SparseArray<Face> faces) {
        mBitmap = bitmap;
        mFaces = faces;

        // mFaces.valueAt(0).getLandmarks();
        invalidate();
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceAnnotations(canvas, scale);

        }
    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Draws a small circle for each detected landmark, centered at the detected landmark position.
     * <p>
     *
     * Note that eye landmarks are defined to be the midpoint between the detected eye corner
     * positions, which tends to place the eye landmarks at the lower eyelid rather than at the
     * pupil position.
     */
   private int leftX;
    private int leftY;
    private int rightX;
    private int rightY;
    private int bottomX;
    private int bottomY;
    private static int noseX;
    private static int noseY;
    private void drawFaceAnnotations(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        boolean leftFound=false;
        boolean rightFound=false;
        boolean bottomFound=false;
        boolean noseFound=false;
        if (mFaces.size() == 0) {
            message="No face dectected please try again!";

        }
        for (int i = 0; i < mFaces.size(); ++i) {
            Face face = mFaces.valueAt(i);
          //  Log.d("Landmark","i value:"+i+" face  is smiling probably :" + face.getIsSmilingProbability());

            for (Landmark landmark : face.getLandmarks()) {
                switch (landmark.getType()){
                    case Landmark.LEFT_MOUTH:
                       leftX  = (int) (landmark.getPosition().x * scale);
                         leftY= (int) (landmark.getPosition().y * scale);
                        //   Log.d("Landmark", "landmark: " + landmark.toString() + " " + cx + " " + cy);
                        leftFound=true;
                        canvas.drawCircle(leftX, leftY, 10, paint);
                        break;
                    case Landmark.RIGHT_MOUTH:
                        rightX = (int) (landmark.getPosition().x * scale);
                        rightY = (int) (landmark.getPosition().y * scale);
                        //   Log.d("Landmark", "landmark: " + landmark.toString() + " " + cx + " " + cy);
                        canvas.drawCircle(rightX,rightY, 10, paint);
                        rightFound=true;
                        break;

                    case Landmark.BOTTOM_MOUTH:
                        bottomX = (int) (landmark.getPosition().x * scale);
                        bottomY = (int) (landmark.getPosition().y * scale);
                        //   Log.d("Landmark", "landmark: " + landmark.toString() + " " + cx + " " + cy);
                        canvas.drawCircle(bottomX, bottomY, 10, paint);
                        bottomFound=true;
                        break;
                    case Landmark.NOSE_BASE:
                        noseX = (int) (landmark.getPosition().x * scale);
                        noseY = (int) (landmark.getPosition().y * scale);
                        //   Log.d("Landmark", "landmark: " + landmark.toString() + " " + cx + " " + cy);
                        canvas.drawCircle(noseX, noseY, 10, paint);
                        noseFound=true;
                        break;
                    default:

                        break;


                }


            }
            getlenngths();
            getRelations();

        }
        if(leftFound && rightFound && bottomFound && noseFound){
            message="";
            Log.d("LAND","FOUND ALL!");
        }else
        {
            message="Missing landmarks! Please retry!";
            Log.d("LAND","Missing!!");
        }


    }
    //A
    public double cornerlength;
    //B
    public double leftmiddle;
    //C
    public double rightmiddle;
    //D
    public double middlenose;
    //E
    public double leftnose;
    //F
    public double rightnose;
    private void getlenngths(){
        cornerlength=countdistance(leftX,rightX,leftY,rightY);
        leftmiddle=countdistance(leftX,bottomX,leftY,bottomY);
        rightmiddle=countdistance(rightX,bottomX,rightY,bottomY);
        middlenose=countdistance(bottomX,noseX,bottomY,noseY);
        leftnose=countdistance(leftX,noseX,leftY,noseY);
        rightnose=countdistance(rightX,noseX,rightY,noseY);




    }
    private double countdistance(int x1, int x2, int   y1, int y2){
        double distance;
        double diff1=x2-x1;
        double diff2=y2-y1;
        double multi1= diff1*diff1;
        double multi2= diff2*diff2;
        double sum= multi1+multi2;
        distance=Math.sqrt(sum);


return distance;

    }
    double AB;
    double AC;
    double AD;
    double AE;
    double AF;
    double BC;
    double BD;
    double BE;
    double BF;
    double CD;
    double CE;
    double CF;
    double DE;
    double DF;
    double EF;

    public String getMyMessage(){
        return message;
    }
    public void getRelations(){
        AB=countRelation(cornerlength,leftmiddle);
        AC=countRelation(cornerlength,rightmiddle);
        AD=countRelation(cornerlength,middlenose);
        AE=countRelation(cornerlength,leftnose);
        AF=countRelation(cornerlength,rightnose);
        BC=countRelation(leftmiddle,rightmiddle);
        BD=countRelation(leftmiddle,middlenose);
        BE=countRelation(leftmiddle,leftnose);
        BF=countRelation(leftmiddle,rightnose);
        CD=countRelation(rightmiddle,middlenose);
        CE=countRelation(rightmiddle,leftnose);
        CF=countRelation(rightmiddle,rightnose);
        DE=countRelation(middlenose,rightnose);
        DF=countRelation(middlenose,leftnose);
        EF=countRelation(leftnose,rightnose);
    }
    public double countRelation(double value1, double value2){
        double ratio= value1/value2;
        return ratio;
    }
    public double[] getMyRelations(){
        double[]relations=new double[]{  AB, AC, AD, AE, AF, BC, BD, BE, BF, CD, CE,CF,DE, DF, EF};
        return relations;
    }
    public double[] getMyLenths(){
        double [] numbers= new double[]{cornerlength, leftmiddle,
        rightmiddle,
        middlenose,
        leftnose,
        rightnose};
        return numbers;
    }
}
