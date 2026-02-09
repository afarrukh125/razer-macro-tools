package me.afarrukh.razertools.command;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Command(name = "movedelay")
public class RemoveMouseMovementDelayCommand extends AbstractParseAndRewriteCommand {

    @Option(name = "--movedelay")
    protected String moveDelay = "0";

    @Option(name = "--otherdelays")
    protected String otherDelays = "8";

    @Override
    public void execute(Document document) {

        NodeList delayNodes = document.getElementsByTagName("Delay");
        for(int i = 0; i<delayNodes.getLength(); i++) {
            Node delayNode = delayNodes.item(i).getFirstChild();
            delayNode.setNodeValue(otherDelays);
        }

        NodeList mouseMovementEventNodes = document.getElementsByTagName("MouseMovementEvent");
        for (int i = 0; i < mouseMovementEventNodes.getLength(); i++) {
            Node targetNode = mouseMovementEventNodes.item(i);
            for(int j = 0; j < targetNode.getChildNodes().getLength(); j++) {
                Node nextNode = targetNode.getChildNodes().item(j);
                if(nextNode.getNodeName().equals("Delay")) {
                    Node delayNode = nextNode.getFirstChild();
                    delayNode.setNodeValue(moveDelay);
                }
            }
        }
    }
}
