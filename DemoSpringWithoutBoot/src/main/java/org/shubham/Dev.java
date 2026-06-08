package org.shubham;

public class Dev {

    private Computer comp;
    // private int age;

    // constructor
    public Dev() {
        System.out.println("Dev constructor");
    }


//    public int getAge() {
//        return age;
//    }

//    public void setAge(int age) {
//        this.age = age;
//    }

    public Computer getComp() {
        return comp;
    }

    public void setComp(Computer comp) {
        this.comp = comp;
    }

    public void build() {

        System.out.println("working on Awesome project!!");
        comp.compile();
    }
}
