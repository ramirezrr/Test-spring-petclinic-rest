package org.springframework.samples.petclinic.service.clinicService;

import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.mapper.PetMapper;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.rest.controller.PetRestController;
import org.springframework.samples.petclinic.rest.dto.PetDto;
import org.springframework.samples.petclinic.rest.dto.PetTypeDto;
import org.springframework.samples.petclinic.service.ClinicService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PetRestControllerTest {
    @Mock
    private ClinicService clinicService;

    @Mock
    private PetMapper petMapper;

    @InjectMocks
    private PetRestController petRestController;

    @Test
    public void getPetSuccessTest() {
        //Arrange
        Integer petId = 1;
        PetDto petDto = new PetDto();
        petDto.setId(petId);
        petDto.setName("Pet 1");
        petDto.birthDate(LocalDate.now());
        PetTypeDto petTypeDto = new PetTypeDto();
        petTypeDto.setId(1);
        petTypeDto.setName("Pet Type");
        petDto.setType(petTypeDto);

        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Pet 2");
        PetType type = new PetType();
        type.setId(1);
        type.setName("Pet 3");
        pet.setType(type);

        when(clinicService.findPetById(anyInt())).thenReturn(pet);
        when(petMapper.toPetDto(any(Pet.class))).thenReturn(petDto);

        //Act
        ResponseEntity<PetDto> response = petRestController.getPet(petId);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(petDto, response.getBody());
        assertEquals(petId, response.getBody().getId());
        assertEquals(petDto.getId(), response.getBody().getId());
        assertEquals(petDto.getName(), response.getBody().getName());
        assertEquals(petDto.getBirthDate(), response.getBody().getBirthDate());
        assertEquals(petDto.getType(), response.getBody().getType());
        assertEquals(petDto.getType().getName(), response.getBody().getType().getName());

    }

    @Test
    public void getPetNotFoundTest() {
        //Arrange
        Integer petId = 1;
        when(clinicService.findPetById(anyInt())).thenReturn(null);

        //Act
        ResponseEntity<PetDto> response = petRestController.getPet(petId);

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void listPetsSuccessTest() {
        //Arrange

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Pet 2");
        PetType type = new PetType();
        type.setId(1);
        type.setName("Pet 3");
        pet.setType(type);

        Collection<Pet> pets = Arrays.asList(pet);
        List<PetDto> petsList = new ArrayList<>();
        PetDto petDto = new PetDto();
        petDto.setId(1);
        petDto.setName("Pet 1");
        petDto.birthDate(LocalDate.now());
        PetTypeDto petTypeDto = new PetTypeDto();
        petTypeDto.setId(1);
        petTypeDto.setName("Pet Type");
        petDto.setType(petTypeDto);
        petsList.add(petDto);
        when(clinicService.findAllPets()).thenReturn(pets);
        when(petMapper.toPetsDto(anyCollection())).thenReturn(petsList);

        //Act
        ResponseEntity<List<PetDto>> response = petRestController.listPets();

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(petsList, response.getBody());
        assertEquals(petsList.size(), response.getBody().size());
        assertEquals(petsList.get(0), response.getBody().get(0));
        assertEquals(petsList.get(0).getId(), response.getBody().get(0).getId());
        assertEquals(petsList.get(0).getName(), response.getBody().get(0).getName());
        assertEquals(petsList.get(0).getBirthDate(), response.getBody().get(0).getBirthDate());
        assertEquals(petsList.get(0).getType(), response.getBody().get(0).getType());
        assertEquals(petsList.get(0).getType().getName(), response.getBody().get(0).getType().getName());
        assertEquals(petsList.get(0).getType().getId(), response.getBody().get(0).getType().getId());

    }

    @Test
    public void listPetsNotFoundTest() {
        //Arrange
        Collection<Pet> pets = new ArrayList<>();
        when(clinicService.findAllPets()).thenReturn(pets);

        //Act
        ResponseEntity<List<PetDto>> response = petRestController.listPets();

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updatePetSuccessTest() {
        //Arrange
        Integer petId = 1;
        PetDto petDto = new PetDto();
        petDto.setId(petId);
        petDto.setName("Pet 1");
        petDto.birthDate(LocalDate.now());
        PetTypeDto petTypeDto = new PetTypeDto();
        petTypeDto.setId(1);
        petTypeDto.setName("Pet Type");
        petDto.setType(petTypeDto);
        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Pet 2");
        PetType type = new PetType();
        type.setId(1);
        type.setName("Pet 3");
        pet.setType(type);
        when(clinicService.findPetById(anyInt())).thenReturn(pet);
        when(petMapper.toPetType(any())).thenReturn(type);
        when(petMapper.toPetDto(any(Pet.class))).thenReturn(petDto);

        //Act
        ResponseEntity<PetDto> response = petRestController.updatePet(petId, petDto);

        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(petDto, response.getBody());
        verify(clinicService).savePet(pet);
        assertEquals(petId, response.getBody().getId());
        assertEquals(petDto.getId(), response.getBody().getId());
        assertEquals(petDto.getName(), response.getBody().getName());
        assertEquals(petDto.getBirthDate(), response.getBody().getBirthDate());
        assertEquals(petDto.getType(), response.getBody().getType());
        assertEquals(petDto.getType().getName(), response.getBody().getType().getName());
        assertEquals(petDto.getType().getId(), response.getBody().getType().getId());

    }

    @Test
    public void updatePetNotFoundTest() {
        //Arrange
        Integer petId = 1;
        PetDto petDto = new PetDto();
        petDto.setBirthDate(LocalDate.now());
        petDto.setName("Fluffy");
        when(clinicService.findPetById(anyInt())).thenReturn(null);
        //Act
        ResponseEntity<PetDto> response = petRestController.updatePet(petId, petDto);
        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void deletePetSuccessTest() {
        //Arrange
        Integer petId = 1;
        PetDto petDto = new PetDto();
        petDto.setId(petId);
        petDto.setName("Pet 1");
        petDto.birthDate(LocalDate.now());
        PetTypeDto petTypeDto = new PetTypeDto();
        petTypeDto.setId(1);
        petTypeDto.setName("Pet Type");
        petDto.setType(petTypeDto);

        Pet pet = new Pet();
        pet.setId(petId);
        pet.setName("Pet 2");
        PetType type = new PetType();
        type.setId(1);
        type.setName("Pet 3");
        pet.setType(type);
        when(clinicService.findPetById(anyInt())).thenReturn(pet);
        //Act
        ResponseEntity<PetDto> response = petRestController.deletePet(petId);
        //Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clinicService).deletePet(pet);
        assertEquals(null, response.getBody());
    }

    @Test
    public void deletePetNotFoundTest() {
        //Arrange
        Integer petId = 1;
        when(clinicService.findPetById(anyInt())).thenReturn(null);
        //Act
        ResponseEntity<PetDto> response = petRestController.deletePet(petId);
        //Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

    }

    @Test
    public void savePetSuccessTest() {
        //Arrange
        Integer petId = 1;
        PetDto petDto = new PetDto();
        petDto.setId(petId);
        Pet pet = new Pet();
        pet.setId(petId);
        when(petMapper.toPet(any(PetDto.class))).thenReturn(pet);
        //Act
        ResponseEntity<PetDto> addPet = petRestController.addPet(petDto);
        //Assert
        assertEquals(HttpStatus.OK, addPet.getStatusCode());
        verify(clinicService).savePet(pet);

    }
}
