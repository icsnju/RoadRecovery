package nju.ics.Entity;

import static nju.ics.Entity.NodeSource.IDENTIFY;

public class RuntimeNode {
    public Node node;
    public String transTime = null;

    public RuntimeNode() {
    }

    public RuntimeNode(Node node, String transTime) {
        this.node = node;
        this.transTime = transTime;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RuntimeNode)) return false;
        RuntimeNode other = (RuntimeNode) object;
        return this.node.equals(other.node);
    }

    @Override
    public Object clone() {
        RuntimeNode stu = null;
        try {
            stu = (RuntimeNode) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }


}
