package org.shubham;

public class Laptop implements Computer {
    // constructor
    public Laptop() {
        System.out.println("Laptop constructor");
    }

    public void compile() {
        System.out.println("Compiling in laptop");
    }
}
