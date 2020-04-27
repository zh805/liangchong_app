package com.example.a12652.just_try.Shape;

import android.graphics.Bitmap;

/**
 * Created by 12652 on 2020/3/12.
 */

public class DevShape {
    private final float RADIUS = 0.1f;
    private final float CYLINDER_HEIGHT = 0.2f;
    private final float CONE_HEIGHT = 0.2f;

    private Cylinder cylinder;
    private Cone cone;
    private Circular circular;
    private float[] cylinderCenter;
    private float[] circularCenter;
    private float[] coneCenter;

    public DevShape(float[] place) {
        cylinderCenter = place.clone();
        circularCenter = place.clone();
        coneCenter = place.clone();
        circularCenter[1] = circularCenter[1] + CYLINDER_HEIGHT/2;
        coneCenter[1] = coneCenter[1] - CYLINDER_HEIGHT/2;
        cylinder = new Cylinder(cylinderCenter, RADIUS, CYLINDER_HEIGHT);
        circular = new Circular(circularCenter, RADIUS);
        cone = new Cone(coneCenter, RADIUS, CONE_HEIGHT);
    }

    public void drawDev(float[] scratchXY,float[] color) {
        cylinder.drawSelf(scratchXY);
        circular.drawSelf(scratchXY,color);
        cone.drawSelf(scratchXY,color);
    }
    public void setCylinderBitmap(Bitmap cylinderBitmap){
        cylinder.setCylinder(cylinderBitmap);
    }
}
