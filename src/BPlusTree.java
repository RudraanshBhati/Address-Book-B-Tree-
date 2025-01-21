import java.io.*;
import java.util.*;

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

public class BPlusTree implements Serializable {
    private String rootId;
    private int t;
    private static final long serialVersionUID = 1L;
    private transient String directoryPath = "bplustree_nodes";
    private transient static final String treeStateFile = "bplustree_state.ser";

    public BPlusTree(int t) {
        this.t = t;
        File stateFile = new File(treeStateFile);
        if (stateFile.exists()) {
            try {
                loadTreeState();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Failed to load existing tree state. Initializing new tree.");
                initializeNewTree();
            }
        } else {
            initializeNewTree();
        }
    }

    private void initializeNewTree() {
        Node root = new Node(true);
        this.rootId = root.nodeId;
        saveNode(root);
    }

    private void loadTreeState() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(treeStateFile))) {
            BPlusTree tree = (BPlusTree) in.readObject();
            this.rootId = tree.rootId;
        }
    }

    private Node getNode(String nodeId) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(directoryPath + "/" + nodeId + ".node"))) {
            return (Node) in.readObject();
        }
    }

    private void saveNode(Node node) {
        File dir = new File(directoryPath);
        if (!dir.exists()) dir.mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(directoryPath + "/" + node.nodeId + ".node"))) {
            out.writeObject(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveTreeState() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(treeStateFile))) {
            out.writeObject(this);
        }
    }

    public boolean search(Address address) throws IOException, ClassNotFoundException {
        return searchInternal(getNode(rootId), address);
    }

    private boolean searchInternal(Node node, Address address) throws IOException, ClassNotFoundException {
        int idx = Collections.binarySearch(node.addresses, address);
        if (idx >= 0) {
            return true;
        } else if (node.isLeaf) {
            return false;
        } else {
            idx = -(idx + 1);
            return searchInternal(getNode(node.childrenIds.get(idx)), address);
        }
    }

    public Address getAddress(Address address) throws IOException, ClassNotFoundException {
        return getAddress(getNode(rootId), address);
    }

    private Address getAddress(Node node, Address searchAddress) throws IOException, ClassNotFoundException {
        while (node != null) {
            int idx = Collections.binarySearch(node.addresses, searchAddress);
            if (idx >= 0) {
                return node.addresses.get(idx);  // Return the found Address
            } else if (node.isLeaf) {
                return null;  // Address not found and it's a leaf node
            } else {
                idx = -(idx + 1);  // Prepare to search in the correct child node
                node = getNode(node.childrenIds.get(idx));
            }
        }
        return null;  // Address not found in non-leaf nodes
    }

    public void insert(Address address) throws IOException, ClassNotFoundException {
        if (search(address)) {
            System.out.println("Duplicate address '" + address + "' not added.");
            return;
        }
        Node root = getNode(rootId);
        if (root.addresses.size() == 2 * t - 1) {
            Node newRoot = new Node(false);
            newRoot.childrenIds.add(root.nodeId);
            rootId = newRoot.nodeId;
            splitChild(newRoot, 0);
            insertNonFull(newRoot, address);
        } else {
            insertNonFull(root, address);
        }
        saveTreeState();
    }

    private void insertNonFull(Node node, Address address) throws IOException, ClassNotFoundException {
        int i = node.addresses.size() - 1;
        if (node.isLeaf) {
            node.addresses.add(address);
            Collections.sort(node.addresses);  // Keep the addresses sorted in the node based on first and last name
            saveNode(node);
        } else {
            while (i >= 0 && address.compareTo(node.addresses.get(i)) < 0) {
                i--;
            }
            i++;
            Node child = getNode(node.childrenIds.get(i));
            if (child.addresses.size() == 2 * t - 1) {
                splitChild(node, i);
                if (address.compareTo(node.addresses.get(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(getNode(node.childrenIds.get(i)), address);
        }
    }

    private void splitChild(Node parent, int i) throws IOException, ClassNotFoundException {
        Node node = getNode(parent.childrenIds.get(i));
        Node newNode = new Node(node.isLeaf);
        int mid = t - 1;
        newNode.addresses.addAll(node.addresses.subList(t, node.addresses.size()));
        node.addresses.subList(t, node.addresses.size()).clear();
        if (!node.isLeaf) {
            newNode.childrenIds.addAll(node.childrenIds.subList(t, node.childrenIds.size()));
            node.childrenIds.subList(t, node.childrenIds.size()).clear();
        }
        parent.addresses.add(i, node.addresses.get(mid));
        node.addresses.remove(mid);
        parent.childrenIds.add(i + 1, newNode.nodeId);
        saveNode(node);
        saveNode(newNode);
        saveNode(parent);
    }

    public void displayAllAddresses() throws IOException, ClassNotFoundException {
        Node root = getNode(rootId);
        int[] count = new int[1]; // Array to keep count as it is passed by reference
        traverseInOrderAndCount(root, count);
        System.out.println("\nTotal number of addresses: " + count[0]);
    }

    private void traverseInOrderAndCount(Node node, int[] count) throws IOException, ClassNotFoundException {
        if (node == null) return;

        // Traverse left to right, ensuring addresses are printed alphabetically
        for (int i = 0; i < node.addresses.size(); i++) {
            // Print each address
            System.out.println(node.addresses.get(i));
            count[0]++;  // Increment the count for each address printed
        }

        // Recursively traverse child nodes if not a leaf
        if (!node.isLeaf) {
            for (int i = 0; i < node.childrenIds.size(); i++) {
                Node childNode = getNode(node.childrenIds.get(i));
                traverseInOrderAndCount(childNode, count);
            }
        }
    }

    public void batchInsert(int count) throws IOException, ClassNotFoundException {
        int batchSize = 1000; // Process in smaller batches
        for (int i = 0; i < count; i++) {
            Address address = AddressGenerator.generateRandomAddress(i);
            insert(address);

            // Print progress every batch
            if (i % batchSize == 0) {
                System.out.println("Inserted " + i + " addresses...");
            }
        }
        System.out.println("Batch insertion complete. Total: " + count + " addresses.");
    }




    public static void main(String[] args) {
        try {
            BPlusTree bpt = new BPlusTree(101); // Use a larger `t` value for better performance with large data
        //   int addressCount = 50000; // 1 million addresses
         // System.out.println("Starting batch insertion of " + addressCount + " addresses...");
         // bpt.batchInsert(addressCount);
         //  System.out.println("All addresses inserted successfully!");

            // Perform a search to confirm functionality
           Address searchTarget = new Address("Simran_24310", "Bhatt_24310", "", "", "");
            Address found = bpt.getAddress(searchTarget);
         System.out.println("Search result for Bhatt_24310 " + (found != null ? found : "Not found"));

          //  bpt.displayAllAddresses();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
