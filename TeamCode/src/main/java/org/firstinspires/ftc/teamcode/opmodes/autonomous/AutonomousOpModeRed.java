package org.firstinspires.ftc.teamcode.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.helpers.TeamState;

@Autonomous(name = "AutonomousOpMode - RED", group = "Autonomous")
public class AutonomousOpModeRed extends AutonomousOpMode {

    @Override
    public void init() {
        teamState = TeamState.RED;
        super.init();
    }

}