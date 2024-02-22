# CCC151---Simple-Student-Information-System

> # Description
> This is the repository for the 2nd Assignment for the subject CCC151.
> 
> ## Time Frame
> Deadline: Feb. 23, 2024 <br>
> Starting: Jan. 31, 2024 
> 
> ## Author
> Caine Ivan R. Bautista (2022-0378) <br>
> 2nd Year - BS Computer Science 

# Reminders
- Avoid directly editing/manipulating/deleting the CSV files to prevent errors. The app is not designed to handle these errors as it treats the CVS files as databases that can't be touched.
- The tables can only (as of the moment) display relationship between data found in the CSV files, add, edit, and delete data/row, check for duplicates, filter data/row. Other functionalities found in some databases are not supported.
- The project is finished, but the designs/functionalities still may or may not be subject to change.

# How to use
- Upon loading the app, the tables and functionality buttons are all present and in view.
- To display the student or course table, click the buttons "Student" and "Course" based on what table you want to be displayed.
- Adding items (student or course) are processed by clicking the "Add Item" button. Fill all necessary information to add the item.
- Editing items are processed by selecting first the row (or item) you want to edit then clicking the "Edit Item" button. Leave no empty information to finish editing.
- Deleting items are processed by selecting first the row to be deleted then clicking the "Delete Item" button. Confirm to finalize the delete process.
- Saving the tables to their respective CSV files are done by clicking the "Save Tables" button. Also, when closing the app, a message with popup asking if you want to save the tables, click "Yes" to save then exit, click "No" to just exit, and click "Cancel" to cancel the exit.
- The tables are auto-sorted based on the first column (index 0), if you want to sort them based on different columns, click on the column headers/names. They will be sorted in ascending or descending order.
- Filtering rows are processed by selecting the column of the data you want to search/filter using the combo box, then entering the data in the text field, finally clicking the "Search" button.
- Some if not all actions have a popup notifying/warning you of what they will do, just confirm or cancel.
- To count how many items are listed in the tables, right-click the tables then select "Student Count" for the student table and "Course Count" for the course table.
- To count how many students are unenrolled, right-click the course table then select "Unenrolled Students".
- To count how many students are enrolled in a course, select a course, right-click the course table, then select "Student Enrolled".