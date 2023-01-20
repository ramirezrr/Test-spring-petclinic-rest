package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy
 *         Simple test to make sure that Bean Validation is working
 *         (useful when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
class ValidatorTests {

    private Validator createValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.afterPropertiesSet();
        return localValidatorFactoryBean;
    }


    @Test
    void shouldNotValidateWhenFirstNameEmpty() {

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Person person = new Person();
        person.setFirstName("");
        person.setLastName("smith");

        Validator validator = createValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("firstName");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

    @BeforeEach
    void initOwner()
    { /*
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Owner owner = new Owner();
        owner.setFirstName("Lisa");
        owner.setLastName("Smith");

        Validator validator = createValidator();
        //Set<ConstraintViolation<Owner>> constraintViolations = validator.validate(owner); */
    }
    @Test
    void shouldNotValidateWhenLastNameEmpty(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        Person person = new Person();
        person.setFirstName("lisa");
        person.setLastName("");

        Validator validator = createValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(person);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Person> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("lastName");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");

    }

    @Test
    void shouldNotValidateWhenNamedEntityNameEmpty(){
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        NamedEntity name = new NamedEntity();
        name.setName("");

        Validator validator = createValidator();
        Set<ConstraintViolation<NamedEntity>> constraintViolations = validator.validate(name);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<NamedEntity> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");

    }

    @Test
    void shouldNotValidateWhenOwnerAddressEmpty()
    {
        Owner owner = new Owner();
        owner.setFirstName("Lisa");
        owner.setLastName("Smith");

        LocaleContextHolder.setLocale(Locale.ENGLISH);
        owner.setAddress("");
        owner.setCity("Bremen");
        owner.setTelephone("9384903356");


        Validator validator = createValidator();
        Set<ConstraintViolation<Owner>> constraintViolations = validator.validate(owner);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }
    @Test
    void shouldNotValidateWhenOwnerCityEmpty()
    {
        Owner owner = new Owner();
        owner.setFirstName("Lisa");
        owner.setLastName("Smith");

        owner.setAddress("Strasse 13");
        owner.setCity("");
        owner.setTelephone("9384903356");

        Validator validator = createValidator();
        Set<ConstraintViolation<Owner>> constraintViolations = validator.validate(owner);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("city");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }
    @Test
    void shouldNotValidateWhenOwnerTelephoneEmpty()
    {
        Owner owner = new Owner();
        owner.setFirstName("Lisa");
        owner.setLastName("Smith");

        owner.setAddress("Strasse 13");
        owner.setCity("Bremen");
        owner.setTelephone("");

        Validator validator = createValidator();
        Set<ConstraintViolation<Owner>> constraintViolations = validator.validate(owner);

        assertThat(constraintViolations.size()).isEqualTo(1);
        ConstraintViolation<Owner> violation = constraintViolations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("telephone");
        assertThat(violation.getMessage()).isEqualTo("must not be empty");
    }

    @Test
    void shouldNotValidateWhenOwnerTelephoneDigitOutOfRange(Owner owner)
    {
        Owner owner = new Owner();
        owner.setFirstName("Lisa");
        owner.setLastName("Smith");

        owner.setAddress("Strasse 13");
        owner.setCity("Bremen");
        owner.setTelephone("9384903356");
    }


}
