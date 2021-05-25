# Medical Office 

### Language: Java
external: Lombok, MySQLConnector/J


Database compatibility: MySQL.

Tabels create themselves on initalization of OfficeService. Ids are the hashcode which
has proven to be a mistake for the update possibilities are limited. Since the hascode is 
constituted of the primary fields, those cannot be changed, otherwise the foreign
keys will be invalidated. To avoid this database auto increment can be used.

### ERD
![alt text](https://github.com/IancuOnescu/Medical-Office/blob/master/MD%20images/diagram.png?raw=true)


Little demo can be found in main, uncommenting is required.

Actiunile/interogarile de la pasul 1 sunt in issues

Next: Diagrama conceptuala, refactoring, extra actiuni/interogari

![alt text](https://github.com/IancuOnescu/Medical-Office/blob/master/MD%20images/classes.png?raw=true)