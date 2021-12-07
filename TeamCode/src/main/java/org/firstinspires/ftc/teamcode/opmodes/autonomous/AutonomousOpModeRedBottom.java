package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.helpers.RouteState;
import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpMode - RED - BOTTOM", group = "Autonomous")
public class AutonomousOpModeRedBottom extends AutonomousOpMode {

    @Override
    public void init() {
        super.init(TeamState.RED, RouteState.BOTTOM);
    }

}