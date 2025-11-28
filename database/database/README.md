# Database Setup Instructions

## Quick Setup

### Option 1: Command Line (macOS/Linux)

1. Open Terminal
2. Navigate to the database folder:
```bash
   cd path/to/Assignment13/database
```
3. Import the database:
```bash
   mysql -u root -p < hibernate_example.sql
```
4. Enter your MySQL root password when prompted

### Option 2: MySQL Workbench

1. Open MySQL Workbench
2. Connect to your local MySQL server
3. Click **Server → Data Import**
4. Select **Import from Self-Contained File**
5. Browse to: `Assignment13/database/hibernate_example.sql`
6. Click **Start Import**

## Database Configuration

The application uses these database credentials (configured in `application.properties`):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hibernate_example
spring.datasource.username=example_user
spring.datasource.password=password123
```

## Create Database User (if needed)

If the `example_user` doesn't exist, run these commands in MySQL:
```sql
CREATE USER 'example_user'@'localhost' IDENTIFIED BY 'password123';
GRANT ALL PRIVILEGES ON hibernate_example.* TO 'example_user'@'localhost';
FLUSH PRIVILEGES;
```

## Database Schema

The application creates the following tables:

- **users** - User account information
- **address** - User addresses (One-to-One with users)
- **accounts** - Bank accounts
- **user_account** - Join table (Many-to-Many relationship)
- **transactions** - Transaction records

## Verify Setup

After importing, verify the database with:
```sql
USE hibernate_example;
SHOW TABLES;
SELECT * FROM users;
```