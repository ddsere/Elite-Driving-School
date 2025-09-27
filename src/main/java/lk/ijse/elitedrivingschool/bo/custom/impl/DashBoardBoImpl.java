package lk.ijse.elitedrivingschool.bo.custom.impl;

import lk.ijse.elitedrivingschool.bo.custom.DashBoardBo;
import lk.ijse.elitedrivingschool.dao.custom.CourseDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.CourseDaoImpl;
import lk.ijse.elitedrivingschool.dao.custom.InstructorDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.InstructorDaoImpl;
import lk.ijse.elitedrivingschool.dao.custom.StudentDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.StudentDaoImpl;

public class DashBoardBoImpl implements DashBoardBo {

    private final StudentDao studentDao = new StudentDaoImpl();
    private final InstructorDao instructorDao = new InstructorDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();

    @Override
    public long getStudentCount() {
        return studentDao.findAll().size();
    }

    @Override
    public long getInstructorCount() {
        return instructorDao.findAll().size();
    }

    @Override
    public long getCourseCount() {
        return courseDao.countAllCourses();
    }
}
