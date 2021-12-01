package org.firstinspires.ftc.teamcode.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpModeTestBlueBottom", group = "Autonomous")
public class AutonomousOpModeTestBlueBottom extends AutonomousOpModeTest {

    @Override
    public void init() {
        teamState = TeamState.BLUE;
        routeState = RouteState.BOTTOM;
        super.init();
    }

}