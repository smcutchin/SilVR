package com.silvrcity.silvr;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;
import java.util.ArrayList;

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

public class glyphUI
{
     // here we should have the Listener handlers here
     // Object Listener
     public ArrayList<glyph> myglyphs;
     public boolean guiactive;
     public float guicount;
     public glyph activeglyph;
     public boolean showglyphs;
     
     public glyphUI()
     {
    	 guiactive = false;
    	 guicount = 0.0f;
    	 activeglyph = null;
    	 showglyphs = true;
     }

     public void loadGlyphs(String glyphstring) throws IOException, JSONException
     {
        Log.println(Log.DEBUG, "GLYPHUI", glyphstring);
       
       JSONObject json = new JSONObject(glyphstring);
        Log.println(Log.DEBUG, "GLYPHUI", "Have Object");
        
        // get the title
       // System.out.println(json.get("title"));
        // get the data
       JSONArray glypharray = (JSONArray) json.get("glyphs");
        Log.println(Log.DEBUG, "GLYPHUI", "Have Array");
 
        myglyphs = new ArrayList<glyph>();
        // walk through the glyphs and convert to internal objects
        for (int i=0;i < glypharray.length(); i++)
        {
            Log.println(Log.DEBUG, "GLYPHUI", "walk the beast");
            JSONObject ajglyph = (JSONObject) glypharray.get(i);
            String aname = ajglyph.getString("name");
            double along = ajglyph.getDouble("long");
            double alat = ajglyph.getDouble("lat");
            double arad = ajglyph.getDouble("rad");
            String aaction = ajglyph.getString("action");
            String aicon = ajglyph.getString("icon");
            glyph aglyph = new glyph(aname,along,alat,arad,aaction,aicon);
            myglyphs.add(aglyph);
        }
        Log.println(Log.DEBUG, "GLYPHUI", "loaded glyphs");
        
     }

     // in here we build the scene with scene graph objects
     public void initScene(GVRContext acontext, GVRScene ascene)
     {
         Future<GVRTexture> textureL = acontext.loadFutureTexture(new GVRAndroidResource(acontext, R.raw.whitefield));
   
         for (int i=0; i < myglyphs.size(); i++)
        {
          // create our initial scene graph object.
             glyph agl = myglyphs.get(i);
        	 if (agl.mythumb == null) agl.mythumb = textureL;
        	 GVRSphereSceneObject  glyphR = new GVRSphereSceneObject(acontext,false,agl.mythumb);
          // set up its position and orientation

            glyphR.setName("cube");
            glyphR.getTransform().setScale(0.05f, 0.05f,0.05f);
            // now we calculate position from long and lat
            double xtheta = ((agl.mylong+270) % 360)*1.0/180.0 * Math.PI;
            double yphi = (agl.mylat+90.0)/180.0 * Math.PI;
            double z = -0.5 * Math.sin(yphi)* Math.cos(xtheta);
            double x = -0.5 * Math.sin(yphi) * Math.sin(xtheta);
            double y = -0.5 * Math.cos(yphi);
            Log.e( "GLYPHUI",String.format("%f %f %f", x,y,z));
            glyphR.getTransform().setPosition((float)x,(float)y,(float)z);
            glyphR.attachEyePointeeHolder();
          // add it to the glyph array
            agl.mygui = glyphR;
          // add it to the scene to display
          ascene.addSceneObject(glyphR);
            //System.out.println(myglyphs[i].myname);
        }
     }

     // turn glyphs on and off
     public void toggleGlyphs()
     {
         // add in code here to hide and show glyphs
    	 if (showglyphs)
    	 {
    		 for (glyph aglyph: myglyphs)
    		 {
    			 if (!aglyph.myname.equals("toggle"))
    			   aglyph.mygui.getRenderData().setRenderMask(0x0);
    		 }
    		 showglyphs = !showglyphs;
    	 }
    	 else
    	 {
    	   for (glyph aglyph : myglyphs)
    	   {
    		   
    		   int newmask = GVRRenderData.GVRRenderMaskBit.Left | GVRRenderData.GVRRenderMaskBit.Right;
    		   if (!aglyph.myname.equals("toggle"))
    		     aglyph.mygui.getRenderData().setRenderMask(newmask);
    	   }
    	   showglyphs = !showglyphs;
    	 }
     }

     // this would return the scene graph object
     public Object getScene()
     {
        return null;
     }

     public String pickGui(GVRContext acontext)
     {
     
         for (glyph gobject : myglyphs) 
         {
        	 //Log.e("GLYPHUI", "check for hit");
        	 GVRSceneObject hobject = gobject.mygui;
        	 hobject.getRenderData().getMaterial().setColor(0.0f,0.0f,1.0f);

         }

         float r,g,b;
         r = 1.0f; g= 0.0f; b = 0.0f;
         if (guiactive)
         {
            r = 1.0f - guicount /300.0f;
            g = 0.0f + guicount/300.0f;
            b = 0.0f;
            r = Math.max(r, 0.0f);
            g = Math.min(g, 1.0f);
         }
     guiactive = false;
     for (GVRPickedObject pickedObject : GVRPicker.findObjects(acontext
             .getMainScene()))
     {
         for (glyph gobject : myglyphs) 
         {
        	 GVRSceneObject object = gobject.mygui;
             if (pickedObject.getHitObject().equals(object)) 
             {
            	 //Log.e("GLYPHUI", "HIT");
                 object.getRenderData()
                         .getMaterial()
                         .setColor(r,g,b);
                 guiactive = true;
                 guicount += 1.0f;
                 activeglyph = gobject;
                 break;
             }
         }
     }
     if (!guiactive)
     {
    	 guicount = 0.0f;
    	 activeglyph = null;
     }
     if (guicount >= 300.0)
     {
    	 guiactive = false;
    	 guicount = 0.0f;
    	 if (activeglyph.myaction.equals("toggle"))
    	 {
    		 toggleGlyphs();
    	 }
    	 else return activeglyph.myaction;
     }
     return "";
}
     public void loadPanos(panoBox apbox) throws IOException, JSONException
     {
        
        Log.e("GLYPHUI", "Build Pano Guis");
        // walk through the panos
        double slong = 0.0;
//        for (int i=0;i < apbox.mypanos.length; i++)
      for (int i=0;i < apbox.mypanos.length; i++)
        {
            String aname = new String(apbox.mypanos[i].myname);
            double along = slong; slong += 30.0;
            double alat = 0.0;
            double arad = 1.0;
            String aaction = new String("pano");
            String aicon = new String(apbox.mypanos[i].myname);
            glyph aglyph = new glyph(aname,along,alat,arad,aaction,aicon);
            aglyph.myltxt = apbox.mypanos[i].myltexture;
            aglyph.myrtxt = apbox.mypanos[i].myrtexture;
            aglyph.mythumb = apbox.mypanos[i].mythumb;
            myglyphs.add(aglyph);
        }
        Log.e("GLYPHUI", "loaded pano glyphs");
        
     }
}
