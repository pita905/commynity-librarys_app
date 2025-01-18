Screen Descriptions


Splash Screen
Title/Name/Role: Splash Screen
Description:
The Splash Screen is the initial screen displayed when the app launches. It provides a branding opportunity while the app initializes resources like databases or user sessions.
View Elements:
Logo/Image:
Purpose: Displays the app’s logo.
Duration: The screen is visible for 2-3 seconds before navigating to the Homepage or Login screen.
Activity Lifecycle:
onCreate: Starts a timer for the splash duration.
Homepage
Title/Name/Role: Homepage
Description:
The Homepage serves as the starting point for users. It provides two options: to log in or register.
View Elements:
Login Button:
Purpose: Navigates to the Login screen.
Register Button:
Purpose: Navigates to the Registration screen.
Register Screen
Title/Name/Role: Registration Screen
Description:
Allows new users to create an account by entering details like username, password, and email.
View Elements:
Username Field (Text Box):
Purpose: Collects the username.
Constraints: 3–15 characters, alphanumeric.
Password Field (Text Box):
Purpose: Collects the user’s password.
Constraints: Minimum 8 characters, must include a special character and a number.
Email Field (Text Box):
Purpose: Collects the user’s email address.
Validation: Checks for a valid email format.
Register Button:
Purpose: Saves the user’s details to the database and redirects to the Login screen.
Login Screen
Title/Name/Role: Login Screen
Description:
This screen allows users to authenticate using their username and password. If a user does not have an account, they can navigate to the Registration screen to create a new one. The screen validates input and provides feedback for incorrect credentials.
View Elements:
Username Field (Text Box):
Purpose: Accepts the user’s username.
Constraints: 3–15 characters, alphanumeric only.
Password Field (Text Box):
Purpose: Accepts the user’s password.
Constraints: Minimum of 8 characters, including one uppercase letter, one number, and one special character.
Masked Input: Password is hidden for security.
Login Button:
Purpose: Validates the entered credentials and redirects the user to the Dashboard on success.
Failure Action: Displays an error dialog (e.g., "Invalid username or password").
Register Button:
Purpose: Redirects to the Registration screen to create a new account.
Dashboard Screen
Title/Name/Role: Dashboard
Description:
The central hub of the app, showing user-specific data and quick navigation options to Profile, Search, and Settings screens.
View Elements:
Navigation Menu (Bottom Navigation):
Purpose: Provides quick access to Profile, and Settings.
Search Libraries Button:
Purpose: Opens the Search screen to browse available public libraries.
Admin panel button
Purpose: Opens the Admin screen to add new libraries.
Admin Panel
Title/Name/Role: Admin Panel
Description:
A restricted screen for administrators to manage libraries, add new libraries, and delete outdated ones.
View Elements:
Add Library Button:
Purpose: Opens a form to add a new library.
Fields: Library name, location.
Security:
Password Protected: The admin panel is hidden unless a password is entered.
Dialogs:
Confirmation Dialog: Prompts before performing critical actions like deleting a library.

Search Screen
Title/Name/Role: Search Libraries Screen
Description:
This screen allows users to search and explore public libraries in their city. Results are dynamically loaded based on the user’s input in the search bar.
View Elements:
Search Bar (Text Box):
Purpose: Allows users to enter a library name or location to filter results.
Library List (ListView):
Purpose: Displays libraries matching the search criteria.
Library Item (List Item):
Purpose: Shows details like the library's name and location.
Action: Clicking on an item opens the Library Details screen.
Library Details Screen
Title/Name/Role: Library Details Screen
Description:
Displays detailed information about a specific library, including its name, location, and a list of books available.
View Elements:
Library Name:
Purpose: Displays the name of the library.
Library Location:
Purpose: Displays the address or location of the library.
Books List (ListView):
Purpose: Displays all books associated with the library.
Add Book Button:
Purpose: Opens the Add Book activity.
Activity Lifecycle:
onResume: Restores scroll position or refreshes the list.
Add Book
Title/Name/Role: Add Book Dialog
Description:
Allows users to add a new book to a specific library.
View Elements:
Title Field (Text Box):
Purpose: Input the book’s title.
Author Field (Text Box):
Purpose: Input the author’s name.
Image Upload Button:
Purpose: Uploads an image for the book cover.
Save Button:
Purpose: Validates the input and saves the book details to the database.
Dialogs:
Error Message: Displays if required fields are empty.

Profile Screen
Title/Name/Role: Profile Screen
Description:
Displays the user’s profile information, including their username, email, and associated libraries. Users can also log out or edit their profile.
View Elements:
Profile Information Section:
Purpose: Displays the user’s username and email.
Edit Profile Button:
Purpose: Redirects to a screen where the user can update their details.
Logout Button:
Purpose: Logs the user out and redirects them to the Login screen.
Activity Lifecycle:
onPause: Clears any unsaved changes.
onResume: Reloads updated profile details if applicable.



