package lk.ijse.elitedrivingschool.bo;

import lk.ijse.elitedrivingschool.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getInstance() {
        return boFactory == null ? (boFactory = new BOFactory()) : boFactory;
    }

    @SuppressWarnings("unchecked")
    public <T extends SuperBO> T getBO(BOTypes boType) {
        return switch (boType) {
            case COURSE -> (T) new CourseBoImpl();
            case DASHBOARD -> (T) new DashBoardBoImpl();
            case INSTRUCTOR -> (T) new InstructorBoImpl();
            case LESSON -> (T) new LessonBoImpl();
            case PAYMENT -> (T) new PaymentBoImpl();
            case STUDENT -> (T) new StudentBoImpl();
            case USER -> (T) new UserBoImpl();
        };
    }
}