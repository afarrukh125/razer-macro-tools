package me.afarrukh.razertools.command;

import com.github.rvesse.airline.annotations.Command;
import me.afarrukh.razertools.utils.NodeUtils;
import org.w3c.dom.Document;

@Command(name = "doublex")
public class DoubleXCommand extends AbstractParseAndRewriteCommand {
    @Override
    public void execute(Document document) {
        NodeUtils.collapseMouseMovement(document);
        NodeUtils.multiplyFirstElementOfChildWithTagByFactor(document, "X", 2);
    }

}
