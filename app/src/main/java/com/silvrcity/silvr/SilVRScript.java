
package com.silvrcity.silvr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;
import java.net.URL;


import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRCameraRig;
import org.gearvrf.GVRCamera;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRScript;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;


import android.graphics.Color;
import android.util.Log;
import android.content.Context;
import android.view.Gravity;


public class SilVRScript extends GVRMain {

    public static String GLYPHURL = "https://s3-us-west-2.amazonaws.com/silvrcity.com/silvrfeed/testglyph.json";
    public static String PANOURL="https://s3-us-west-2.amazonaws.com/silvrcity.com/silvrfeed/panoramas.json";
	GVRContext mycontext;
	glyphUI myglyphui;
	panoBox mypbox;
    GVRSphereSceneObject sphereObjectR;

    // veariables available for setting up the Background Scene that appears while
    // panoramas are loading on startup.
    private static final float CUBE_WIDTH = 20.0f;
    private Future<GVRTexture> mFutureCubemapTexture;
    private GVRMaterial mCubemapMaterial;
    private GVRTextViewSceneObject tsceneObject;

    @Override
    public void onInit(GVRContext gvrContext) {

        // get a handle to the scene
        GVRScene scene = gvrContext.getNextMainScene();
        initHoloDeck(gvrContext,scene);
//        Future<GVRTexture> texture = gvrContext.loadFutureTexture(new GVRAndroidResource(gvrContext, R.raw.bluefield));

        // create a sphere scene object with the specified texture and triangles facing inward (the 'false' argument)
//       sphereObjectR = new GVRSphereSceneObject(gvrContext, 72, 144, false, texture);
//        scene.addSceneObject(sphereObjectR);
        // set background color
        GVRCameraRig mainCameraRig = scene.getMainCameraRig();
        mainCameraRig.getLeftCamera()
                .setBackgroundColor(Color.WHITE);
       GVRCamera rcam = mainCameraRig.getRightCamera();
                rcam.setBackgroundColor(Color.BLUE);

        GVRSphereSceneObject sphereObjectL = null;
        sphereObjectR = null;
        mycontext = gvrContext;
        // load panoramas
        mypbox = new panoBox();
        String apanostring = "";
        try {
            String aupanostring = readURL(PANOURL);
         //apanostring = readFile(gvrContext,R.raw.panoramas);
        // add the scene object to the scene graph
        mypbox.loadPanos(aupanostring);
        mypbox.initScene(gvrContext, scene);
        }
        catch(Exception e) { apanostring = ""; };
 
        // let's load up the GUI object
        myglyphui = new glyphUI();
        String aglyphstring = "";
        try {
            String auglyphstring = readURL(GLYPHURL);
            Log.e("GLYPH",auglyphstring);
         //aglyphstring = readFile(gvrContext,R.raw.testglyph);
        // add the scene object to the scene graph
        myglyphui.loadGlyphs(auglyphstring);
        myglyphui.loadPanos(mypbox);
        myglyphui.initScene(gvrContext, scene);
        }
        catch(Exception e) { Log.e("GLYPH",e.toString()); aglyphstring = ""; };

        
    }

    @Override
    public void onStep() {
      String myaction =  myglyphui.pickGui(mycontext);
      Log.e("GLYPHUI", myaction);
      if (myaction.equals("hide"))
      {
          mypbox.hideAll();
                    	  
      }
      else if (myaction.equals("show"))
      {
    	  mypbox.hideAll();
          mypbox.nextPano();
      }
      else if (myaction.equals("pano"))
      {
    	  String aname = myglyphui.activeglyph.myname;
    	  mypbox.hideAll();
    	  mypbox.showPano(aname);
      }
/*
        if (mypbox.isLoaded()) {
            //Log.e("LOAD", "loaded");
            tsceneObject.getRenderData().setRenderMask(0);

        }
*/
    }

    public String readFile(GVRContext gvrContext, int fgid) throws IOException {
/* later we will add in advanced file support
   	 final File file;
   	 final InputStream inputStream = new FileInputStream(file);
   	 */
    	Context acon = gvrContext.getContext();
     	 
   	 InputStream ais = acon.getResources().openRawResource(fgid);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(ais));
   	 // for the moment we will load from a fixed resource.
        final StringBuilder stringBuilder = new StringBuilder();
        
        boolean done = false;
        
        while (!done) {
            final String line = reader.readLine();
            done = (line == null);
            
            if (line != null) {
                stringBuilder.append(line);
            }
        }
        
        reader.close();
        ais.close();
        
        return stringBuilder.toString();
    }

    public String readURL(String aurl) throws IOException {
/* later we will add in advanced file support
   	 final File file;
   	 final InputStream inputStream = new FileInputStream(file);
   	 */
        Log.e("GLYPH",aurl);
        URL myurl = new URL(aurl);
        Log.e("GLYPH",myurl.toString());

        InputStream ais = myurl.openStream();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(ais));
        // for the moment we will load from a fixed resource.
        final StringBuilder stringBuilder = new StringBuilder();
        Log.e("GLYPH","BUILDR");

        boolean done = false;

        while (!done) {
            final String line = reader.readLine();
            done = (line == null);
          //  Log.e("GLYPH","LINE");

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        ais.close();

        return stringBuilder.toString();
    }

    public void initHoloDeck(GVRContext gvrContext, GVRScene ascene) {
        // Uncompressed cubemap texture
        mFutureCubemapTexture = gvrContext.loadFutureCubemapTexture(new GVRAndroidResource(gvrContext, R.raw.hex));  //change this to a grid.
        mCubemapMaterial = new GVRMaterial(gvrContext, GVRMaterial.GVRShaderType.Cubemap.ID);
        mCubemapMaterial.setMainTexture(mFutureCubemapTexture);

        // ////////////////////////////////////////////////////////////
        // create surrounding cube using GVRCubeSceneObject method A //
        // ////////////////////////////////////////////////////////////
        GVRCubeSceneObject mCubeEvironment = new GVRCubeSceneObject(
                gvrContext, false, mCubemapMaterial);
        mCubeEvironment.getTransform().setScale(CUBE_WIDTH, CUBE_WIDTH,
                CUBE_WIDTH);
        ascene.addSceneObject(mCubeEvironment);
        tsceneObject = new GVRTextViewSceneObject(gvrContext);
        // set the scene object position
        float x = 0 * 2.0f;// i * 2.0f - 4.0f;
        tsceneObject.getTransform().setPosition(0.0f, 0.0f, -2.0f);
        tsceneObject.setText("Loading Scene!");
        tsceneObject.setTextColor(Color.GRAY);
        tsceneObject.setGravity(Gravity.TOP | Gravity.LEFT);
        tsceneObject.setTextSize(14);
        //tsceneObject.setRefreshFrequency(frequencies[i % 3]);
        // add the scene object to the scene graph
        ascene.addSceneObject(tsceneObject);
        //tsceneObject.getTransform().setPositionZ(-3.0f);
    }
}
