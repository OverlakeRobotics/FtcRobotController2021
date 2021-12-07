package org.firstinspires.ftc.teamcode.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpModeTestRedTop", group = "Autonomous")
public class AutonomousOpModeTestRedTop extends AutonomousOpModeTest {

    @Override
    public void init() {
        super.init(TeamState.RED, RouteState.TOP);

    }

}