Class Descriptions

1. SplashActivity
Department: UI/Navigation
Role: Handles the splash screen display and navigates to the main activity (HomeActivity) after a fixed delay.
Explanation of Features:
SPLASH_DURATION: A constant defining the duration (3 seconds) for the splash screen.
onCreate(): Initializes the activity, sets the layout, and navigates to HomeActivity after the splash duration using a Handler.
Explanation of Operations:
The primary operation is navigating from SplashActivity to HomeActivity after the splash duration.


2. SearchActivity
Department: UI/Search
Role: Provides the user interface for searching and viewing libraries from a database.
Explanation of Features:
edtSearchBar: EditText used to enter search queries.
listViewResults: ListView used to display library results.
dbHelper: A helper object used to interact with the database for fetching libraries.
Explanation of Operations:
onCreate(): Sets up the search functionality and handles user input for filtering library data.
fetchLibraries(): Queries the database and filters results based on user input.
Main Logical Operations:
Search Library: The search is triggered by the TextWatcher on edtSearchBar, filtering results based on library names or locations. Each time the query changes, the database is queried again.


3. RegisterActivity
Department: UI/User Management
Role: Provides the user interface for user registration.
Explanation of Features:
edtName, edtEmail, edtUsername, edtPassword: Input fields for user details.
btnRegister: Button for submitting registration data.
btnBack: Button to navigate back to the HomeActivity.
Explanation of Operations:
onCreate(): Sets up the registration form and handles input validation and submission to the database.
Main Logical Operations:
User Registration: Validates the input fields and registers the user in the database, ensuring unique usernames.


4. LoginActivity
Department: UI/User Management
Role: Handles user login functionality and navigation to the dashboard.
Explanation of Features:
edtUsername, edtPassword: Input fields for login credentials.
btnLogin: Button to submit login credentials.
btnBack: Button to go back to the home screen.
Explanation of Operations:
onCreate(): Handles user login validation and navigation to the DashboardActivity on successful login.
Main Logical Operations:
User Login: Verifies the credentials in the database and navigates to the DashboardActivity on success.


5. LibraryDetailsActivity
Department: UI/Library Management
Role: Displays details of a specific library, including its books.
Explanation of Features:
btnBack: Button to go back to the previous activity.
btnAddBook: Button to add books to the library.
txtLibraryName, txtLibraryLocation: Displays the name and location of the library.
listViewBooks: ListView to display books available in the library.
Explanation of Operations:
onCreate(): Fetches library details and displays books related to the library.
onResume(): Refreshes the list of books when returning to the activity.
Main Logical Operations:
Display Library Details: Displays detailed information about a selected library, including its books.


6. LibraryDatabaseHelper
Department: Database Management
Role: Provides methods for interacting with the library database (CRUD operations for libraries and books).
Explanation of Features:
DATABASE_NAME, DATABASE_VERSION: Configuration constants for the database.
TABLE_LIBRARIES, TABLE_BOOKS: Table definitions for storing libraries and books.
onCreate(), onUpgrade(): Methods for database creation and schema upgrades.
Explanation of Operations:
getAllLibraries(): Fetches all libraries from the database along with their associated books.
getBooksByLibraryId(): Retrieves books based on the library ID.
addLibrary(): Adds a new library to the database.
addBookToLibraryWithImage(): Adds a book to a library, including storing an image in Base64 format.
Main Logical Operations:
Fetching Libraries and Books: Fetches data about libraries and their books, including filtering and associating books to specific libraries by their IDs.


7. Library
Department: Data Models
Role: Represents a library, including its name, location, and list of books.
Explanation of Features:
name, location, books: Represents the name, location, and associated books of the library.
id: A unique identifier for each library.
Explanation of Operations:
Getter and setter methods for the fields (name, location, books, id).
Main Logical Operations: No complex operations, just basic data storage.
Database Description
Libraries Table:
Stores library information with columns for ID, name, and location.
Books Table:
Stores book information with columns for ID, title, author, ISBN, and a foreign key to the library it belongs to.

8. Database Helper Class
Database Initialization: Properly sets up an SQLite database with a table to store user details (username, password, email, name).
Methods:
validateUser: Validates user credentials by querying the database.
registerUser: Registers a new user by inserting their details into the database.
checkUserExists: Checks whether a given username exists in the database.
logAllUsers: Logs all users in the database for debugging.
getUserDetails: Retrieves user details based on a username.


9. Admin Panel Activity
UI Initialization: Sets up UI components for adding library information.
Intent Handling: Passes and receives data (such as username) through intents to manage user sessions.
Add Library Logic: Handles adding a library to the database and provides feedback to the user.

9. Add Book Activity
Image Handling: Manages the selection and conversion of book cover images into Base64 format.
Permissions: Ensures the app has the necessary permissions to access storage and camera for image selection.
Database Interaction: Uses LibraryDatabaseHelper to add book information (with image) to the database.

