package com.mysolution;

/*
 * Click `Run` in the top left corner to run the command line program. Output will appear in the "Program Output" tab in the right pane.
 */

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SigmaFileSystem {



    public static class TreeNode{
        private String id;                  // Store ID of the file
        private String name;                // Store name of the file
        private String type;                // Store type of the file
        private String folderId;            // Store which folder the file belongs to
        private Set<TreeNode> children;     // Store the inner contents of the file

        // Create a new dashboard/worksheet
        TreeNode(String fileId, String fileName, String fileType, String folderId){
            this.id = fileId;
            this.name = fileName;
            this.type = fileType;
            this.folderId = folderId;
        }

        // Create a new folder
        TreeNode(String fileId, String fileName, String fileType, String folderId, Set<TreeNode> children){
            this.id = fileId;
            this.name = fileName;
            this.type = fileType;
            this.folderId = folderId;
            this.children = children;
        }

        // Get ID of the calling file
        public String getID(){
            return this.id;
        }

        // Get name of the calling file
        public String getName(){
            return this.name;
        }

        // Set folder id of the calling file
        public void setFolderID(String folderId){
            this.folderId = folderId;
        }

        // Get folder id of the calling file
        public String getFolderID(){
            return this.folderId;
        }

        // Get type of the calling file
        public String getType(){
            return this.type;
        }

        // Get inner contents of the calling file
        public Set<TreeNode> getChildren(){
            return this.children;
        }

        // Add file to the calling folder
        void addChild(TreeNode node)
        {
            this.children.add(node);
        }
    }

    TreeNode root;                                                                  // Points to the root folder i.e MyDocuments
    private int newFolderId = 0;                                                    // Counter to store folder ID
    private int newDocId = 0;                                                       // Counter to dashboard/worksheet ID
    public static final String FILE_NOT_FOUND_MSG = "File Not Found";               // Error msg to show file was not found
    private static final String DUPLICATE_FILE_FOUND_MSG = "File Already Exists";   // Error msg to show duplicate file exists

    //  Find location of the folder using its folder ID
    // Optional interface used to handle null references
    Optional<TreeNode> findFolder(String folderId)
    {
        // Level order traversal to find the folder based on folderID
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode currNode = queue.poll();

                if(currNode.getID().equals(folderId)){
                    return Optional.of(currNode);
                }

                if(currNode.getType().equals("folder")) {
                    queue.addAll(currNode.getChildren());
                }
            }
        }

        return Optional.empty(); // If folder not found, return empty Optional object
    }

    // Optional interface used to handle null references
    Optional<String> findFile(String fileId)
    {
        // Level order traversal to find the folder based on fileID
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode currNode = queue.poll();

                if(currNode.getID().equals(fileId)) {
                    return Optional.of(currNode.getFolderID());
                }

                if(currNode.getType().equals("folder")) {
                    queue.addAll(currNode.getChildren());
                }
            }
        }

        return Optional.empty(); // If folder not found, return empty Optional object
    }

    // Constructor to create root directory
    public SigmaFileSystem() {
        root = new TreeNode("folder_"+newFolderId, "MyDocuments", "folder", "folder_" + newFolderId, new HashSet<>());
    }

    // Feel free to modify the parameter/return types of these functions
    // as you see fit. Please add comments to indicate your changes with a
    // brief explanation. We care more about your thought process than your
    // adherence to a rigid structure.

    // Helper method to get count of dashboard/worksheet based on the type of file
    int getTotalCount(String fileType)
    {
        int count = 0;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                TreeNode currNode = queue.poll();

                if(currNode.getType().equals(fileType)) {
                    count++;
                }

                if(currNode.getType().equals("folder")) {
                    queue.addAll(currNode.getChildren());
                }
            }
        }

        return count;
    }

    // Method to get total count of dashboards
    int getTotalDashboards() {
        return getTotalCount("dashboard");
    }

    // Method to get total count of worksheets
    int getTotalWorksheets() {
        return getTotalCount("worksheet");
    }

    // Method to add a new file based on its name, file type and folder it needs to be added to
    void addNewFile(String fileName, String fileType, String folderId) {

        // Get the folder the new file needs to be added to
        Optional<TreeNode> parentFolder = findFolder(folderId);
        if(parentFolder.isEmpty()){
            throw new Error(FILE_NOT_FOUND_MSG);
        }

        // Check if a file already exists with the same name in the current folder
        for(TreeNode currentFile : parentFolder.get().getChildren()){
            if(currentFile.getName().equals(fileName)){
                throw new Error(DUPLICATE_FILE_FOUND_MSG);
            }
        }

        TreeNode node;
        // Folder will be created with a Hashset to store inner contents. Worksheet/Dashboards created without a Hashset to store inner contents
        if(fileType.equals("folder")) {
            node = new TreeNode("folder_" + ++newFolderId, fileName, fileType, folderId, new HashSet<>());
        }
        else {
            node = new TreeNode("doc_" + ++newDocId, fileName, fileType, folderId);
        }

        // New item added as a content of the folder
        parentFolder.get().addChild(node);
    }

    // Method to get the file ID based on the filename and the folder ID it is stored in
    Optional<String> getFileId(String fileName, String folderId) {
        Optional<TreeNode> parentFolder = findFolder(folderId);

        // Check if the folder exists
        if(parentFolder.isEmpty()) {
            throw new Error(FILE_NOT_FOUND_MSG);
        }

        // Return folder id if the requested item is a folder
        if(parentFolder.get().getName().equals(fileName)) {
            return Optional.of(parentFolder.get().getID());
        }

        // Check for item in the current folder
        for(TreeNode node : parentFolder.get().getChildren()) {
            if(node.getName().equals(fileName)) {
                return Optional.of(node.getID());
            }
        }

        return Optional.ofNullable(null); // If folder not found, return empty Optional object
    }

    // Method to move file from 1 location to another
    void moveFile(String fileId, String newFolderId) {

        Optional<TreeNode> newFolder = findFolder(newFolderId);
        // Check if the destination folder exists
        if(newFolder.isEmpty())
            throw new Error(FILE_NOT_FOUND_MSG);

        Optional<String> fileLocation = findFile(fileId);
        // Check if we are able to find the file
        if(fileLocation.isEmpty()){
            throw new Error(FILE_NOT_FOUND_MSG);
        }

        Optional<TreeNode> currentFolder = findFolder(fileLocation.get());
        // Check if the source folder exists
        if(currentFolder.isEmpty())
            throw new Error(FILE_NOT_FOUND_MSG);

        String filename = "";
        // Get the name of the file to be moved
        for(TreeNode currentFolderNode : currentFolder.get().getChildren()){
            if(currentFolderNode.getID().equals(fileId)){
                filename = currentFolderNode.getName();
                break;
            }
        }

        // Check if the file already exists in the destination folder
        for(TreeNode newFolderNode : newFolder.get().getChildren()){
            if(newFolderNode.getName().equals(filename)){
                throw new Error(DUPLICATE_FILE_FOUND_MSG);
            }
        }

        // Move the file from source to destination folder and change the folder ID of the moving file
        for(TreeNode node : currentFolder.get().getChildren()) {
            if(node.getID().equals(fileId)) {
                currentFolder.get().getChildren().remove(node);
                newFolder.get().getChildren().add(node);
                node.setFolderID(newFolder.get().id);
                break;
            }
        }
    }

    // Method to get files in the folder based on folder ID
    String[] getFiles(String folderId) {
        List<String> files = new ArrayList<>();
        Optional<TreeNode> parentFolder = findFolder(folderId);

        if(parentFolder.isEmpty()){
            throw new Error(FILE_NOT_FOUND_MSG);
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(parentFolder.get());

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode currNode = queue.poll();
                files.add(currNode.getName());

                if(currNode.getType().equals("folder")) {
                    queue.addAll(currNode.getChildren());
                }
            }
        }

        String[] fileNames = files.toArray(new String[0]);

        return fileNames;
    }

    // Helper method to print files in a hierarchical format
    // DFS to iterate in a n-ary tree
    void collect(TreeNode myRoot, int level) {
        System.out.println("\t".repeat(level) + " - "+ myRoot.getName());

        if(myRoot.getType().equals("folder")) {
            for (TreeNode node : myRoot.getChildren()){
                collect(node, level+1);
            }
        }

        return;
    }

    // Method to print files in a hierarchical format
    void printFiles() {
        collect(root, 0);
    }

}



