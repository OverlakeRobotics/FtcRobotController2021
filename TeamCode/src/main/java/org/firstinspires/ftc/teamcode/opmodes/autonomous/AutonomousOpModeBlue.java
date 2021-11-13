package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpMode - BLUE", group = "Autonomous")
public class AutonomousOpModeBlue extends AutonomousOpMode {

    @Override
    public void init() {
        teamState = TeamState.BLUE;
        super.init();
    }

}