package VM2M;

import VM2M.elements.Element;

/**
 * Each statement in Vapor will have a Node
 * according to it's line #, assignment variable,
 * accessor variable(s), input Node, and output Node.
 */
public class Node {
    public int line_number;
    public Element element;
    public Node(int line, Element e) {
        this.element = e;
        this.line_number = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return line_number == node.line_number;
    }

    @Override
    public int hashCode() {
        return line_number;
    }
}
