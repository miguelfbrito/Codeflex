# Client installation
sudo apt install -y nodejs npm
sudo npm install -g npm

cd ../client/
npm i -S

# Server installation
sudo apt install -y maven
sudo apt install -y mysql-server

# Create mysql database and users
# Settings defined on application.properties
sudo mysql --user=root --password=root --execute="create database db_codeflex;"
sudo mysql --user=root --password=root --execute="create user 'admin'@'localhost' identified by 'spring-password!2018';"
sudo mysql --user=root --password=root --execute="grant all on db_codeflex.* to 'admin'@'localhost';"

cd ../server/
mvn

