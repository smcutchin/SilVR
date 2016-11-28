package com.silvrcity.silvr;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;
import java.net.URL;


import org.json.JSONObject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.GVRSceneObject;

import org.gearvrf.GVRPicker.GVRPickedObject;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRRenderData;

public class panoBox
{
     // here we should have the Listener handlers here
     // Object Listener
     public static String URLBASE = "https://s3-us-west-2.amazonaws.com/silvrcity.com/silvrfeed/";
     public pano mypanos[];
     public int curpano;
     public panoBox()
     {
    	 mypanos = null;
    	 curpano = 0;
     }

     public void loadPanos(String panostring) throws IOException, JSONException
     {
        Log.e( "PBOX", panostring);
       
       JSONObject json = new JSONObject(panostring);
 
        
        // get the title
       // System.out.println(json.get("title"));
        // get the data
       JSONArray parray = (JSONArray) json.get("panos");
        //Log.println(Log.DEBUG, "GLYPHUI", "Have Array");
       Log.e("PBOX", String.format("%d", parray.length()));
        mypanos = new pano[parray.length()];
        // walk through the panos and convert to internal objects
        for (int i=0;i < mypanos.length; i++)
        {
            //Log.println(Log.DEBUG, "GLYPHUI", "walk the beast");
            JSONObject apano = (JSONObject) parray.get(i);
            String aname = apano.getString("name");
            String aleft = apano.getString("left");
            String aright = apano.getString("right");
            String athumb = apano.getString("thumb");
            pano acpano = new pano(aname,aleft,aright, athumb);
            mypanos[i] = acpano;
        }
        Log.e("PBOX", "loaded panos");
        
     }

     // in here we build the scene with scene graph objects
     public void initScene(GVRContext acontext, GVRScene ascene) throws IOException
     {
   
    	 //for (int i=0; i < mypanos.length; i++)
       for (int i=0; i < mypanos.length; i++)
         {
    	   pano apano = mypanos[i];
           // load textures from assets/panos directory
           URL alurl = new URL(URLBASE + apano.myleft);
           URL arurl = new URL(URLBASE + apano.myright);
             URL aturl = new URL(URLBASE + apano.mythumbname);
             Log.e("PBOX",alurl.toString());
             Log.e("PBOX",arurl.toString());
             Log.e("PBOX", aturl.toString());
             Future<GVRTexture> textureL = acontext.loadFutureTexture(new GVRAndroidResource(acontext, alurl));
             Future<GVRTexture> textureR = acontext.loadFutureTexture(new GVRAndroidResource(acontext, arurl));
             Future<GVRTexture> textureT = acontext.loadFutureTexture(new GVRAndroidResource(acontext, aturl));
             //Future<GVRTexture> textureL = acontext.loadFutureTexture(new GVRAndroidResource(acontext, apano.myleft));
           //Future<GVRTexture> textureR = acontext.loadFutureTexture(new GVRAndroidResource(acontext, apano.myright));
          // create our left and right spheres
          GVRSphereSceneObject panoL = new GVRSphereSceneObject(acontext,false,textureL);
          GVRSphereSceneObject panoR = new GVRSphereSceneObject(acontext,false,textureR);
          // set their masks
          panoL.getRenderData().setRenderMask(0);
          panoR.getRenderData().setRenderMask(0);
          if (i == 0)
          {
            panoL.getRenderData().setRenderMask(GVRRenderData.GVRRenderMaskBit.Left);
            panoR.getRenderData().setRenderMask(GVRRenderData.GVRRenderMaskBit.Right);   	  
          }
                // add it to the glyph array
          mypanos[i].myltexture = textureL;
          mypanos[i].myrtexture = textureR;
             mypanos[i].mythumb = textureT;
            mypanos[i].mylsphere = panoL;
            mypanos[i].myrsphere = panoR;
          // add it to the scene to display
          ascene.addSceneObject(panoL);
          ascene.addSceneObject(panoR);
            //System.out.println(myglyphs[i].myname);
        }

           
     }

     // turn glyphs on and off
     public void hidePano(String aname)
     {
    	 for (pano apo : mypanos)
    	 {
    		 if (apo.myname.equals(aname))
    		 {
    	         apo.mylsphere.getRenderData().setRenderMask(0);
    	         apo.myrsphere.getRenderData().setRenderMask(0);		 
    		 }
    	 }
         // add in code here to hide and show glyphs
     }

     public void hideAll()
     {
    	 for (pano apo : mypanos)
    	 {
    		    apo.mylsphere.getRenderData().setRenderMask(0);
    	        apo.myrsphere.getRenderData().setRenderMask(0);		 
    	 }
         // add in code here to hide and show glyphs
     }

     // show a pano
     public void showPano(String aname)
     {
    	 for (pano apo : mypanos)
    	 {
    		 if (apo.myname.equals(aname))
    		 {
    	         apo.mylsphere.getRenderData().setRenderMask(GVRRenderData.GVRRenderMaskBit.Left);
    	         apo.myrsphere.getRenderData().setRenderMask(GVRRenderData.GVRRenderMaskBit.Right);		 
    		 }
    	 }
         // add in code here to hide and show glyphs
     }
     public void nextPano()
     {
    	 curpano = (curpano + 1) % mypanos.length;
    	 pano apo = mypanos[curpano];
    		 apo.mylsphere.getRenderData().setRenderMask(GVRRenderData.GVRRenderMaskBit.Left);
    	     apo.myrsphere.getRenderData().setRenderMask(GVRRenderData.GVRRenderMaskBit.Right);		 
    	
         // add in code here to hide and show glyphs
     }

    public boolean isLoaded()
    {
        boolean ares;
        ares = mypanos[0].myltexture.isDone();
        return ares;
    }

     // this would return the scene graph object
     public Object getScene()
     {
        return null;
     }

         
}
