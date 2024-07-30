package org.springframework.samples.petclinic.service.clinicService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.SpecialtyMapper;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.rest.controller.SpecialtyRestController;
import org.springframework.samples.petclinic.rest.dto.SpecialtyDto;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SpecialtyRestControllerTest {
    @Mock
    private ClinicService clinicService;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private SpecialtyRestController specialtyRestController;

    public static Specialty arrangeSeciality(){
        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("especiality1");
        return specialty;
    }

    public static SpecialtyDto arrangeSecialityDto(){
        SpecialtyDto specialtyDto = new SpecialtyDto();
        specialtyDto.setId(1);
        specialtyDto.setName("especiality1");
        return specialtyDto;
    }


    @Test
    public void listSpecialtiesSuccessTest() {
        //Arrange

        Collection<Specialty> espec = Arrays.asList(arrangeSeciality());
        Collection<SpecialtyDto> especDto = Arrays.asList(arrangeSecialityDto());

        when(clinicService.findAllSpecialties()).thenReturn(espec);

        when(specialtyMapper.toSpecialtyDtos(anyCollection())).thenReturn(especDto);
        //Act
        ResponseEntity<List<SpecialtyDto>> listSpecialities =  specialtyRestController.listSpecialties();
        //Assert
        assertEquals(HttpStatus.OK,listSpecialities.getStatusCode());
        assertEquals(especDto,listSpecialities.getBody());
        assertEquals(espec.size(),listSpecialities.getBody().size());
        SpecialtyDto expectedDto = especDto.iterator().next();
        SpecialtyDto actualDto = listSpecialities.getBody().get(0);
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getName(), actualDto.getName());
    }

    @Test
    public void listSpecialtiesNotFoundTest() {
        //Arrange
        Collection<Specialty> espec = new ArrayList<>();
        when(clinicService.findAllSpecialties()).thenReturn(espec);
        //Act
        ResponseEntity<List<SpecialtyDto>> listSpecialities =  specialtyRestController.listSpecialties();
        //Assert
        assertEquals(HttpStatus.NOT_FOUND,listSpecialities.getStatusCode());
        assertEquals(null,listSpecialities.getBody());
    }

    @Test
    public void getSpecialtySuccessTest() {
        //Arrange
        when(clinicService.findSpecialtyById(1)).thenReturn(arrangeSeciality());
        when(specialtyMapper.toSpecialtyDto(any())).thenReturn(arrangeSecialityDto());
        //Act
        ResponseEntity<SpecialtyDto> getSpecialty = specialtyRestController.getSpecialty(1);
        //Assert
        System.out.println(getSpecialty.getBody());
        assertEquals(HttpStatus.OK,getSpecialty.getStatusCode());
        assertEquals(arrangeSecialityDto(),getSpecialty.getBody());
        assertEquals(arrangeSecialityDto().getId(),getSpecialty.getBody().getId());
        assertEquals(arrangeSecialityDto().getName(),getSpecialty.getBody().getName());
    }


    @Test
    public void addSpecialtySuccessTest() {
//        //Arrange
        Specialty specialty = arrangeSeciality();
        when(specialtyMapper.toSpecialty(any(SpecialtyDto.class))).thenReturn(specialty);
        when(specialtyMapper.toSpecialtyDto(any(Specialty.class))).thenReturn(arrangeSecialityDto());
        //Act
        ResponseEntity<SpecialtyDto> addSpecialty = specialtyRestController.addSpecialty(arrangeSecialityDto());
        //Assert
        assertEquals(HttpStatus.CREATED,addSpecialty.getStatusCode());
        assertEquals(arrangeSecialityDto(),addSpecialty.getBody());
        assertEquals(arrangeSecialityDto().getId(),addSpecialty.getBody().getId());
        assertEquals(arrangeSecialityDto().getName(),addSpecialty.getBody().getName());
        verify(clinicService).saveSpecialty(specialty);
        assertNotNull(addSpecialty.getHeaders().getLocation());
        assertEquals("/api/specialties/1", addSpecialty.getHeaders().getLocation().getPath());
    }

    @Test
    public void updateSpecialitySuccessTest(){
        //Arrange
        Specialty specialty = arrangeSeciality();
        SpecialtyDto specialtyDto = arrangeSecialityDto();
        when(clinicService.findSpecialtyById(1)).thenReturn(specialty);
        when(specialtyMapper.toSpecialtyDto(any())).thenReturn(specialtyDto);
        //Act
        ResponseEntity<SpecialtyDto> updateSpecialty = specialtyRestController.updateSpecialty(1, specialtyDto);
        //Assert
        assertEquals(HttpStatus.NO_CONTENT,updateSpecialty.getStatusCode());
        assertEquals(specialtyDto,updateSpecialty.getBody());
        assertEquals(specialtyDto.getId(),updateSpecialty.getBody().getId());
        assertEquals(specialtyDto.getName(),updateSpecialty.getBody().getName());
        verify(clinicService).saveSpecialty(specialty);
    }

    @Test
    public void updateSpecialityNotFoundTest(){
        //Arrange
        SpecialtyDto specialtyDto = arrangeSecialityDto();
        when(clinicService.findSpecialtyById(1)).thenReturn(null);
        //Act
        ResponseEntity<SpecialtyDto> updateSpecialty = specialtyRestController.updateSpecialty(1, specialtyDto);
        //Assert
        assertEquals(HttpStatus.NOT_FOUND,updateSpecialty.getStatusCode());
        assertNull(updateSpecialty.getBody());
    }

    @Test
    public void deleteSpecialtySuccessTest(){
        //Arrange
        Specialty specialty = arrangeSeciality();
        SpecialtyDto specialtyDto = arrangeSecialityDto();
        when(clinicService.findSpecialtyById(1)).thenReturn(specialty);
        //Act
        ResponseEntity<SpecialtyDto> deleteSpecialty = specialtyRestController.deleteSpecialty(1);
        //Assert
        assertEquals(HttpStatus.NO_CONTENT,deleteSpecialty.getStatusCode());
        assertNull(deleteSpecialty.getBody());
    }
    @Test
    public void deleteSpecialtyNotFoundTest(){
        //Arrange
        when(clinicService.findSpecialtyById(1)).thenReturn(null);
        //act
        ResponseEntity<SpecialtyDto> deleteSpecialty = specialtyRestController.deleteSpecialty(1);
        //Assert
        assertEquals(HttpStatus.NOT_FOUND,deleteSpecialty.getStatusCode());
        assertNull(deleteSpecialty.getBody());
    }

}
