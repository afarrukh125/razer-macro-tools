package me.afarrukh.razertools.command;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import java.util.Collection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Command(name = "synapse4")
public class Synapse4CleanupCommand extends AbstractParseAndRewriteCommand {

    @Option(name = "--time", description = "Time to set for mouse movements")
    private String timeValue = "0";

    @Option(name = "--number", description = "Time to set for delays for all events")
    private String number = "0.03";

    @Override
    public void execute(Document document) {
        collapseMouseMovementSynapse4(document);
        replaceAllNodes(document, "time", timeValue);
        replaceAllNodes(document, "Number", number);
    }

    private static void collapseMouseMovementSynapse4(Document document) {
        collapseMouseMovement(document);
    }

    private static void collapseMouseMovement(Document document) {
        var mouseEventNodes = nodeListToList(document.getElementsByTagName("MouseEvent"));
        for (var mouseEventNode : mouseEventNodes) {
            var childNodes = mouseEventNode.getChildNodes();
            var childNode = nodeListToList(childNodes);
            var buffers = childNode.stream()
                    .filter(node -> node.getNodeName().equals("Buffer"))
                    .toList();
            var indexToDeleteUpTo = buffers.size() - 1;
            if (buffers.size() <= 1) {
                continue;
            }
            for (int i = 0; i < (long) indexToDeleteUpTo; i++) {
                var child = buffers.get(i);
                child.getParentNode().removeChild(child);
            }
        }
    }

    private static void replaceAllNodes(Document document, String key, String value) {
        var timeNodes = nodeListToList(document.getElementsByTagName(key)).stream()
                .map(Node::getChildNodes)
                .map(Synapse4CleanupCommand::nodeListToList)
                .flatMap(Collection::stream)
                .toList();
        for (var timeNode : timeNodes) {
            timeNode.setNodeValue(value);
        }
    }
}
