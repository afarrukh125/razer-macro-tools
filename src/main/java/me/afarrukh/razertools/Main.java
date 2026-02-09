package me.afarrukh.razertools;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.builder.CliBuilder;
import com.github.rvesse.airline.help.Help;
import me.afarrukh.razertools.command.Synapse4CleanupCommand;

public class Main {

    public static void main(String[] args) {
        Cli<Runnable> cli = new CliBuilder<Runnable>("parser")
                .withCommand(Help.class)
                .withCommand(Synapse4CleanupCommand.class)
                .build();
        cli.parse(args).run();
    }
}
