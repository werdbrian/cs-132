echo "Generating parser ..."
cd parser;
javacc awk.jj > /dev/null;
cd - > /dev/null;

echo "Compiling parser ..."
javac Parse.java -classpath . > /dev/null
