# File Sorter

## Project Overview

The File Sorter program is a Java application designed to automatically organize files based on specific rules. It monitors a directory for new files and moves them to designated folders according to their extensions and creation times. The project also maintains a count of the number of files moved in different categories.

### Features

- **Directory Structure**:
    - Creates a directory structure with three folders: `HOME`, `DEV`, and `TEST`.

- **File Sorting Rules**:
    - **.jar files**:
        - If the file's creation hour is even, it is moved to the `DEV` folder.
        - If the file's creation hour is odd, it is moved to the `TEST` folder.
    - **.xml files**:
        - Always moved to the `DEV` folder.

- **Count Tracking**:
    - Maintains a file `/home/count.txt` that records the number of files moved, including a breakdown by category.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Setup

1. **Clone the Repository**

  ```
  git clone https://github.com/yourusername/file-sorter.git
```
2. **Build project**
```
mvn clean install  
```
3. **Run project**
To run project : 
```
mvn exec:java
```