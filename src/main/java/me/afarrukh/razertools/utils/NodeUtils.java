package me.afarrukh.razertools.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class NodeUtils {
    public static LinkedList<Node> getChildNodes(Node parent) {
        LinkedList<Node> children = new LinkedList<>();
        NodeList childNodes = parent.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            children.add(childNodes.item(i));
        }
        return children;
    }

    public static LinkedList<Node> nodeListToList(NodeList nodeList) {
        LinkedList<Node> items = new LinkedList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            items.add(nodeList.item(i));
        }
        return items;
    }

    public static void multiplyFirstElementOfChildWithTagByFactor(Document document, String tagName, int factor) {
        NodeList nodesWithTag = document.getElementsByTagName(tagName);
        for (int i = 0; i < nodesWithTag.getLength(); i++) {
            Node targetNode = nodesWithTag.item(i).getChildNodes().item(0);
            int currentValue = parseInt(targetNode.getNodeValue());
            targetNode.setNodeValue(String.valueOf(currentValue * factor));
        }
    }


    public static void collapseMouseMovement(Document document) {
        var mouseMovementNodes = document.getElementsByTagName("MouseMovement");
        for (var node : nodeListToList(mouseMovementNodes)) {
            var mouseMovementEventNodes = getChildNodes(node);
            mouseMovementEventNodes.stream()
                    .limit(mouseMovementEventNodes.size() - 2)
                    .forEach(childNode -> childNode.getParentNode().removeChild(childNode));
        }
    }
}
