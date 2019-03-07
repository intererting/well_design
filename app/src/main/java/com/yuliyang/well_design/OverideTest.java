package com.yuliyang.well_design;

public class OverideTest {
}

class FatherJava {
    protected String say(String name) throws RuntimeException {
        throw new RuntimeException("haha");
//        return "FatherTest";
    }
}

class SonJava extends FatherJava {

    @Override
    public String say(String name) {
        return "SonTest";
    }
}


