package core;

//public aspect Aspect_Test {
//    before() : execution(float World.getRescuedRate()) {
//        System.out.println("Print every time when getRescuedRate method is called?");
//    }
//}


public aspect Aspect_Test {
    pointcut mexec() : execution(float World.getRescuedRate());

    float around() : mexec() {
        float returnValue = proceed();
        System.out.println("By Aspect, return value of method getRescuedRate(): " + returnValue);
        if(returnValue < 0.3)
            System.out.println("Verification Result: Fail!!!");
        else
            System.out.println("Verification Result: Success!!");
        return 0;
    }

}

