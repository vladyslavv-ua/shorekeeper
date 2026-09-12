# Shorekeeper

Shorekeeper is a rapid application development framework for building data-driven GUI applications

## Using
(currently under development)
1. Clone the repo
2. Build the project
3. Download JCEF from [here](https://github.com/jcefmaven/jcefbuild/releases) for your platform
4. Extract the lib/yourplatform folder to the desired location
5. Download JDBC driver for your database to the 'jdbc-drivers' folder in the executable folder
6. Download your favourite migration tool (supported liquibase)
7. Run shorekeeper
8. Set up paths on the settings page
9. Create a new project
10. Write design using the default web stack (HTML, CSS, JS) or your favourite js framework
11. Write migrations for your db
12. Execute migrations
13. Write SQL queries to your db
14. Using window.cefQuery or [wrapper](https://github.com/vladyslavv-ua/shorekeeper-wrapper) join queries to html
15. Enjoy


## Shorekeeper vs alternatives

| Criteria           | MS access                              | LibreOffice base                         | Shorekeeper                                   |
|--------------------|----------------------------------------|------------------------------------------|-----------------------------------------------|
| Price              | Paid                                   | Open source                              | Open source                                   |
| DB Support         | Proprietary db, ODBC compliant drivers | Own db, RDMS with JDBC compliant drivers | RDMS with JDBC compliant drivers              |
| Design             | Desktop WYSIWYG Forms                  | Desktop WYSIWYG Forms                    | Using Web stack                               |
| Design flexibility | Poor                                   | Poor                                     | Rich — any web framework, full markup control |
| Logic language     | VBA                                    | LO Basic, Python, JS, BeanShell          | JS                                            |
| Platforms          | Windows only                           | Cross platform                           | Cross platform                                |