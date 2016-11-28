
package com.silvrcity.silvr;

import android.os.Bundle;

import org.gearvrf.GVRActivity;

public class SilVRActivity extends GVRActivity
{
    SilVRScript main = new SilVRScript();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setMain(main,"gvr.xml");

       // SilVRScript scriptmain = new SilVRScript();
       // setMain(scriptmain, "gvr.xml");
    }
}
