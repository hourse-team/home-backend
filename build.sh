git pull origin master
mvn install -Dmaven.test.skip=true
cp ./target/*.jar ./
service mongod restart 
./spring-boot.sh restart
