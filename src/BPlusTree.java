import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

class Node implements Serializable {
    ArrayList<Address> addresses;
    ArrayList<String> childrenIds;
    boolean isLeaf;
    String nodeId;

    Node(boolean isLeaf) {
        this.isLeaf = isLeaf;
        addresses = new ArrayList<>();
        childrenIds = new ArrayList<>();
        nodeId = UUID.randomUUID().toString();
    }
}

