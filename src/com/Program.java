package com;

public class Program {

    public static void main(String[] args) {
        Glob glob = Glob.compile("abc?");
        System.out.println(glob.matches("abc")); //false => ? means exactly one character
        System.out.println(glob.matches("abcd")); //true
        System.out.println(glob.matches("abcde"));
        
        Glob glob2 = Glob.compile("a*d");
        System.out.println(glob2.matches("abcd"));
        System.out.println(glob2.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd"));
        System.out.println(glob2.matches("abcdej"));
        System.out.println(glob2.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd1"));
        
        Glob glob3 = Glob.compile("*.html");
        System.out.println(glob3.matches("index.html"));
        System.out.println(glob3.matches("index.htm")); //false - missing 'l'
        System.out.println(glob3.matches("directory/index.html")); //fa
        
        Glob glob4 = Glob.compile("Di{nko,mitur}");
        System.out.println(glob4.matches("Dimitur"));
        System.out.println(glob4.matches("Dinko")); //false - missing 'l'
        System.out.println(glob4.matches("Divna")); //false - crossing directory boundaries
        
        Glob glob5 = Glob.compile("/home/georgi/**index.html");
        System.out.println(glob5.matches("/home/georgi/testme/testme2/index.html"));
        System.out.println(glob5.matches("/home/georgi/testme/testme2/testME_index.html")); //false - missing 'l'
        System.out.println(glob5.matches("/home/index.html")); //false - crossing directory boundaries
    }

}
