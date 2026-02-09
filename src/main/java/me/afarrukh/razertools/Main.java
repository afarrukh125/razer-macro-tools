package me.afarrukh.razertools;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.help.Help;
import me.afarrukh.razertools.command.CollapseMouseMovementCommand;
import me.afarrukh.razertools.command.FullCleanupCommand;
import me.afarrukh.razertools.command.DoubleXCommand;
import me.afarrukh.razertools.command.RemoveMouseMovementDelayCommand;

public class Main {

    public static void main(String[] args) {
        Cli<Runnable> cli = new CliBuilder<Runnable>("parser")
                .withCommand(DoubleXCommand.class)
                .withCommand(Help.class)
                .withCommand(RemoveMouseMovementDelayCommand.class)
                .withCommand(FullCleanupCommand.class)
                .withCommand(CollapseMouseMovementCommand.class)
                .build();
        cli.parse(args).run();
    }
}