/////////////////////////////////////////////////////////
// YOU DO NOT NEED TO MAKE CHANGES BELOW UNLESS NECESSARY
/////////////////////////////////////////////////////////


/*
 * Note from Coder Pad: To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */
class Solution {

    private static int askForInteger(Scanner scanner, String question) {
        System.out.println(question);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Please enter a valid integer.");
            return askForInteger(scanner, question);
        }
    }

    public static void runExample() {
        SigmaFileSystem fs = new SigmaFileSystem();
        Optional<String> rootId = fs.getFileId("MyDocuments", "folder_0");
        fs.addNewFile("draft", "folder", rootId.get());
        fs.addNewFile("complete", "folder", rootId.get());
        Optional<String> draftId = fs.getFileId("draft", rootId.get());
        Optional<String> completeId = fs.getFileId("complete", rootId.get());
        fs.addNewFile("foo", "worksheet", draftId.get());
        fs.addNewFile("bar", "dashboard", completeId.get());
        fs.printFiles();
        Optional<String> fooId = fs.getFileId("foo", draftId.get());
        fs.moveFile(fooId.get(), completeId.get());

        System.out.println(String.join(", ", fs.getFiles(rootId.get())));
        System.out.println(String.join(", ", fs.getFiles(draftId.get())));
        System.out.println(String.join(", ", fs.getFiles(completeId.get())));


        fs.addNewFile("project", "folder", draftId.get());
        Optional<String> projectId = fs.getFileId("project", draftId.get());
        fs.addNewFile("page1", "worksheet", projectId.get());
        fs.addNewFile("page2", "worksheet", projectId.get());
        fs.addNewFile("page3", "worksheet", projectId.get());
        fs.addNewFile("cover", "dashboard", projectId.get());
        fs.moveFile(projectId.get(), completeId.get());
        projectId = fs.getFileId("project", completeId.get());
        Optional<String> coverId = fs.getFileId("cover", projectId.get());
        fs.moveFile(coverId.get(), rootId.get());

        System.out.println(String.join(", ", fs.getFiles(rootId.get())));
        System.out.println(String.join(", ", fs.getFiles(draftId.get())));
        System.out.println(String.join(", ", fs.getFiles(completeId.get())));
        System.out.println(String.join(", ", fs.getFiles(projectId.get())));

        System.out.println(fs.getTotalDashboards());
        System.out.println(fs.getTotalWorksheets());
        fs.printFiles();
    }

    public static void runErrorTestcases() {
        SigmaFileSystem fs = new SigmaFileSystem();
        Optional<String> rootId = fs.getFileId("MyDocuments", "folder_0");

        // Adding a sub folder in a folder that doesn't exist
        Error e1 = assertThrows(Error.class, () -> {fs.addNewFile("draft", "folder", String.valueOf(1));}, "File Not Found");
        assertEquals("File Not Found", e1.getMessage());

        // Trying to add duplicate folder
        fs.addNewFile("draft", "folder", rootId.get());
        Error e2 = assertThrows(Error.class, () -> {fs.addNewFile("draft", "folder", rootId.get());}, "File Already Exists");
        assertEquals("File Already Exists", e2.getMessage());

        // Trying to move 1 file from 1 folder to another where file with same name already exists
        fs.addNewFile("complete", "folder", rootId.get());
        Optional<String> draftId = fs.getFileId("draft", rootId.get());
        Optional<String> completeId = fs.getFileId("complete", rootId.get());
        fs.addNewFile("foo", "worksheet", draftId.get());
        fs.addNewFile("foo", "dashboard", completeId.get());
        Optional<String> fooId = fs.getFileId("foo", draftId.get());
        Error e3 = assertThrows(Error.class, () -> {fs.moveFile(fooId.get(), completeId.get());}, "File Already Exists");
        assertEquals("File Already Exists", e3.getMessage());

        // Trying to move 1 file from 1 folder to another and the new folder doesn't exist
        Error e4 = assertThrows(Error.class, () -> {fs.moveFile(fooId.get(), String.valueOf(10));}, "File Not Found");
        assertEquals("File Not Found", e4.getMessage());
    }

    // Feel free to modify this main function as you see fit.
    public static void main(String[] args) {
        runExample();
        runErrorTestcases();
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        SigmaFileSystem fs = new SigmaFileSystem();
        int command;
        while (running) {
            command = askForInteger(scanner, "\nEnter an integer to indicate a command: \n[1] get_total_dashboards\n[2] get_total_worksheets\n[3] add_new_folder\n[4] get_file_id\n[5] move_file\n[6] get_files \n[7] print_files\n[8] exit\n");
            switch (command) {
                case 1: {
                    int totalDashboards = fs.getTotalDashboards();
                    System.out.println(String.format("There are %d dashboards in the file system.", totalDashboards));
                    break;
                }
                case 2: {
                    int totalWorksheets = fs.getTotalWorksheets();
                    System.out.println(String.format("There are %d worksheets in the file system.", totalWorksheets));
                    break;
                }
                case 3: {
                    System.out.println("Enter a new file name:");
                    String fileName = scanner.nextLine();
                    System.out.println("Enter a file type (worksheet, dashboard, or folder)");
                    String fileType = scanner.nextLine();
                    int folderId = askForInteger(scanner, "Enter a folder id where you'd like to put this file");
                    fs.addNewFile(fileName, fileType, "folder_" + Integer.toString(folderId));
                    System.out.println(String.format("%s has been added to folder %d", fileName, folderId));
                    break;
                }
                case 4: {
                    System.out.println("Enter a file name:");
                    String fileName = scanner.nextLine();
                    int folderId = askForInteger(scanner, "Enter a folder id:");
                    Optional<String> fileId = fs.getFileId(fileName, "folder_" + Integer.toString(folderId));
                    System.out.println(String.format("%s is file %s", fileName, fileId.get()));
                    break;
                }
                case 5: {
                    System.out.println("Enter the type of file you want to move(folder or document):");
                    String fileType = scanner.nextLine();
                    int fileId = askForInteger(scanner, "Enter a file id:");
                    int newFileId = askForInteger(scanner, "Enter the folder id where you'd like to move this file.");
                    if(fileType.equals("folder")) {
                        fs.moveFile("folder_" + Integer.toString(fileId), "folder_" + Integer.toString(newFileId));
                    }
                    else {
                        fs.moveFile("doc_" + Integer.toString(fileId), "folder_" + Integer.toString(newFileId));
                    }
                    System.out.println(String.format("Successfully moved file %s to folder %s", fileId, newFileId));
                    break;
                }
                case 6: {
                    int folderId = askForInteger(scanner, "Enter a folder id:");
                    String[] fileNames = fs.getFiles("folder_" + Integer.toString(folderId));
                    if (fileNames.length == 0) {
                        System.out.println(String.format("There are no files in folder %d", folderId));
                    } else {
                        System.out.println(String.format("The following files are in folder %d:", folderId));
                        for (String fileName: fileNames) {
                            System.out.println(String.format("\t%s", fileName));
                        }
                    }
                    break;
                }
                case 7: {
                    fs.printFiles();
                    break;
                }
                case 8: {
                    System.out.println("Exiting program.");
                    running = false;
                    scanner.close();
                    break;
                }
                default:
                    System.out.println(String.format("Invalid command: %d. Please try again.\n",command));
            }
        }
    }


}

