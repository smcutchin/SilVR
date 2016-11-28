package com.silvrcity.silvr;

import org.gearvrf.GVRScene;
import org.gearvrf.scene_objects.GVRCubeSceneObject;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.GVRTexture;
import java.util.concurrent.Future;

public class pano
{
    public String myname;
    public String myleft;
    public String myright;
    public String mythumbname;
    public GVRSphereSceneObject mylsphere;
    public GVRSphereSceneObject myrsphere;
    public Future<GVRTexture> myltexture;
    public Future<GVRTexture> myrtexture;
    public Future<GVRTexture> mythumb;
    
    public pano(String aname, String aleft, String aright, String athumb)
    {
        myname = aname;
        myleft = aleft;
        myright = aright;
        mythumbname = athumb;
        mylsphere = null;
        myrsphere = null;
        mythumb = null;
    }
}
