package com.silvrcity.silvr;

import org.gearvrf.GVRScene;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.GVRTexture;
import java.util.concurrent.Future;


public class glyph
{
    public String myname;
    public double mylong, mylat, myrad;
    public String myaction;
    public String myicon;
    public GVRSphereSceneObject mygui;
    public Future<GVRTexture> myltxt;
    public Future<GVRTexture> myrtxt;
    public Future<GVRTexture> mythumb;

    public glyph(String aname, double along, double alat, double arad, String aact, String aicon)
    {
        myname = aname;
        mylong = along;
        mylat = alat;
        myrad = arad;
        myaction = aact;
        myicon = aicon;
        mygui = null;
        myltxt = null;
        myrtxt = null;
        mythumb = null;
    }
}
