## Codeflex

### Setup application

1. #### Clone repository
        git clone git@github.com:miguelfbrito/Codeflex.git

2. #### Run setup scripts (install npm, node, mysql)
        cd /setup/
        ./setup.sh

3. #### Create MySQL database
        
        sudo mysql --password
        create database db_codeflex;
        create user 'admin'@'localhost' identified by 'spring-password!2018';
        grant all on db_codeflex.* to 'admin'@'localhost';

    Change sql mode:
        
        vi /etc/mysql/conf.d/mysql.cnf
        sql_mode = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION'
        sudo mysql restart


### Run application
    
1. #### Start frontend
        cd /client/
        npm start   

2. #### Start backend
        cd /server/
        mvn clean
        mvn package
        cd target/ && java -jar web-0.0.1-SNAPSHOT.war
