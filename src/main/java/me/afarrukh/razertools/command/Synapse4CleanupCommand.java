package me.afarrukh.razertools.command;

import com.github.rvesse.airline.annotations.Command;
import org.w3c.dom.Document;

@Command(name = "synapse4")
public class Synapse4CleanupCommand extends AbstractParseAndRewriteCommand {

    @Override
    public void execute(Document document) {
        collapseMouseMovementSynapse4(document);
    }

    private static void collapseMouseMovementSynapse4(Document document) {
        collapseMouseMovement(document);
    }

    private static void collapseMouseMovement(Document document) {
        var mouseEventNodes = nodeListToList(document.getElementsByTagName("MouseEvent"));
        for (var mouseEventNode : mouseEventNodes) {
            var childNodes = mouseEventNode.getChildNodes();
            var childNode = nodeListToList(childNodes);
            var buffers = childNode.stream().filter(node -> node.getNodeName().equals("Buffer")).toList();
            var indexToDeleteUpTo = buffers.size() - 1;
            if(buffers.size() <= 1) {
                continue;
            }
            for (int i = 0; i < (long) indexToDeleteUpTo; i++) {
                var child = buffers.get(i);
                child.getParentNode().removeChild(child);
            }
        }
    }

}
