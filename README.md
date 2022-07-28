# E-Commerce Website Backend and Fronted

 ## Summary
An online perfume sales site that uses springboot and postgresql on the backend and html css and javascript on the front. As it is a trial project, the site does not have real purchasing functions, but the frontend is still interactive. If you want to see the site with its products, I also share the data with you.
 
 ## How to run?
 - Database Steps
   - First you need to [install PostgreSQL](https://www.postgresql.org/) to your system.
   - Create an local account and set a `name` and `password`.
   - Use the csv data i shared in [CSV to SQL](https://www.convertcsv.com/csv-to-sql.htm) to get the data in sql format.
   - Add the tables and the data to your database.
 
 - Springboot Steps
   - All you need to do is go to `perfume/src/main/resources/application.properties` and change the `username` and `password` to your PostgreSQL name and password.
 
 - Last Steps
   - Run the `perfume/src/main/java/PerfumeApplication.java` file.
   - Open any .html file in the web file (preferably home.html).
   - Enjoy!
  
> *The server will run on localhost `port:8080` you can change this in application.properties file. Keep in mind that if you change this you need to change the ports in all javascript files.*

> *Components of the web page may appear too large on some screens and may overflow. You can set the correct dimensions by doing `CTRL` + `-` on your web page.*
