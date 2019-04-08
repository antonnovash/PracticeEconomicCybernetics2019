import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.*;

public class Catalog extends JFrame {

    static DragNode addResult = null;
    private static String path = null;
    private JTable infoPanel = new JTable();
    private JTree notebooksTree = new JTree();
    private myTableModel tableModel = null;
    private myTreeModel treeModel = null;

    public Catalog() throws HeadlessException {
        JButton addButton = new JButton("Add drag");
        addButton.addActionListener(e -> SwingUtilities.invokeLater(() -> openAddDialog()));

        JButton removeButton = new JButton("Delete drag");
        removeButton.addActionListener(e -> removeItem());

        tableModel = new myTableModel();
        infoPanel = new JTable(tableModel);
        treeModel = new myTreeModel(new TreeNode("Store"));
        notebooksTree = new JTree(treeModel);
        notebooksTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreeNode node = (TreeNode) notebooksTree.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }
                ArrayList<DragNode> array = node.getAllNodes();
                tableModel = new myTableModel(array);
                infoPanel.setModel(tableModel);
            }
        });
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(notebooksTree), new JScrollPane(infoPanel));
        splitPane.setDividerLocation(300);


        getContentPane().add(splitPane);
        getContentPane().add("North", addButton);
        getContentPane().add("South", removeButton);
        setBounds(100, 100, 600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        Catalog mainClass = new Catalog();
        mainClass.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainClass.setVisible(true);
    }

    private void openAddDialog() {
        Add addForm = new Add(this);
        addForm.setVisible(true);
    }

    void addNewItem() {
        TreeNode temp, where, insert, root = treeModel.getRoot();
        String name = addResult.getName();
        String type = addResult.getType();
        try {
            insert = new TreeNode(addResult.getNumber(), addResult);
            if ((where = findNode(root, addResult.getManufacturer())) != null) {
                treeModel.insertNodeInto(insert, where, where.getChildCount(), false);
            } else if (findNode(root, name) != null) {
                treeModel.insertNodeInto(new TreeNode(addResult.getManufacturer()), (temp = findNode(root, addResult.getName())), Objects.requireNonNull(temp).getChildCount(), false);
                where = findNode(root, addResult.getManufacturer());
                treeModel.insertNodeInto(insert, where, Objects.requireNonNull(where).getChildCount(), false);
            } else if (findNode(root, type) != null) {
                treeModel.insertNodeInto(new TreeNode(addResult.getName()), (temp = findNode(root, addResult.getType())), Objects.requireNonNull(temp).getChildCount(), false);
                treeModel.insertNodeInto(new TreeNode(addResult.getManufacturer()), (temp = findNode(root, addResult.getName())), Objects.requireNonNull(temp).getChildCount(), false);
                where = findNode(root, addResult.getManufacturer());
                treeModel.insertNodeInto(insert, where, where != null ? Objects.requireNonNull(where).getChildCount() : 0, false);
            } else {
                treeModel.insertNodeInto(new TreeNode(addResult.getType()), root, root.getChildCount(), false);
                treeModel.insertNodeInto(new TreeNode(addResult.getName()), (temp = findNode(root, addResult.getType())), temp != null ? temp.getChildCount() : 0, false);
                treeModel.insertNodeInto(new TreeNode(addResult.getManufacturer()), (temp = findNode(root, addResult.getName())), temp != null ? temp.getChildCount() : 0, false);
                where = findNode(root, addResult.getManufacturer());
                treeModel.insertNodeInto(insert, where, where != null ? where.getChildCount() : 0, false);
            }
        } catch (Exception e) {
            path = null;
            addResult = null;
            return;
        }

        path = null;
        addResult = null;
    }

    private void removeItem() {
        TreePath currentSelection = notebooksTree.getSelectionPath();
        if (currentSelection != null) {
            TreeNode currentNode = (TreeNode) (currentSelection.getLastPathComponent());
            TreeNode parent = (TreeNode) (currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                parent.deleteNode(currentNode);
                ArrayList<DragNode> array = parent.getAllNodes();
                tableModel = new myTableModel(array);
                infoPanel.setModel(tableModel);
            }
        }
    }

    private TreeNode findNode(TreeNode root, String s) {
        Enumeration<TreeNode> e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            TreeNode node = e.nextElement();
            if (node.toString().equalsIgnoreCase(s)) {
                return node;
            }
        }
        return null;
    }
}

