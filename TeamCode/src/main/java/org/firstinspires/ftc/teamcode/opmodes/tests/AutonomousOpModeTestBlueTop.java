package org.firstinspires.ftc.teamcode.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpModeTestBlueTop", group = "Autonomous")
public class AutonomousOpModeTestBlueTop extends AutonomousOpModeTest {

    @Override
    public void init() {
        super.init(TeamState.BLUE, RouteState.TOP);
    }

}