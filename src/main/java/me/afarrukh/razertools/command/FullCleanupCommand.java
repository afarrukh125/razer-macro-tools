package me.afarrukh.razertools.command;

import com.github.rvesse.airline.annotations.Command;
import org.w3c.dom.Document;

@Command(name = "fullcleanup")
public class FullCleanupCommand extends AbstractParseAndRewriteCommand {

    @Override
    public void execute(Document document) {
        new CollapseMouseMovementCommand().execute(document);
        new DoubleXCommand().execute(document);
        new RemoveMouseMovementDelayCommand().execute(document);
    }
}
