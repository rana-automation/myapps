package com.mindtap.StefDefinations;
import com.mindtap.Actions.LaunchMindtap;
import cucumber.api.java.en.*;

public class MindtapLogin extends LaunchMindtap {

    LaunchMindtap mindtap = new LaunchMindtap();

      @Given("^Instructor user already on Mindtap snapshot$")
    public void instructor_user_already_on_Mindtap_snapshot()
      {
        mindtap.launchMindtapurl();

    }




}
