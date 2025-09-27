package lk.ijse.elitedrivingschool.bo.custom.impl;

import lk.ijse.elitedrivingschool.bo.custom.InstructorBo;
import lk.ijse.elitedrivingschool.dao.custom.InstructorDao;
import lk.ijse.elitedrivingschool.dao.custom.impl.InstructorDaoImpl;
import lk.ijse.elitedrivingschool.dto.InstructorDto;
import lk.ijse.elitedrivingschool.entity.Instructor;

import java.util.List;
import java.util.stream.Collectors;

public class InstructorBoImpl implements InstructorBo {

    private final InstructorDao instructorDao = new InstructorDaoImpl();

    @Override
    public void addInstructor(InstructorDto instructorDto) {
        instructorDao.save(mapToEntity(instructorDto));
    }

    @Override
    public InstructorDto getInstructorById(Integer id) {
        return instructorDao.findById(id).map(this::mapToDto).orElse(null);
    }

    @Override
    public List<InstructorDto> getAllInstructors() {
        return instructorDao.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void updateInstructor(InstructorDto instructorDto) {
        Instructor instructor = mapToEntity(instructorDto);
        instructor.setInstructorId(instructorDto.getInstructorId());
        instructorDao.update(instructor);
    }

    @Override
    public void deleteInstructor(Integer id) {
        instructorDao.findById(id).ifPresent(instructorDao::delete);
    }

    @Override
    public List<InstructorDto> getInstructorsBySpecialization(String specialization) {
        return instructorDao.findAll().stream()
                .filter(i -> specialization.equals(i.getSpecialization())) // Directly compare with specialization
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private InstructorDto mapToDto(Instructor instructor) {
        return new InstructorDto(
                instructor.getInstructorId(),
                instructor.getName(),
                instructor.getContactNumber(),
                instructor.getEmail(),
                instructor.getSpecialization()
        );
    }

    private Instructor mapToEntity(InstructorDto instructorDto) {
        return new Instructor(
                instructorDto.getInstructorId(),
                instructorDto.getName(),
                instructorDto.getContactNumber(),
                instructorDto.getEmail(),
                instructorDto.getSpecialization()
        );
    }
}