class myTreeModel extends DefaultTreeModel {

    private TreeNode root;

    myTreeModel(TreeNode r) {
        super(r);
        root = r;
    }

    @Override
    public TreeNode getRoot() {
        return root;
    }

    void insertNodeInto(TreeNode child, TreeNode parent, int i, boolean flag) {
        this.insertNodeInto(child, parent, i);
        parent.addNode(child);
    }
}

class myTableModel implements TableModel {

    private static final String[] columnNames = new String[]{"Number", "Name", "Type", "Manufacturer"};
    private static final Class[] columnTypes = new Class[]{String.class, String.class, String.class, String.class, String.class, Integer.class, Integer.class};
    private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
    private ArrayList<DragNode> infoNodes;

    myTableModel() {
        infoNodes = new ArrayList<DragNode>();
    }

    myTableModel(ArrayList<DragNode> al) {
        this.infoNodes = al;
    }

    public void setInfoArray(ArrayList<DragNode> al) {
        infoNodes = al;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return infoNodes.size();
    }

    public Class getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        DragNode nb = infoNodes.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return nb.getNumber();
            case 1:
                return nb.getName();
            case 2:
                return nb.getType();
            case 3:
                return nb.getManufacturer();
        }
        return "";
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
    }
}

class TreeNode extends DefaultMutableTreeNode {
    private String name;
    private DragNode ifNode = null;
    private ArrayList<TreeNode> nodes;
    private boolean isThisTheEnd = false;

    public TreeNode() {
        name = "-";
        nodes = new ArrayList<>();
        ifNode = null;
        isThisTheEnd = false;
    }

    TreeNode(String str) {
        name = str;
        nodes = new ArrayList<>();
        ifNode = null;
        isThisTheEnd = false;
    }

    TreeNode(String str, DragNode nbNode) {
        name = str;
        nodes = new ArrayList<>();
        ifNode = nbNode;
        isThisTheEnd = true;
    }

    ArrayList<DragNode> getAllNodes() {
        ArrayList<DragNode> ret = new ArrayList<>();
        Deque<TreeNode> deque = new ArrayDeque<>();

        TreeNode temp;
        deque.push(this);
        while (!deque.isEmpty()) {
            temp = deque.removeFirst();
            if (temp.isThisTheEnd) {
                ret.add(temp.getIfNode());
            } else {
                for (int i = 0; i < temp.nodes.size(); i++) {
                    deque.push(temp.nodes.get(i));
                }
            }
        }
        return ret;
    }

    void addNode(TreeNode tn) {
        nodes.add(tn);
    }

    void deleteNode(TreeNode tn) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).toString().compareToIgnoreCase(tn.toString()) == 0) {
                nodes.remove(i);
            }
        }
    }

    private DragNode getIfNode() {
        return ifNode;
    }

    public ArrayList<TreeNode> getNodes() {
        return nodes;
    }

    public String toString() {
        return name;
    }
}

class DragNode {
    private String number, type, name, manufacturer;


    DragNode() {
    }

    DragNode(String number, String type, String name, String manufacturer) {
        this.number = number;
        this.type = type;
        this.name = name;
        this.manufacturer = manufacturer;
    }


    String getNumber() {
        return this.number;
    }

    String getType() {
        return this.type;
    }

    String getName() {
        return this.name;
    }

    String getManufacturer() {
        return this.manufacturer;
    }
}