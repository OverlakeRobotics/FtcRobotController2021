package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpMode - BLUE - TOP", group = "Autonomous")
public class AutonomousOpModeBlueTop extends AutonomousOpMode {

    @Override
    public void init() {
        super.init(TeamState.BLUE, RouteState.TOP);
    }

}