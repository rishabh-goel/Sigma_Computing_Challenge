Sigma is a cloud analytics solution with a spreadsheet-like UI, which enables anyone to explore data at cloud scale and speed. Currently, users can save their explorations in documents called “worksheets”. They can also import charts and data visualizations into interactive documents called “dashboards”. Naturally, all these files need to be organized into folders, which are easily navigable using a file system.
For this take home question, you’ll be modeling some operations in what happen in Sigma’s file system. To simplify the problem, we’ll assume the file system has the following rules:
1. The file system contains Files.
2. Files can be either Folders or Documents.
3. Documents can be Dashboards or Worksheets.
4. Folders can contain Files.
5. Files will always have a name and unique id.
6. There is a root folder called “MyDocuments”.
   You should use a data structure of your choosing to maintain the state of the file system.
   Although files typically have some type of content (i.e. text, images, code), you don’t need to concern yourself with that for this problem. You only need to keep track of each file’s name and unique id.
   We’ve provided you with a main function that runs a command line program. This program supports the following commands:
7. get_total_dashboards: returns the total # of dashboards in the file system.
8. get_total_worksheets: returns the total # of worksheets in the file system.
9. add_new_file <fileName> <fileType> <folderId>: Given a file name, file type (folder, dashboard, or worksheet), and a folder’s unique id, add a new file of that type to the given folder.
10. get_file_id <fileName> <folderId>: Given the name of a file and the id of the folder it’s in (not required for “MyDocuments”), return the file’s id.
11. move_file <fileId> <newFolderId>: Given a file id and a folder id, move the file into the folder with the given id.
12. get_files <folderId>: Given a folder, return all the names of all files in that folder.
13. print_files: Print out each file in the file system, showing the nested structure.

We’ve provided function definitions for each of these functions. Your job is to implement the functions so that the command line program runs as expected. We encourage you to provide test cases for your functions, or describe how you tested your program. Please add comments providing any assumptions you’ve made and describing any limitations your implementation has.
We’ve also provided a run_example function that exercises these functions. Think of this as a kind of simple test case that we might run to check your implementation.
    
You’ll be graded on the following criteria:
1. Correctness
2. Testing
3. Code Structure
   
We value your time, so we don’t expect you to spend more than 2-3 hours on this project. You’ll have the opportunity to discuss “TODOs” and future optimizations with one of our engineers in your first interview. We care about your thought process more than your ability to build perfect, production-ready code in a few hours.
Please contact colleen@sigmacomputing.com if you have any questions about the assignment.
P.S. If you’re curious to see what the file system looks like in our product, we encourage you to try a free trial here: https://app.sigmacomputing.com/get-started. Don’t be scared to put a fake company name into the form.
